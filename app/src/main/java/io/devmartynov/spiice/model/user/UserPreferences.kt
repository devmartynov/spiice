package io.devmartynov.spiice.model.user

import android.content.Context
import io.devmartynov.spiice.ui.auth.AuthState

class UserPreferences private constructor(context: Context) : AuthState {
    private val tokenSetting = context
        .applicationContext
        .getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE)

    var token
        get() = tokenSetting.getString(USER_TOKEN, "")
        set(token) {
            tokenSetting.edit()
                .putString(USER_TOKEN, token)
                .apply()
        }

    companion object {
        const val USER_TOKEN = "token"

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