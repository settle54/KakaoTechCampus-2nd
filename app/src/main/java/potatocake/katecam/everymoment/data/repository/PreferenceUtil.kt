package potatocake.katecam.everymoment.data.repository

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String?): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun setInt(key: String, value: Int?) {
        if (value == null) {
            prefs.edit().remove(key).apply()
        } else {
            prefs.edit().putInt(key, value).apply()
        }
    }

    fun getInt(key: String): Int? {
        if (!prefs.contains(key)) {
            return null
        }
        return prefs.getInt(key, 0)
    }
}