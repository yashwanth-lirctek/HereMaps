package com.lirctek.heremaps.models

class TripLoad {

    var WOId: Int = 0
    var Id: Int = 0
    var RelayId: Int = 0
    var DispatchStatus: Int = 0
    var StartingLocation_Id: Int = 0
    var EndingLocation_Id: Int = 0
    var DispatchOrder_Id: Int = 0
    var IsBOLReceived: Int = 0
    var IsBOLRequired: Int = 0
    var status: Int = 0

    var Temperature: String? = null

    var IsAccepted: Int = 0
    var IsRejected: Int = 0
    var RoleTypeId: Int = 0
    var DeadHeadedBefore: Double = 0.toDouble()
    var DeadHeadedAfter: Double = 0.toDouble()
    var LoadedMiles: Double = 0.toDouble()
    var TotalAmount: Double = 0.toDouble()
    var Notes: String? = null
    var DispatchNumber: String? = null
    var CarrierName: String? = null
    var TruckNumber: String? = null
    var TrailerNumber: String? = null
    var Driver1Name: String? = null
    var Driver2Name: String? = null
    var StartingLocation: String? = null
    var EndingLocation: String? = null
    var StartingAddress1: String? = null
    var StartingAddress2: String? = null
    var StartingCity: String? = null
    var StartingState: String? = null
    var StartingZip: String? = null
    var EndingAddress1: String? = null
    var EndingAddress2: String? = null
    var EndingCity: String? = null
    var EndingState: String? = null
    var EndingZip: String? = null
    var LoadType: String? = null
    var DriverViewedOn: String? = null
    var Driver2ViewedOn:String? = null
    var Driver1_Id:Int? = null
    var Driver2_Id:Int? = null
    var PickupDate: String? = null
    var PickupLocation: String? = null
    var DeliveryDate: String? = null
    var DeliveryLocation: String? = null
    var Type: String? = null
    var BolReceivedDate: String? = null
    var Trailer_Id: String? = null
    var Truck_Id: String? = null
    var AcceptedDate: String? = null
    var RejectedDate: String? = null
    var RejectReason: String? = null
    var ExpectedRate: Int = 0
    var RoleType: String? = null

    // Start and End Trip fields
    var StartedLat: String? = null
    var StartedLong: String? = null
    var StartedOdo: Int = 0
    var StartedOn: String? = null
    var EndedOn: String? = null
    var EndedLat: String? = null
    var EndedLong: String? = null
    var EndedOdo: Int = 0
    var AStartedLat: String? = null
    var AStartedLong: String? = null
    var AStartedOdo: Int = 0
    var AStartedOn: String? = null
    var AEndedOn: String? = null
    var AEndedLat: String? = null
    var AEndedLong: String? = null
    var AEndedOdo: Int = 0

    var UpdatedFields: MutableList<String> = ArrayList<String>()

    var StopDetails = ArrayList<StopDetail>()

    var AcceptRejectLog: MutableList<AcceptRejectLog> = ArrayList<AcceptRejectLog>()

    var DispatchFee: Double? = null

    var RateDetails = ArrayList<RateDetail>()

    var lastLocation: String? = null

}