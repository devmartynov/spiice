package io.devmartynov.spiice.ui.feature

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.devmartynov.spiice.repository.user.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    fun setHasOnboardingFinish(hasFinished: Boolean) {
        userPreferencesRepository.hasOnboardingFinished = hasFinished
    }
}