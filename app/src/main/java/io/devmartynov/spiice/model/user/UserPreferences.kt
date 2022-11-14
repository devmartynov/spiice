package io.devmartynov.spiice.model.user

import android.content.Context
import io.devmartynov.spiice.ui.auth.AuthState
import java.util.UUID

class UserPreferences private constructor(context: Context) : AuthState {
    private val userSettings = context
        .applicationContext
        .getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE)

    var token: String?
        get() = userSettings.getString(USER_TOKEN, null)
        set(token) {
            userSettings.edit()
                .putString(USER_TOKEN, token)
                .apply()
        }

    private val firstName: String?
        get() = userSettings.getString(USER_FIRST_NAME, "")

    private val lastName: String?
        get() = userSettings.getString(USER_LAST_NAME, "")

    val fullName: String
        get() = firstName + " " + lastName

    val userId: UUID
        get() = UUID.fromString(userSettings.getString(USER_ID, ""))

    fun setUserInfo(token: String, email: String, firstName: String, lastName: String, id: UUID) {
        userSettings.edit()
            .putString(USER_TOKEN, token)
            .putString(USER_EMAIL, email)
            .putString(USER_FIRST_NAME, firstName)
            .putString(USER_LAST_NAME, lastName)
            .putString(USER_ID, id.toString())
            .apply()
    }

    companion object {
        const val USER_SETTINGS = "user_settings"
        const val USER_TOKEN = "user_token"
        const val USER_EMAIL = "user_email"
        const val USER_FIRST_NAME = "user_first_name"
        const val USER_LAST_NAME = "user_last_name"
        const val USER_ID = "user_id"

        private var INSTANCE: UserPreferences? = null

        fun get(): UserPreferences {
            return INSTANCE ?: throw IllegalStateException("UserPreferences must be initialized")
        }

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = UserPreferences(context)
            }
        }
    }

    override fun isAuthorized(): Boolean {
        return token != null
    }
}