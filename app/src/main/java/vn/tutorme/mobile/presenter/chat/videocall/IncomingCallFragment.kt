package vn.tutorme.mobile.presenter.chat.videocall

import com.stringee.call.StringeeCall
import com.stringee.common.StringeeAudioManager
import com.stringee.common.StringeeConstant
import com.stringee.listener.StatusListener
import org.json.JSONObject
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.base.extension.toast
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.IncomingCallFragmentBinding


class IncomingCallFragment : TutorMeFragment<IncomingCallFragmentBinding>(R.layout.incoming_call_fragment) {

    companion object {
        const val CALL_ID = "CALL_ID"
    }

    private var stringCall: StringeeCall? = null
    private var state: StringeeCall.SignalingState? = null
    private var callId: String? = null
    private var audioManager: StringeeAudioManager? = null

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        callId = arguments?.getString(CALL_ID)
    }

    override fun onInitView() {
        super.onInitView()
        initAnswer()
        initView()
    }

    override fun onDestroyView() {
        binding.flIncomingCallVideo.removeView(stringCall?.localView)
        binding.flIncomingCallVideo2.removeView(stringCall?.remoteView)
        super.onDestroyView()
    }

    private fun initAnswer() {
        stringCall = MainVideoCallFragment.callMap[callId]

        audioManager = StringeeAudioManager.create(this@IncomingCallFragment.context)
        audioManager?.start { _, _ ->
//            TODO("Not yet implemented")
        }
        audioManager?.setSpeakerphoneOn(true)
        stringCall?.enableVideo(true)
        stringCall?.setQuality(StringeeConstant.QUALITY_FULLHD)
        stringCall?.setCallListener(object : StringeeCall.StringeeCallListener {
            override fun onSignalingStateChange(p0: StringeeCall?, p1: StringeeCall.SignalingState?, p2: String?, p3: Int, p4: String?) {
                state = p1
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
                    binding.flIncomingCallVideo.addView(p0?.localView)
                    p0?.renderLocalView(true)
                }
            }

            override fun onRemoteStream(p0: StringeeCall?) {
                mainActivity.runOnUiThread {
                    binding.flIncomingCallVideo2.addView(p0?.remoteView)
                    p0?.renderRemoteView(false)
                }
            }

            override fun onCallInfo(p0: StringeeCall?, p1: JSONObject?) {
//                TODO("Not yet implemented")
            }
        })

        stringCall?.ringing(
            object : StatusListener() {
                override fun onSuccess() {
                    toast("thành công")
                }
            })
    }

    private fun initView() {
        binding.btnIncomingConfirm.setOnSafeClick {
            stringCall?.let {
                stringCall?.answer(null)
                binding.btnIncomingConfirm.gone()
                binding.btnIncomingReject.gone()
                binding.btnIncomingEnd.show()
            }
        }

        binding.btnIncomingReject.setOnSafeClick {
            stringCall?.let {
                stringCall?.reject(null)
                onBackPressByFragment()
            }
        }

        binding.btnIncomingEnd.setOnSafeClick {
            stringCall?.let {
                stringCall!!.hangup(null)
                onBackPressByFragment()
            }
        }
    }
}
