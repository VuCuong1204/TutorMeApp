package vn.tutorme.mobile.presenter.lessondetail

import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.common.anim.SlideAnimation
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.base.extension.toast
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.LessonDetailFragmentBinding
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE
import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.model.feedback.FeedBackInfo
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.presenter.dialog.InputFeedBackDialog
import vn.tutorme.mobile.presenter.dialog.InputRoomInfoDialog
import vn.tutorme.mobile.presenter.dialog.bottomsheetchat.BottomSheetChatDialog
import vn.tutorme.mobile.presenter.dialog.feedbacklist.FeedBackListDialog
import vn.tutorme.mobile.presenter.lessondetail.camera.FaceDetectionFragment
import vn.tutorme.mobile.presenter.lessondetail.model.ZoomRoomInfo

@AndroidEntryPoint
class LessonDetailFragment : TutorMeFragment<LessonDetailFragmentBinding>(R.layout.lesson_detail_fragment) {

    companion object {
        const val LESSON_ID_KEY = "LESSON_ID_KEY"
        const val CLASS_ID_KEY = "CLASS_ID_KEY"
        const val ZOOM_INFO_CHILD_KEY = "ZoomRoomInfo"
    }

    private val viewModel by viewModels<LessonDetailViewModel>()
    private val studentLessonAdapter by lazy { StudentLessonAdapter() }
    private lateinit var myRef: DatabaseReference
    private lateinit var postListener: ValueEventListener
    private lateinit var inputFeedBackDialog: InputFeedBackDialog

    private var isShowViewMore = false

    override fun onInitView() {
        super.onInitView()

        val userInfo = AppPreferences.userInfo?.copy(
            role = ROLE_TYPE.STUDENT_TYPE
        )
        AppPreferences.userInfo = userInfo
        addHeader()
        addAdapter()
        addRoomStateListenerEvent()
    }

    override fun onDestroyView() {
        removeListener()
        super.onDestroyView()
    }

