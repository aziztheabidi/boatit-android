package com.boatit.boatsharing.utils

import com.boatit.boatsharing.ui.voyager.dashbaord.model.BusinessData
import com.boatit.boatsharing.ui.voyager.dashbaord.model.Place
import com.boatit.boatsharing.ui.voyager.dashbaord.model.Sponser
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyageCategory

object AppConstants {
    var USER_ID: String? = null
    var Voyage_ID: String? = null
    var JWT_TOKEN: String? = null
    var Cates: List<VoyageCategory> = emptyList()
    var Business: BusinessData? = null
    var PLACES: List<Place> = emptyList()
    var BPLACES: List<Place> = emptyList()
    var sponsorList = arrayListOf<Sponser>()
    var Event_Name: String? = "Event"
    var No_Of_Voyagers: Int? = 3
    var Per_Hour_Rate: Double? = 100.0
    var No_of_Hour: Double? = 5.0
    var Estimated_Cost: Double? = 200.0
    var Total_Cost: Double? = 200.0
    var Event_Time: String?= ""
    var Event_Time_End: String?= ""
    var Event_Date: String? = ""
    var Pick_Up_Loc: String? = null
    var Drop_Off_Loc: String? = null
    var Cat_id: Int? = null
    var Travel_Now:Boolean? = false
    var Stay_on_water:Boolean? = false

}