package vn.tutorme.mobile.presenter.lessondetail.camera

import android.Manifest
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.BaseActivity
import vn.tutorme.mobile.base.common.activityresultlauncher.OpenAppSettingResult
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.extension.toast
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.FaceDetectionFragmentBinding
import vn.tutorme.mobile.presenter.widget.detectview.DETECT_STATE
import vn.tutorme.mobile.presenter.widget.detectview.RADIUS_DEFAULT
import java.text.SimpleDateFormat
import java.util.Locale

class FaceDetectionFragment : TutorMeFragment<FaceDetectionFragmentBinding>(R.layout.face_detection_fragment) {

    companion object {
        const val MIN_ANALYSIS_INTERVAL = 400L
        const val LIMIT_DEFAULT = 40
        const val CODE_REQUEST = 101
    }

    private val openAppSettingResult by lazy { OpenAppSettingResult() }

    private var imageCapture: ImageCapture? = null
    private lateinit var imageAnalysis: ImageAnalysis
    private lateinit var highAccuracyOpts: FaceDetectorOptions
    private var lastAnalysisTime = 0L
    private lateinit var cropBitmap: Bitmap

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        openAppSettingResult.register(this)
    }

    override fun onInitView() {
        super.onInitView()
        getPermission()
        startCamera()
        configFaceDetector()
        addHeader()
    }

    override fun onDestroyView() {
        openAppSettingResult.unregister()
        super.onDestroyView()
    }

    override fun onBackPressByFragment() {
        backFragment(FaceDetectionFragment::class.java.simpleName)
    }

    private fun addHeader() {
        binding.ivFaceDetectionClose.setOnSafeClick {
            onBackPressByFragment()
        }
    }

    /**
     * Xin quyền camera
     */

    private fun getPermission() {
        mainActivity.doRequestPermission(arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ), object : BaseActivity.PermissionListener {
            override fun onAllow() {

            }

            override fun onDenied(neverAskAgainPermissionList: List<String>) {
                if (neverAskAgainPermissionList.isNotEmpty()) {
                    openAppSettingResult.launch(mainActivity)
                }
            }
        })
    }

    /**
     * Bắt đầu một cameraX
     */

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(mainActivity)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(binding.pvFaceDetectionPreview.surfaceProvider) }

            imageCapture = ImageCapture.Builder().build()

            imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(binding.pvFaceDetectionPreview.width, binding.pvFaceDetectionPreview.height))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { imageAnalysis ->
                    imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(mainActivity)) { imageProxy ->
                        val rotatedBitmap = rotateBitmap(imageProxy.toBitmap(), -90f)

//                      Cắt phần giữa hình tròn dựa vào tọa độ hình tròn
                        val xRatio = rotatedBitmap.width.toFloat() / binding.pvFaceDetectionPreview.width.toFloat()

                        cropBitmap = cropBitmap(
                            rotatedBitmap,
                            (RADIUS_DEFAULT.toInt() * xRatio).toInt(),
                            (rotatedBitmap.height.toFloat() / 2 - rotatedBitmap.width.toFloat() / 2 + RADIUS_DEFAULT.toInt() * xRatio).toInt(),
                            (rotatedBitmap.width.toFloat() - RADIUS_DEFAULT * 2 * xRatio).toInt(),
                            (rotatedBitmap.width.toFloat() - RADIUS_DEFAULT * 2 * xRatio).toInt()
                        )

//                        Giới hạn số frame được gửi lên trong 1 thời gian quy định
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastAnalysisTime >= MIN_ANALYSIS_INTERVAL) {
                            lastAnalysisTime = currentTime
                            detectInImage(cropBitmap)
                            detectInImage(cropBitmap)
                        }

                        imageProxy.close()
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalysis
                )

            } catch (exception: Exception) {
                Log.e("TAG", "Use case binding failed", exception)
            }
        }, ContextCompat.getMainExecutor(mainActivity))
    }


    /**
     * Chụp hình xong thực hiện lưu vào bộ nhớ máy
     */

    private fun imageCapture() {

        val name = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                mainActivity.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ).build()

        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(mainActivity),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${outputFileResults.savedUri}"
                    toast(msg)
                    Log.d("TAG", msg)
                }

                override fun onError(exception: ImageCaptureException) {
                    val msg = "Photo capture failed: ${exception.message}"
                    toast(msg)
                    Log.d("TAG", msg)
                }
            }
        )
    }

    /**
     * Chuyển tọa độ face trả về từ bitmap sang một tọa độ bất kì dựa vào ratio
     */

    private fun convertImageCoordinatesToScreenCoordinates(
        faceRect: Rect,
        imageSize: Size,
        screenSize: Size
    ): Rect {
        val xRatio = screenSize.width.toFloat() / imageSize.width.toFloat()
        val yRatio = screenSize.height.toFloat() / imageSize.height.toFloat()

        val left = (faceRect.left * xRatio).toInt()
        val top = (faceRect.top * yRatio).toInt()
        val right = (faceRect.right * xRatio).toInt()
        val bottom = (faceRect.bottom * yRatio).toInt()

        return Rect(
            left + RADIUS_DEFAULT.toInt(),
            top + screenSize.width / 2,
            right + RADIUS_DEFAULT.toInt(),
            bottom + screenSize.width / 2
        )
    }

    /**
     * Cấu hình FaceDetector
     */

    private fun configFaceDetector() {
        highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
            .build()
    }

    /**
     * Nhận bitmap từ camera trả về , mỗi bitmap là 1 frame của camera
     */

    private fun detectInImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val detector = FaceDetection.getClient(highAccuracyOpts)
        detector.process(image)
            .addOnSuccessListener { faces ->
                if (faces.isNotEmpty()) {
                    faces.forEach {
                        if (it.boundingBox.left >= bitmap.width / 6 &&
                            it.boundingBox.left <= bitmap.width / 2 &&
                            it.boundingBox.right <= bitmap.width &&
                            it.boundingBox.bottom <= bitmap.height &&
                            it.boundingBox.top >= bitmap.height / 8
                        ) {
                            binding.tvFaceDetectionName.text = getAppString(R.string.detach_success)
                            binding.tvFaceDetectionName.setTextColor(getAppColor(R.color.blue))
                            binding.ccvFaceDetectionCircle.setCircleState(DETECT_STATE.SUCCESS)
                        } else {
                            binding.tvFaceDetectionName.text = getAppString(R.string.detach_fail)
                            binding.tvFaceDetectionName.setTextColor(getAppColor(R.color.red))
                            binding.ccvFaceDetectionCircle.setCircleState(DETECT_STATE.FAILED)
                        }

                        binding.dvFaceDetectionAvatar.setRect(it.boundingBox)
                    }
                } else {
                    binding.tvFaceDetectionName.text = ""
                    binding.tvFaceDetectionName.setTextColor(getAppColor(R.color.white))
                    binding.ccvFaceDetectionCircle.setCircleState(DETECT_STATE.NONE)
                    binding.dvFaceDetectionAvatar.setRect(null)
                }
            }
            .addOnFailureListener {}
    }

    /**
     * Xoay bitmap
     */

    private fun rotateBitmap(sourceBitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(
            sourceBitmap,
            0,
            0,
            sourceBitmap.width,
            sourceBitmap.height,
            matrix,
            true
        )
    }

    /**
     * Cắt một phần bitmap dựa vào tọa độ bắt đầu và width , height cần lấy
     */

    private fun cropBitmap(originalBitmap: Bitmap, startX: Int, startY: Int, width: Int, height: Int): Bitmap {
        // Tạo một Bitmap mới để lưu phần cắt ra.
        val croppedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Tạo một Rect để xác định vùng cắt trên originalBitmap.
        val srcRect = Rect(startX, startY, startX + width, startY + height)

        // Tạo một Rect để xác định vị trí vẽ trên croppedBitmap.
        val destRect = Rect(0, 0, width, height)

        // Tạo một Canvas để vẽ phần cắt từ originalBitmap lên croppedBitmap.
        val canvas = Canvas(croppedBitmap)

        // Vẽ phần cắt từ originalBitmap lên croppedBitmap.
        canvas.drawBitmap(originalBitmap, srcRect, destRect, null)

        return croppedBitmap
    }
}
