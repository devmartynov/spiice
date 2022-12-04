package io.devmartynov.spiice.repository.user

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : UserPreferencesRepository {
    private val userSettings = context
        .getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE)

    override var token: String?
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

    override val fullName: String
        get() = firstName + " " + lastName

    override val userId: UUID
        get() = UUID.fromString(userSettings.getString(USER_ID, ""))

    override fun setUserInfo(
        token: String,
        email: String,
        firstName: String,
        lastName: String,
        id: UUID
    ) {
        userSettings.edit()
            .putString(USER_TOKEN, token)
            .putString(USER_EMAIL, email)
            .putString(USER_FIRST_NAME, firstName)
            .putString(USER_LAST_NAME, lastName)
            .putString(USER_ID, id.toString())
            .apply()
    }

    override fun isAuthorized(): Boolean {
        return token != null
    }

    companion object {
        private const val USER_SETTINGS = "user_settings"
        const val USER_TOKEN = "user_token"
        const val USER_EMAIL = "user_email"
        const val USER_FIRST_NAME = "user_first_name"
        const val USER_LAST_NAME = "user_last_name"
        const val USER_ID = "user_id"
    }
}