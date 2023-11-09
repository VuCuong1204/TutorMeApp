package vn.tutorme.mobile.presenter.chat.videocall

import com.stringee.call.StringeeCall
import com.stringee.common.StringeeAudioManager
import com.stringee.common.StringeeConstant
import com.stringee.listener.StatusListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.VideoCallFragmentBinding

@AndroidEntryPoint
class VideoCallFragment : TutorMeFragment<VideoCallFragmentBinding>(R.layout.video_call_fragment) {

    companion object {
        const val CALL_ID_KEY = "CALL_ID_KEY"
        const val USER_FROM_KEY = "USER_FROM_KEY"
        const val USER_TO_KEY = "USER_TO_KEY"
        const val STATE_CALL_KEY = "STATE_CALL_KEY"
    }

    private var stringCall: StringeeCall? = null
    private var state: StringeeCall.SignalingState? = null
    private var callId: String? = null
    private var audioManager: StringeeAudioManager? = null

    private var userSendId: String? = null
    private var userReceiverId: String? = null
    private var callVideoType: CALL_VIDEO_TYPE? = null

    private var stateMicro = true
    private var stateVolume = false
    private var stateCamera = true

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        callId = arguments?.getString(CALL_ID_KEY)
        userSendId = arguments?.getString(USER_FROM_KEY)
        userReceiverId = arguments?.getString(USER_TO_KEY)
        callVideoType = CALL_VIDEO_TYPE.valuesOfName(arguments?.getInt(STATE_CALL_KEY))
    }

    override fun onInitView() {
        super.onInitView()
        initAnswer()

        binding.ivVideoCallMic.setOnSafeClick {
            stateMicro != stateMicro
            stringCall?.mute(stateMicro)
            binding.ivVideoCallMic.setImageResource(
                if (stateMicro) R.drawable.ic_micro
                else R.drawable.ic_micro_turn_off
            )
        }

        binding.ivVideoCallVolume.setOnSafeClick {
            stateVolume != stateVolume
            audioManager?.setSpeakerphoneOn(stateMicro)
            binding.ivVideoCallVolume.setImageResource(
                if (stateVolume) R.drawable.ic_loudspeaker_external
                else R.drawable.ic_loudspeaker
            )
        }

        binding.ivVideoCallCamera.setOnSafeClick {
            stateCamera != stateCamera
            stringCall?.enableVideo(stateCamera)
            binding.ivVideoCallCamera.setImageResource(
                if (stateVolume) R.drawable.ic_camera_video
                else R.drawable.ic_camera_gone
            )
        }

        binding.ivVideoCallRotate.setOnSafeClick {
            stringCall?.switchCamera(object : StatusListener() {
                override fun onSuccess() {
                }
            })
        }

        binding.ivVideoCallCancel.setOnSafeClick {
            stringCall?.let {
                stringCall!!.hangup(null)
                audioManager?.stop()
                onBackPressByFragment()
            }
        }
    }

    private fun initAnswer() {
        stringCall = if (callVideoType == CALL_VIDEO_TYPE.OUT_GOING_CALL_TYPE) {
            StringeeCall(MainVideoCallFragment.strClient, userSendId, userReceiverId)
        } else {
            mainActivity.callMap[callId]
        }

        audioManager = StringeeAudioManager.create(this@VideoCallFragment.context)
        audioManager?.start { _, _ -> }
        audioManager?.setSpeakerphoneOn(false)
        stringCall?.enableVideo(true)
        stringCall?.setQuality(StringeeConstant.QUALITY_FULLHD)
        stringCall?.setCallListener(object : StringeeCall.StringeeCallListener {
            override fun onSignalingStateChange(p0: StringeeCall?, p1: StringeeCall.SignalingState?, p2: String?, p3: Int, p4: String?) {
                state = p1
                if (state == StringeeCall.SignalingState.ENDED) {
                    onBackPressByFragment()
                    showSuccess(getAppString(R.string.video_call_end))
                }
            }

            override fun onError(p0: StringeeCall?, p1: Int, p2: String?) {
//                TODO("Not yet implemented")
            }

            override fun onHandledOnAnotherDevice(p0: StringeeCall?, p1: StringeeCall.SignalingState?, p2: String?) {
//                TODO("Not yet implemented")
            }

            override fun onMediaStateChange(p0: StringeeCall?, p1: StringeeCall.MediaState?) {
//                TODO("Not yet implemented")
            }

            override fun onLocalStream(p0: StringeeCall?) {
                mainActivity.runOnUiThread {
                    if (callVideoType == CALL_VIDEO_TYPE.OUT_GOING_CALL_TYPE) {
                        binding.flVideoCallSender.addView(p0?.localView)
                        binding.rlVideoCallReceiver.addView(p0?.localView)
                        binding.flVideoCallSender.gone()
                    } else {
                        binding.flVideoCallSender.addView(p0?.localView)
                        binding.flVideoCallSender.show()
                    }
                    p0?.renderLocalView(true)
                }
            }

            override fun onRemoteStream(p0: StringeeCall?) {
                mainActivity.runOnUiThread {
                    if (callVideoType == CALL_VIDEO_TYPE.OUT_GOING_CALL_TYPE) {
                        binding.flVideoCallSender.show()
                        binding.rlVideoCallReceiver.removeView(p0?.localView)
                    }
                    binding.rlVideoCallReceiver.addView(p0?.remoteView)
                    p0?.renderRemoteView(false)
                }
            }

            override fun onCallInfo(p0: StringeeCall?, p1: JSONObject?) {
//                TODO("Not yet implemented")
            }
        })

        if (callVideoType == CALL_VIDEO_TYPE.OUT_GOING_CALL_TYPE) {
            stringCall?.makeCall(null)
        } else {
            stringCall?.ringing(null)
        }
    }
}
