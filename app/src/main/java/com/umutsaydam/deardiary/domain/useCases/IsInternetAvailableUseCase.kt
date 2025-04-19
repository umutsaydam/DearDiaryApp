package com.umutsaydam.deardiary.domain.useCases

import com.umutsaydam.deardiary.domain.ConnectivityObserver
import com.umutsaydam.deardiary.domain.enums.NetworkStatusEnum
import javax.inject.Inject

class IsInternetAvailableUseCase @Inject constructor(
    private val connectivityObserver: ConnectivityObserver
) {
    operator fun invoke(): Boolean {
        return connectivityObserver.getCurrentStatus() == NetworkStatusEnum.Available
    }
}