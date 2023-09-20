package vn.tutorme.mobile

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT

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

    private const val SAVE_NAME_USER_KEY = "SAVE_NAME_USER_KEY"
    private const val SAVE_PASS_WORD_KEY = "SAVE_PASS_WORD_KEY"
    private const val SAVE_TOKEN_KEY = "SAVE_TOKEN_KEY"

    var userNameAccount: String?
        get() = preferences.getString(SAVE_NAME_USER_KEY, STRING_DEFAULT)
        set(value) = preferences.edit {
            it.putString(SAVE_NAME_USER_KEY, value)
        }

    var passwordAccount: String?
        get() = preferences.getString(SAVE_PASS_WORD_KEY, STRING_DEFAULT)
        set(value) = preferences.edit {
            it.putString(SAVE_PASS_WORD_KEY, value)
        }

    var token: String?
        get() = preferences.getString(SAVE_TOKEN_KEY, STRING_DEFAULT)
        set(value) = preferences.edit {
            it.putString(SAVE_TOKEN_KEY, value)
        }
}
