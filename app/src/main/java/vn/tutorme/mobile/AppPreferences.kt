package vn.tutorme.mobile

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import vn.tutorme.mobile.domain.model.authen.UserInfo

object AppPreferences {
    lateinit var preferences: SharedPreferences
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var gson: Gson

    fun init(context: Context) {
        preferences = context.getSharedPreferences(context.packageName, MODE)
        gson = Gson()
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    private inline fun SharedPreferences.commit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.commit()
    }

    private val SAVE_NAME_USER_KEY = Pair("SAVE_NAME_USER_KEY", "")
    private val SAVE_PASS_WORD_KEY = Pair("SAVE_PASS_WORD_KEY", "")
    private val SAVE_TOKEN_KEY = Pair("SAVE_TOKEN_KEY", "")
    private val SAVE_CHECK_INFO_KEY = Pair("SAVE_CHECK_INFO_KEY", false)
    private val SAVE_USER_INFO_KEY = Pair("SAVE_USER_INFO_KEY", "")

    var userNameAccount: String?
        get() = preferences.getString(SAVE_NAME_USER_KEY.first, SAVE_NAME_USER_KEY.second)
        set(value) = preferences.edit {
            it.putString(SAVE_NAME_USER_KEY.first, value)
        }

    var passwordAccount: String?
        get() = preferences.getString(SAVE_PASS_WORD_KEY.first, SAVE_PASS_WORD_KEY.second)
        set(value) = preferences.edit {
            it.putString(SAVE_PASS_WORD_KEY.first, value)
        }

    var checkSaveInfo: Boolean?
        get() = preferences.getBoolean(SAVE_CHECK_INFO_KEY.first, SAVE_CHECK_INFO_KEY.second)
        set(value) = preferences.edit {
            it.putBoolean(SAVE_CHECK_INFO_KEY.first, value ?: false)
        }

    var token: String?
        get() = preferences.getString(SAVE_TOKEN_KEY.first, SAVE_TOKEN_KEY.second)
        set(value) = preferences.edit {
            it.putString(SAVE_TOKEN_KEY.first, value)
        }

    var userInfo: UserInfo?
        get() = gson.fromJson(
            preferences.getString(SAVE_USER_INFO_KEY.first, SAVE_USER_INFO_KEY.second), UserInfo::class.java
        )
        set(value) = preferences.edit {
            it.putString(SAVE_USER_INFO_KEY.first, gson.toJson(value))
        }
}
