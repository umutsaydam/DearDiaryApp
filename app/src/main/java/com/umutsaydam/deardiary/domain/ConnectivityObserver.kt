package com.umutsaydam.deardiary.domain

import com.umutsaydam.deardiary.domain.enums.NetworkStatusEnum

interface ConnectivityObserver {
    fun getCurrentStatus(): NetworkStatusEnum


}