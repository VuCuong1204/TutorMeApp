package vn.tutorme.mobile.presenter.lessondetail.zoomsdk

import android.content.Context
import android.os.Build
import org.json.JSONObject
import us.zoom.sdk.JoinMeetingOptions
import us.zoom.sdk.JoinMeetingParams
import us.zoom.sdk.ZoomSDK
import us.zoom.sdk.ZoomSDKInitParams
import us.zoom.sdk.ZoomSDKInitializeListener
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppString
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class ZoomSdkConfig(val context: Context) {

    private fun initializeSdk() {
        val sdk = ZoomSDK.getInstance()
        val params = ZoomSDKInitParams().apply {
            jwtToken = getJWTToken()
            domain = "zoom.us"
            enableLog = false
        }
        val listener = object : ZoomSDKInitializeListener {
            override fun onZoomSDKInitializeResult(errorCode: Int, internalErrorCode: Int) = Unit
            override fun onZoomAuthIdentityExpired() = Unit
        }
        sdk.initialize(context, listener, params)
    }

    private fun getJWTToken(): String {
        val meetingNumber = 12042001L
        val role = 0

        return generateSignature(meetingNumber, role)
    }

    private fun generateSignature(meetingNumber: Long, role: Int): String {
        val iat = (System.currentTimeMillis() / 1000).toInt() - 30
        val exp = (System.currentTimeMillis() / 1000).toInt() + 7200

        val header = JSONObject()
        header.put("alg", "HS256")
        header.put("typ", "JWT")

        val payload = JSONObject()
        payload.put("sdkKey", getAppString(R.string.sdk_key))
        payload.put("appKey", getAppString(R.string.sdk_key))
        payload.put("mn", meetingNumber)
        payload.put("role", role)
        payload.put("iat", iat)
        payload.put("exp", exp)
        payload.put("tokenExp", exp)

        val encodedHeader = if (Build.VERSION.SDK_INT >= 26) {
            Base64.getEncoder().encodeToString(header.toString().toByteArray())
        } else {
            android.util.Base64.encodeToString(header.toString().toByteArray(), 0)
        }

        val encodedPayload = if (Build.VERSION.SDK_INT >= 26) {
            Base64.getEncoder().encodeToString(payload.toString().toByteArray())
        } else {
            android.util.Base64.encodeToString(payload.toString().toByteArray(), 0)
        }

        val dataToSign = "$encodedHeader.$encodedPayload"

        val secretBytes = getAppString(R.string.sdk_secret).toByteArray()
        val signingKey = SecretKeySpec(secretBytes, "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(signingKey)
        val signatureBytes = mac.doFinal(dataToSign.toByteArray())
        val signature = if (Build.VERSION.SDK_INT >= 26) {
            Base64.getEncoder().encodeToString(signatureBytes)
        } else {
            android.util.Base64.encodeToString(signatureBytes, 0)
        }

        return "$dataToSign.$signature"
    }

    fun register() {
        initializeSdk()
    }

    fun joinRoom(username: String, id: String, password: String) {
        val meetingService = ZoomSDK.getInstance().meetingService
        val options = JoinMeetingOptions()
        val params = JoinMeetingParams().apply {
            this.displayName = username
            this.meetingNo = id
            this.password = password
        }
        meetingService.joinMeetingWithParams(context, params, options)
    }
}
