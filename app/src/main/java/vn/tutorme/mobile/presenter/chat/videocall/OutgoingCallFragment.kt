package vn.tutorme.mobile.presenter.chat.videocall

import com.stringee.call.StringeeCall
import com.stringee.common.StringeeAudioManager
import org.json.JSONObject
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.OutGoingCallFragmentBinding

class OutgoingCallFragment : TutorMeFragment<OutGoingCallFragmentBinding>(R.layout.out_going_call_fragment) {

    companion object {
        const val USER_FROM = "USER_FROM"
        const val USER_TO = "USER_TO"
    }

    private lateinit var stringCall: StringeeCall
    private var state: StringeeCall.SignalingState? = null
    private var audioManager: StringeeAudioManager? = null

    private var userFrom: String? = null
    private var userTo: String? = null

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        userFrom = arguments?.getString(USER_FROM)
        userTo = arguments?.getString(USER_TO)
    }

    override fun onInitView() {
        super.onInitView()
        makeCall()
    }

    private fun makeCall() {

        audioManager = StringeeAudioManager.create(this@OutgoingCallFragment.context)
        audioManager?.start { p0, p1 ->
//            TODO("Not yet implemented")
        }
        audioManager?.setSpeakerphoneOn(true)

        stringCall = StringeeCall(MainVideoCallFragment.strClient, userFrom, userTo)
        stringCall.isVideoCall = true
        stringCall.setCallListener(object : StringeeCall.StringeeCallListener {
            override fun onSignalingStateChange(p0: StringeeCall?, p1: StringeeCall.SignalingState?, p2: String?, p3: Int, p4: String?) {
                state = p1
                if (state == StringeeCall.SignalingState.ENDED || state == StringeeCall.SignalingState.BUSY) {
                    onBackPressByFragment()
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
                    binding.flOutGoingCallView1.addView(stringCall.localView)
                    stringCall.renderLocalView(true)
                }
            }

            override fun onRemoteStream(p0: StringeeCall?) {
                mainActivity.runOnUiThread {
                    binding.flOutGoingCallView2.addView(stringCall.remoteView)
                    stringCall.renderRemoteView(true)
                }
            }

            override fun onCallInfo(p0: StringeeCall?, p1: JSONObject?) {
//                TODO("Not yet implemented")
            }
        })

        stringCall.makeCall(null)
    }
}
