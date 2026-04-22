package com.boatit.boatsharing.data.local.prefmanager

interface ICaptainStatusProvider {
    fun setCaptainStatus(isOnline: Boolean)

    fun isCaptainOnline(): Boolean
}
