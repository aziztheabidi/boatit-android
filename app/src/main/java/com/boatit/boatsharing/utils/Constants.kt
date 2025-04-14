package com.boatit.boatsharing.utils

import com.boatit.boatsharing.ui.voyager.dashbaord.model.Place
import com.boatit.boatsharing.ui.voyager.dashbaord.model.Sponser

object AppConstants {
    var USER_ID: String? = null
    var Voyage_ID: String? = null
    var JWT_TOKEN: String? = null
    var PLACES: List<Place> = emptyList()
    var sponsorList = arrayListOf<Sponser>()
    var Event_Name: String? = "Event"
    var No_Of_Voyagers: Int? = 3
    var Per_Hour_Rate: Int? = 100
    var Estimated_Cost: Int? = 200
    var Event_Time: String?= null
    var Pick_Up_Loc: String? = null
    var Drop_Off_Loc: String? = null
    var Travel_Now:Boolean? = null

}