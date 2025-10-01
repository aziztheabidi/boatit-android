package com.boatit.boatsharing.utils

import com.boatit.boatsharing.ui.voyager.dashboard.model.BusinessData
import com.boatit.boatsharing.ui.voyager.dashboard.model.Place
import com.boatit.boatsharing.ui.voyager.dashboard.model.Sponser
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyageCategory

object AppConstants {
    var IMG_PATH: String? = "https://testbyfarhan.squarecod.com/"
    var USER_ID: String? = null
    var USER_NAME: String? = null
    var Voyage_ID: String? = null
    var JWT_TOKEN: String? = null
    var Cates: List<VoyageCategory> = emptyList()
    val hourList = List(24) { hour -> String.format("%02d:00:00", hour) }
    var Business: BusinessData? = null
    var Business_Status: Boolean? = false
    var BusinessDock:Boolean? = false
    var BusinessDockType:String? = "Pick"
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
    var Event_Time_End: String?= "15:00:00"
    var Event_Date: String? = ""
    var Pick_Up_Loc: Pair<Int, String>? = null
    var Drop_Off_Loc: Pair<Int, String>? = null
    var Cat_id: Int? = null
    var Travel_Now:Boolean? = false
    var Split:Boolean? = false
    var Stay_on_water:Boolean? = false
    var Busines_Location: String?= ""
    var Busines_DOCK: Boolean? = false
    var Busines_City: String?= ""
    var Busines_Zip: String?= ""
    var Busines_State: String?= ""
    var Busines_Lat: Double?= 0.0
    var Busines_Lont: Double?= 0.0

    fun resetDefaults() {
        IMG_PATH = "https://testbyfarhan.squarecod.com/"
        Voyage_ID = null
        JWT_TOKEN = null
        Cates = emptyList()
        Business = null
        BusinessDock = false
        BusinessDockType = "Pick"
        PLACES = emptyList()
        BPLACES = emptyList()
        sponsorList = arrayListOf()
        Event_Name = "Event"
        No_Of_Voyagers = 3
        Per_Hour_Rate = 100.0
        No_of_Hour = 5.0
        Estimated_Cost = 200.0
        Total_Cost = 200.0
        Event_Time = ""
        Event_Time_End = "15:00:00"
        Event_Date = ""
        Pick_Up_Loc = null
        Drop_Off_Loc = null
        Cat_id = null
        Travel_Now = false
        Split = false
        Stay_on_water = false
        Busines_Location = ""
        Busines_DOCK = false
        Busines_City = ""
        Busines_Zip = ""
        Busines_State = ""
        Busines_Lat = 0.0
        Busines_Lont = 0.0
    }
}