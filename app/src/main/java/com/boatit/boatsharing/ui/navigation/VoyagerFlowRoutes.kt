package com.boatit.boatsharing.ui.navigation

object VoyagerFlowRoutes {
    const val DASHBOARD_VALUE_ARG = "value"
    const val CREATE_SPONSOR_SPLIT_ARG = "splitPayment"

    val dashboardPattern: String = "${AppRoutes.Voyager.DASHBOARD}/{$DASHBOARD_VALUE_ARG}"
    val createVoyageSponsorPattern: String = "${AppRoutes.Voyager.CREATE_VOYAGE_SPONSOR}/{$CREATE_SPONSOR_SPLIT_ARG}"

    fun dashboard(value: String?): String {
        return "${AppRoutes.Voyager.DASHBOARD}/${value ?: "null"}"
    }

    fun createVoyageSponsor(splitPayment: Boolean): String {
        return "${AppRoutes.Voyager.CREATE_VOYAGE_SPONSOR}/$splitPayment"
    }
}
