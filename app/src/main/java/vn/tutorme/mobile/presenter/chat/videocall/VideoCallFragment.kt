package vn.tutorme.mobile.presenter.chat.videocall

import android.media.MediaPlayer
import com.stringee.call.StringeeCall
import com.stringee.common.StringeeAudioManager
import com.stringee.common.StringeeConstant
import com.stringee.listener.StatusListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.SendVideoCallState
import vn.tutorme.mobile.base.common.eventbus.EventBusManager
import vn.tutorme.mobile.base.extension.setOnSafeClick
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

    private var stateMicro = false
    private var stateVolume = false
    private var stateCamera = true

    private var mediaPlayer: MediaPlayer? = null

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        callId = arguments?.getString(CALL_ID_KEY)
        userSendId = arguments?.getString(USER_FROM_KEY)
        userReceiverId = arguments?.getString(USER_TO_KEY)
        callVideoType = CALL_VIDEO_TYPE.valuesOfName(arguments?.getInt(STATE_CALL_KEY))
    }

    override fun onInitView() {
        super.onInitView()

        mediaPlayer = MediaPlayer.create(this@VideoCallFragment.context, R.raw.ring_phone_coming)
        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.start()
        }
        mediaPlayer?.start()

        initAnswer()

        binding.ivVideoCallMic.setOnSafeClick {
            stateMicro = !stateMicro
            stringCall?.mute(stateMicro)
            binding.ivVideoCallMic.setImageResource(
                if (stateMicro) R.drawable.ic_micro_turn_off
                else R.drawable.ic_micro
            )
        }

        binding.ivVideoCallVolume.setOnSafeClick {
            stateVolume = !stateVolume
            audioManager?.setSpeakerphoneOn(stateVolume)
            binding.ivVideoCallVolume.setImageResource(
                if (stateVolume) R.drawable.ic_loudspeaker_external
                else R.drawable.ic_loudspeaker
            )
        }

        binding.ivVideoCallCamera.setOnSafeClick {
            stateCamera = !stateCamera
            stringCall?.enableVideo(stateCamera)
            binding.ivVideoCallCamera.setImageResource(
                if (stateCamera) R.drawable.ic_camera_video
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
                EventBusManager.instance?.postPending(SendVideoCallState(CALL_VIDEO_STATE.ENDED))
                onBackPressByFragment()
            }
        }
    }

    override fun onBackPressByFragment() {
        backFragment(VideoCallFragment::class.java.simpleName)
    }

    override fun onDestroyView() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroyView()
    }

    private fun initAnswer() {
        stringCall = if (callVideoType == CALL_VIDEO_TYPE.OUT_GOING_CALL_TYPE) {
            StringeeCall(mainActivity.strClient, userSendId, userReceiverId)
        } else {
            mainActivity.callMap[callId]
        }

        audioManager = StringeeAudioManager.create(this@VideoCallFragment.context)
        audioManager?.start { _, _ -> }
        audioManager?.setSpeakerphoneOn(false)
        stringCall?.enableVideo(true)
        stringCall?.isVideoCall = true
        stringCall?.setQuality(StringeeConstant.QUALITY_FULLHD)
        stringCall?.setCallListener(object : StringeeCall.StringeeCallListener {
            override fun onSignalingStateChange(p0: StringeeCall?, p1: StringeeCall.SignalingState?, p2: String?, p3: Int, p4: String?) {
                state = p1
                if (state == StringeeCall.SignalingState.ENDED) {
                    EventBusManager.instance?.postPending(SendVideoCallState(CALL_VIDEO_STATE.ENDED))
                    onBackPressByFragment()
                } else if (state == StringeeCall.SignalingState.BUSY) {
                    EventBusManager.instance?.postPending(SendVideoCallState(CALL_VIDEO_STATE.BUSY))
                    onBackPressByFragment()
                }
            }

            override fun onError(p0: StringeeCall?, p1: Int, p2: String?) {
                EventBusManager.instance?.postPending(SendVideoCallState(CALL_VIDEO_STATE.ERROR))
                onBackPressByFragment()
            }

            override fun onHandledOnAnotherDevice(p0: StringeeCall?, p1: StringeeCall.SignalingState?, p2: String?) {
//                TODO("Not yet implemented")
            }

            override fun onMediaStateChange(p0: StringeeCall?, p1: StringeeCall.MediaState?) {
//                TODO("Not yet implemented")
            }

            override fun onLocalStream(p0: StringeeCall?) {
                mainActivity.runOnUiThread {
                    binding.flVideoCallSender.addView(p0?.localView)
                    p0?.renderLocalView(true)
                }
            }

            override fun onRemoteStream(p0: StringeeCall?) {
                mainActivity.runOnUiThread {
                    binding.rlVideoCallReceiver.addView(p0?.remoteView)
                    mediaPlayer?.pause()
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
            stringCall?.answer(null)
        }
    }
}
