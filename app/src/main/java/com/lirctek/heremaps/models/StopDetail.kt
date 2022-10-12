package com.lirctek.heremaps.models

import java.util.ArrayList

class StopDetail {

    var WorkOrderId: Int = 0
    var WOStop_Id: Int = 0
    var Id: Int = 0
    var StopNumber: String? = null
    var StopType: String? = null
    var FromDate: String? = null
    var FromTime: String? = null
    var Address1: String? = null
    var DosStopNumber: String? = null
    var Address2: String? = null
    var PONumber: String? = null
    var AppNumber: String? = null
    var SealNumber: String? = null
    var ToDate: String? = null
    var ToTime: String? = null
    var FacilityName: String? = null
    var City: String? = null
    var State: String? = null
    var Zip: String? = null
    var ArrivedDate: String? = null
    var LoadedORUnLoadedDate: String? = null
    var PhoneNo: String? = null
    var PhoneExt: String? = null
    var Notes: String? = null
    var DelayReason: String? = null
    var ETA: String? = null


    var CheckInTime:String? = null
    var CheckInLatitude:String? = null
    var CheckInLongitude:String? = null
    var CheckOutTime:String? = null
    var CheckOutLatitude:String? = null
    var CheckOutLongitude:String? = null
    var TrailerTemperature: String? = null
    var TemperatureDateTime:String? = null
    var TemperatureDoc:String? = null
    var ScaleWeight:String? = null
    var ScaleDateTime:String? = null
    var ScaleDoc:String? = null
    var BOLNumPages:String? = null
    var BOLDateTime:String? = null
    var BOLDoc:String? = null
    var SealNum:String? = null
    var SealDateTime:String? = null
    var SealDoc:String? = null

    var TemperatureDocLocalPath:String? = null
    var ScaleDocLocalPath:String? = null
    var BOLDocLocalPath:String? = null
    var SealDocLocalPath:String? = null

    var ContainerId: String? = null
    var ContainerName: String? = "wostop"
    var Url: String? = null

    var UpdatedFields: ArrayList<String> = ArrayList()

    var StopItems: MutableList<StopItem> = ArrayList<StopItem>()
    var Latitude:String? = null
    var Longitude:String? = null

    //Updated Fields
    var ArrivedLat:String?=null
    var ArrivedLong:String?=null
    var ArrivedOdo: String?=null
    var LeftDate:String?=null
    var LeftLat:String?=null
    var LeftLong:String?=null
    var LeftOdo: String?=null
    var ALeftDate:String?=null
    var ALeftLat:String?=null
    var ALeftLong:String?=null
    var ALeftOdo: String?=null
    var AArrivedLat:String?=null
    var AArrivedLong:String?=null
    var AArrivedOdo: String?=null
    var AArrivedDate:String?=null

    var isExpended: Boolean = false

    var isChecked: Boolean = false

}