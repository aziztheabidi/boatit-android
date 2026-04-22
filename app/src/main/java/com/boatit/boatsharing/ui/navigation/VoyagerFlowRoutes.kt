package com.boatit.boatsharing.ui.navigation

object VoyagerFlowRoutes {
    const val DASHBOARD_VALUE_ARG = "value"
    const val CREATE_SPONSOR_SPLIT_ARG = "splitPayment"
    @Deprecated("Use CREATE_SPONSOR_SPLIT_ARG for clarity")
    const val SPONSOR_SPLIT_ARG = CREATE_SPONSOR_SPLIT_ARG

    val dashboardPattern: String = "${NavigationManager.DASHBOARD_SCREEN}/{$DASHBOARD_VALUE_ARG}"
    val createVoyageSponsorPattern: String = "${NavigationManager.CREATE_VOYAGE_SPONSOR_SCREEN}/{$CREATE_SPONSOR_SPLIT_ARG}"

    fun dashboard(value: String?): String {
        return "${NavigationManager.DASHBOARD_SCREEN}/${value ?: "null"}"
    }

    fun createVoyageSponsor(splitPayment: Boolean): String {
        return "${NavigationManager.CREATE_VOYAGE_SPONSOR_SCREEN}/$splitPayment"
    }
}