    override fun onBackPressByFragment() {
        if (isShowViewMore) {
            binding.rlLessonDetailSupport.gone()
            isShowViewMore = false
        } else {
            super.onBackPressByFragment()
        }
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        coroutinesLaunch(viewModel.lessonDetailState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    it.data?.let { value -> setLessonInfo(value) }
                }
            }, canShowLoading = true)
        }

        coroutinesLaunch(viewModel.studentInfoLessonState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {

                    binding.cvLessonDetailRoot.apply {
                        setBaseAdapter(studentLessonAdapter.apply { lessonType = viewModel.lessonInfo?.status })
                        submitList(it.data)
                    }
                }
            }, canShowLoading = true)
        }

        coroutinesLaunch(viewModel.feedbackListState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    if (it.data is List<*>) {
                        val newList = mutableListOf<String>()
                        (it.data as? List<FeedBackInfo>)?.forEach { feedbackInfo ->
                            feedbackInfo.comment?.let { comment -> newList.add(comment) }
                        }
                        if (AppPreferences.userInfo?.role == ROLE_TYPE.TEACHER_TYPE) {
                            showFeedBackListDialog(newList)
                        } else {
                            showFeedbackDialog()
                        }
                    } else {
                        showSuccess(getAppString(R.string.feedback_success))
                    }
                }
            }, canShowLoading = true)
        }

        coroutinesLaunch(viewModel.attendanceStudentState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    viewModel.getStudentInfoLesson()
                }
            }, canShowLoading = true)
        }
    }

    private fun addAdapter() {
        binding.cvLessonDetailRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
        }

        addListener()
    }

    private fun addListener() {
        studentLessonAdapter.listener = object : IStudentLessonListener {
            override fun onRateClick(item: UserInfo) {
                toast("success")
            }
        }
    }

    private fun removeListener() {
        myRef.removeEventListener(postListener)
        studentLessonAdapter.listener = null
    }

    private fun addHeader() {
        binding.ivLessonDetailBack.setOnSafeClick {
            showFeatureDialog(true)
        }

        binding.ivLessonDetailViewMore.setOnSafeClick {
            addFragment(fragment = FaceDetectionFragment(), screenAnim = SlideAnimation())
        }

        binding.rlLessonDetailSupport.setOnSafeClick {
            showFeatureDialog(false)
        }

        binding.tvLessonDetailInfoRoom.setOnSafeClick {
            showFeatureDialog(false)
            showRoomInfoDialog()
        }

        binding.tvLessonDetailChatRoom.setOnSafeClick {
            showFeatureDialog(false)
            showRoomChatDialog()
        }

        binding.tvLessonDetailSupport.setOnSafeClick {
            showFeatureDialog(false)
            showWarning(getAppString(R.string.update_feature))
        }

        binding.tvLessonDetailFeedback.setOnSafeClick {
            showFeatureDialog(false)
            viewModel.getFeedbackList()
        }
    }

    private fun showFeatureDialog(state: Boolean) {
        isShowViewMore = state
        if (state) {
            binding.rlLessonDetailSupport.show()
        } else {
            binding.rlLessonDetailSupport.gone()
        }
    }

    private fun showFeedBackListDialog(value: List<String>) {
        FeedBackListDialog().apply {
            dataList = value
        }.show(childFragmentManager, FeedBackListDialog::class.simpleName)
    }

    private fun showRoomInfoDialog() {
        InputRoomInfoDialog().apply {
            roomId = viewModel.zoomRoomInfo?.zoomId ?: STRING_DEFAULT
            roomPassword = viewModel.zoomRoomInfo?.zoomId ?: STRING_DEFAULT
            listener = object : InputRoomInfoDialog.IInputRoomInfoListener {
                override fun onSendClick(id: String, password: String) {
                    val zoomRoomInfo = ZoomRoomInfo(id, password)
                    sendZoomRoomInfoListenerEvent(zoomRoomInfo)
                }
            }
        }.show(childFragmentManager, InputRoomInfoDialog::class.simpleName)
    }

    private fun showRoomChatDialog() {
        BottomSheetChatDialog().apply {
            lessonId = viewModel.lessonInfo?.lessonId
        }.show(childFragmentManager, BottomSheetChatDialog::class.simpleName)
    }

    private fun showFeedbackDialog() {
        inputFeedBackDialog = InputFeedBackDialog().apply {
            listener = object : InputFeedBackDialog.IInputFeedBackListener {
                override fun onConfirmClick(text: String) {
                    viewModel.feedBackLessonUseCase(content = text)
                }
            }
        }
        inputFeedBackDialog.show(childFragmentManager, BottomSheetChatDialog::class.simpleName)
    }

    private fun setLessonInfo(value: LessonInfo) {
        binding.tvLessonDetailId.text = value.classId
        binding.tvLessonDetailAvatar.setImageDrawable(
            when (value.status) {
                LESSON_STATUS.UPCOMING_STATUS -> getAppDrawable(R.drawable.ic_upcoming)
                LESSON_STATUS.HAPPENING_STATUS -> getAppDrawable(R.drawable.ic_happenning)
                LESSON_STATUS.CANCEL_STATUS -> getAppDrawable(R.drawable.ic_cancel)
                LESSON_STATUS.TOOK_PLACE_STATUS -> getAppDrawable(R.drawable.ic_took_place)
                else -> getAppDrawable(R.drawable.ic_upcoming)
            }
        )
        binding.tvLessonDetailState.background = when (value.status) {
            LESSON_STATUS.UPCOMING_STATUS -> getAppDrawable(R.drawable.shape_bg_secondary_corner_8)
            LESSON_STATUS.HAPPENING_STATUS -> getAppDrawable(R.drawable.shape_bg_status_manual_corner_10)
            LESSON_STATUS.CANCEL_STATUS -> getAppDrawable(R.drawable.shape_bg_classic_corner_10)
            LESSON_STATUS.TOOK_PLACE_STATUS -> getAppDrawable(R.drawable.shape_bg_state_sucess_corner_8)
            else -> getAppDrawable(R.drawable.shape_bg_secondary_corner_8)
        }

        binding.tvLessonDetailState.text = when (value.status) {
            LESSON_STATUS.UPCOMING_STATUS -> getAppString(R.string.upcoming)
            LESSON_STATUS.HAPPENING_STATUS -> getAppString(R.string.happening)
            LESSON_STATUS.CANCEL_STATUS -> getAppString(R.string.cancel)
            LESSON_STATUS.TOOK_PLACE_STATUS -> getAppString(R.string.took_place)
            else -> getAppString(R.string.upcoming)
        }

        binding.tvLessonDetailClass.text = value.getClassTitle()
        binding.tvLessonDetailAdvanced.text = value.level
        binding.tvLessonDetailLesson.text = value.getSession()

        binding.tvLessonDetailNumber.text = if (value.status == LESSON_STATUS.TOOK_PLACE_STATUS) {
            value.getNumberMemberRatio()
        } else {
            value.getNumberMember()
        }
    }

    private fun addRoomStateListenerEvent() {
        myRef = FirebaseDatabase.getInstance().getReference(ZOOM_INFO_CHILD_KEY)
        postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    viewModel.zoomRoomInfo = it.child(viewModel.lessonInfo?.lessonId.toString()).getValue(ZoomRoomInfo::class.java)
                }
                if (System.currentTimeMillis() in (viewModel.lessonInfo?.timeBegin?.times(1000)
                        ?: 1)..(viewModel.lessonInfo?.timeEnd?.times(1000) ?: 1)
                ) {
                    viewModel.updateStateLesson(state = LESSON_STATUS.HAPPENING_STATUS)
                } else if (System.currentTimeMillis() <= (viewModel.lessonInfo?.timeBegin?.times(1000)
                        ?: 1)
                ) {
                    viewModel.updateStateLesson(state = LESSON_STATUS.UPCOMING_STATUS)
                } else {
                    viewModel.updateStateLesson(state = LESSON_STATUS.TOOK_PLACE_STATUS)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showError(getAppString(R.string.error_join_room))
            }
        }

        myRef.addValueEventListener(postListener)
    }


    private fun sendZoomRoomInfoListenerEvent(zoomRoomInfo: ZoomRoomInfo) {
        viewModel.lessonInfo?.let {
            myRef.child(it.toString()).setValue(zoomRoomInfo)
                .addOnFailureListener {
                    showError(getAppString(R.string.send_zoom_info_error))
                }
        }
    }
}
