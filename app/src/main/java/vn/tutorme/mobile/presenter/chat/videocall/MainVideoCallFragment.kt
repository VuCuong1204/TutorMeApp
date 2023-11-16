package vn.tutorme.mobile.presenter.chat.videocall

import android.Manifest
import androidx.core.os.bundleOf
import com.stringee.StringeeClient
import com.stringee.call.StringeeCall
import com.stringee.call.StringeeCall2
import com.stringee.exception.StringeeError
import com.stringee.listener.StringeeConnectionListener
import org.json.JSONObject
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.BaseActivity
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.MainVideoCallFragmentBinding
import vn.tutorme.mobile.presenter.chat.videocall.IncomingCallFragment.Companion.CALL_ID
import vn.tutorme.mobile.presenter.chat.videocall.OutgoingCallFragment.Companion.USER_FROM
import vn.tutorme.mobile.presenter.chat.videocall.OutgoingCallFragment.Companion.USER_TO


class MainVideoCallFragment : TutorMeFragment<MainVideoCallFragmentBinding>(R.layout.main_video_call_fragment) {

    companion object {
        var strClient: StringeeClient? = null
        val callMap: HashMap<String, StringeeCall> = hashMapOf()
    }

    private val token_user5 = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTSy4wLks3em5NcWJPWEFIQktha29FOHZNbEVEWnBGWEN2Uk5FLTE2OTkyNDQ5NTUiLCJpc3MiOiJTSy4wLks3em5NcWJPWEFIQktha29FOHZNbEVEWnBGWEN2Uk5FIiwiZXhwIjoxNjk5MzMxMzU1LCJ1c2VySWQiOiJ1c2VyNSJ9.BPyCZ7kl7OoXhGdckuPepf9C82lq0UQbgyZ7ZL6I89s"
    private val token_user6 = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTSy4wLks3em5NcWJPWEFIQktha29FOHZNbEVEWnBGWEN2Uk5FLTE2OTkyNDQ5NjMiLCJpc3MiOiJTSy4wLks3em5NcWJPWEFIQktha29FOHZNbEVEWnBGWEN2Uk5FIiwiZXhwIjoxNjk5MzMxMzYzLCJ1c2VySWQiOiJ1c2VyNiJ9.F56Z563SbrNm7h59GE5uXE-g9lwFfGDVQavp74ZmdTs"
    private val token_user10 = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTSy4wLks3em5NcWJPWEFIQktha29FOHZNbEVEWnBGWEN2Uk5FLTE2OTkyNTY4MzkiLCJpc3MiOiJTSy4wLks3em5NcWJPWEFIQktha29FOHZNbEVEWnBGWEN2Uk5FIiwiZXhwIjoxNjk5MjYwNDM5LCJ1c2VySWQiOiJ1c2VyMTAifQ.aLMbkd9qbfttiDqiBryXogqaFfLeYDdxBCF05InXFWo"
    private val token_user11 = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTSy4wLks3em5NcWJPWEFIQktha29FOHZNbEVEWnBGWEN2Uk5FLTE2OTkyNTY5NDciLCJpc3MiOiJTSy4wLks3em5NcWJPWEFIQktha29FOHZNbEVEWnBGWEN2Uk5FIiwiZXhwIjoxNjk5MjYwNTQ3LCJ1c2VySWQiOiJ1c2VyMTEifQ.PpvM9jtFw2_LfDNHMN285ArUlx02MLIBeI4MQG6mDjQ"

    override fun onInitView() {
        super.onInitView()

        mainActivity.doRequestPermission(arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        ), object : BaseActivity.PermissionListener {
            override fun onAllow() {
                initStringee()
            }

            override fun onDenied(neverAskAgainPermissionList: List<String>) {}
        })

        initView()
    }

    private fun initStringee() {
        strClient = StringeeClient(mainActivity)
        strClient?.setConnectionListener(object : StringeeConnectionListener {
            override fun onConnectionConnected(p0: StringeeClient?, p1: Boolean) {
                mainActivity.runOnUiThread {
                    binding.tvMainVideoCallId.text = p0?.userId
                }
            }

            override fun onConnectionDisconnected(p0: StringeeClient?, p1: Boolean) {
//                TODO("Not yet implemented")
            }

            override fun onIncomingCall(p0: StringeeCall) {
                mainActivity.runOnUiThread {
                    callMap[p0.callId] = p0
                    replaceFragment(IncomingCallFragment(), bundleOf(
                        CALL_ID to p0.callId
                    ))
                }
            }

            override fun onIncomingCall2(p0: StringeeCall2?) {
//                TODO("Not yet implemented")
            }

            override fun onConnectionError(p0: StringeeClient?, p1: StringeeError?) {
//                TODO("Not yet implemented")
            }

            override fun onRequestNewToken(p0: StringeeClient?) {
//                TODO("Not yet implemented")
            }

            override fun onCustomMessage(p0: String?, p1: JSONObject?) {
//                TODO("Not yet implemented")
            }

            override fun onTopicMessage(p0: String?, p1: JSONObject?) {
//                TODO("Not yet implemented")
            }
        })

        strClient?.connect(token_user11)
    }

    private fun initView() {
        binding.btnMainVideoCall.setOnSafeClick {
            replaceFragment(OutgoingCallFragment(), bundleOf(
                USER_FROM to strClient?.userId,
                USER_TO to binding.edtMainVideoCallContent.text.toString().trim()
            ))
        }
    }
}
