package com.lirctek.heremaps.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.mapview.MapMeasure
import com.here.sdk.mapview.MapScheme
import com.lirctek.heremaps.HereMaps
import com.lirctek.heremaps.databinding.ActivityMapsBinding
import com.lirctek.heremaps.models.TripLoad
import com.lirctek.heremaps.ui.gestures.GesturesData
import com.lirctek.heremaps.ui.routing.RoutingData
import com.lirctek.heremaps.ui.traffic.TrafficData


class MapsActivity : AppCompatActivity() {

    lateinit var mViewBinding: ActivityMapsBinding
    lateinit var tripLoad: TripLoad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        mViewBinding.mMapView.onCreate(savedInstanceState)

        loadSampleData()
        initToolBar()

        loadMapScene()
    }

    @SuppressLint("SetTextI18n")
    private fun initToolBar() {

        (tripLoad.StopDetails[0].City)?.let {
            mViewBinding.mPickupLocation.text = it.substring(0, it.length.coerceAtMost(10))+
                    ", "+tripLoad.StopDetails[0].State
        }

        (tripLoad.StopDetails[tripLoad.StopDetails.size - 1].City)?.let {
            mViewBinding.mDeliveryLocation.text = it.substring(0, it.length.coerceAtMost(10))+
                    ", "+tripLoad.StopDetails[tripLoad.StopDetails.size - 1].State
        }

    }

    private fun loadSampleData() {
        val jsonString = "{\n" +
                "   \"AEndedLat\":\"\",\n" +
                "   \"AEndedLong\":\"\",\n" +
                "   \"AEndedOdo\":0,\n" +
                "   \"AEndedOn\":\"\",\n" +
                "   \"AStartedLat\":\"\",\n" +
                "   \"AStartedLong\":\"\",\n" +
                "   \"AStartedOdo\":0,\n" +
                "   \"AStartedOn\":\"\",\n" +
                "   \"AcceptRejectLog\":[\n" +
                "      {\n" +
                "         \"ExpectedRate\":0,\n" +
                "         \"LogDateTime\":\"2022-06-24 22:43:03\",\n" +
                "         \"Reason\":\"\",\n" +
                "         \"Status\":\"Accepted\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"ExpectedRate\":0,\n" +
                "         \"LogDateTime\":\"2022-06-24 22:43:36\",\n" +
                "         \"Reason\":\"nxnxnc\",\n" +
                "         \"Status\":\"Rejected\"\n" +
                "      }\n" +
                "   ],\n" +
                "   \"AcceptedDate\":\"2022-10-12 14:13:07\",\n" +
                "   \"DeadHeadedAfter\":0.0,\n" +
                "   \"DeadHeadedBefore\":0.0,\n" +
                "   \"DeliveryDate\":\"2020-07-31 08:30:00\",\n" +
                "   \"DeliveryLocation\":\"KENT, WA\",\n" +
                "   \"DispatchNumber\":\"7265\",\n" +
                "   \"DispatchOrder_Id\":628094,\n" +
                "   \"DispatchStatus\":2,\n" +
                "   \"Driver1Name\":\"Black Tiger\",\n" +
                "   \"Driver1_Id\":16141,\n" +
                "   \"EndedOdo\":0,\n" +
                "   \"EndingLocation_Id\":0,\n" +
                "   \"ExpectedRate\":0,\n" +
                "   \"Id\":628094,\n" +
                "   \"IsAccepted\":1,\n" +
                "   \"IsBOLReceived\":0,\n" +
                "   \"IsBOLRequired\":0,\n" +
                "   \"IsRejected\":0,\n" +
                "   \"LoadType\":\"Full Load\",\n" +
                "   \"LoadedMiles\":742.44,\n" +
                "   \"PickupDate\":\"2020-07-29 08:00:00\",\n" +
                "   \"PickupLocation\":\"SACRAMENTO, CA\",\n" +
                "   \"RateDetails\":[\n" +
                "      \n" +
                "   ],\n" +
                "   \"RejectReason\":\"\",\n" +
                "   \"RejectedDate\":\"\",\n" +
                "   \"RelayId\":0,\n" +
                "   \"RoleTypeId\":0,\n" +
                "   \"StartedOdo\":0,\n" +
                "   \"StartingLocation_Id\":0,\n" +
                "   \"StopDetails\":[\n" +
                "      {\n" +
                "         \"AArrivedDate\":\"\",\n" +
                "         \"AArrivedLong\":\"\",\n" +
                "         \"AArrivedOdo\":\"\",\n" +
                "         \"ALeftDate\":\"\",\n" +
                "         \"ALeftLat\":\"\",\n" +
                "         \"ALeftLong\":\"\",\n" +
                "         \"ALeftOdo\":\"\",\n" +
                "         \"Address1\":\"2670 Land Ave\",\n" +
                "         \"Address2\":\"\",\n" +
                "         \"AppNumber\":\"\",\n" +
                "         \"ArrivedDate\":\"\",\n" +
                "         \"ArrivedLat\":\"\",\n" +
                "         \"ArrivedLong\":\"\",\n" +
                "         \"ArrivedOdo\":\"\",\n" +
                "         \"BOLDateTime\":\"\",\n" +
                "         \"BOLDoc\":\"\",\n" +
                "         \"BOLNumPages\":\"\",\n" +
                "         \"CheckInLatitude\":\"\",\n" +
                "         \"CheckInLongitude\":\"\",\n" +
                "         \"CheckInTime\":\"\",\n" +
                "         \"CheckOutLatitude\":\"\",\n" +
                "         \"CheckOutLongitude\":\"\",\n" +
                "         \"CheckOutTime\":\"\",\n" +
                "         \"City\":\"SACRAMENTO\",\n" +
                "         \"ContainerName\":\"wostop\",\n" +
                "         \"DelayReason\":\"\",\n" +
                "         \"ETA\":\"\",\n" +
                "         \"FacilityName\":\"DPSG- Sacramento\",\n" +
                "         \"FromDate\":\"2020-07-29 08:00:00\",\n" +
                "         \"FromTime\":\"2020-07-29 08:00:00\",\n" +
                "         \"Id\":1654481,\n" +
                "         \"Latitude\":\"38.61612\",\n" +
                "         \"LeftDate\":\"\",\n" +
                "         \"LeftLat\":\"\",\n" +
                "         \"LeftLong\":\"\",\n" +
                "         \"LeftOdo\":\"\",\n" +
                "         \"LoadedORUnLoadedDate\":\"\",\n" +
                "         \"Longitude\":\"-121.43375\",\n" +
                "         \"Notes\":\"*critical production load must deliver on time, and have daily tracking*\",\n" +
                "         \"PONumber\":\"SC072920HM2\",\n" +
                "         \"PhoneExt\":\"\",\n" +
                "         \"PhoneNo\":\"(916) 564-7743\",\n" +
                "         \"ScaleDateTime\":\"\",\n" +
                "         \"ScaleDoc\":\"\",\n" +
                "         \"ScaleWeight\":\"\",\n" +
                "         \"SealDateTime\":\"\",\n" +
                "         \"SealDoc\":\"\",\n" +
                "         \"SealNum\":\"\",\n" +
                "         \"SealNumber\":\"\",\n" +
                "         \"State\":\"CA\",\n" +
                "         \"StopItems\":[\n" +
                "            {\n" +
                "               \"CONumber\":\"\",\n" +
                "               \"Commodity\":\"Zevia Cola\",\n" +
                "               \"Id\":377844,\n" +
                "               \"ItemNumber\":\"\",\n" +
                "               \"Length\":0,\n" +
                "               \"PONumber\":\"SC072920HM2\",\n" +
                "               \"Pallets\":20,\n" +
                "               \"PieceCount\":2400,\n" +
                "               \"UpdatedFields\":[\n" +
                "                  \n" +
                "               ],\n" +
                "               \"Weight\":\"41800\"\n" +
                "            }\n" +
                "         ],\n" +
                "         \"StopNumber\":\"1\",\n" +
                "         \"StopType\":\"Pickup\",\n" +
                "         \"TemperatureDateTime\":\"\",\n" +
                "         \"TemperatureDoc\":\"\",\n" +
                "         \"ToDate\":\"\",\n" +
                "         \"ToTime\":\"\",\n" +
                "         \"TrailerTemperature\":\"\",\n" +
                "         \"UpdatedFields\":[\n" +
                "            \n" +
                "         ],\n" +
                "         \"WOStop_Id\":2008410,\n" +
                "         \"WorkOrderId\":811230,\n" +
                "         \"Zip\":\"95815-2380\",\n" +
                "         \"isChecked\":false,\n" +
                "         \"isExpended\":false\n" +
                "      },\n" +
                "      {\n" +
                "         \"AArrivedDate\":\"\",\n" +
                "         \"AArrivedLong\":\"\",\n" +
                "         \"AArrivedOdo\":\"\",\n" +
                "         \"ALeftDate\":\"\",\n" +
                "         \"ALeftLat\":\"\",\n" +
                "         \"ALeftLong\":\"\",\n" +
                "         \"ALeftOdo\":\"\",\n" +
                "         \"Address1\":\"22430 76TH AVE S\",\n" +
                "         \"Address2\":\"\",\n" +
                "         \"AppNumber\":\"\",\n" +
                "         \"ArrivedDate\":\"\",\n" +
                "         \"ArrivedLat\":\"\",\n" +
                "         \"ArrivedLong\":\"\",\n" +
                "         \"ArrivedOdo\":\"\",\n" +
                "         \"BOLDateTime\":\"\",\n" +
                "         \"BOLDoc\":\"\",\n" +
                "         \"BOLNumPages\":\"\",\n" +
                "         \"CheckInLatitude\":\"\",\n" +
                "         \"CheckInLongitude\":\"\",\n" +
                "         \"CheckInTime\":\"\",\n" +
                "         \"CheckOutLatitude\":\"\",\n" +
                "         \"CheckOutLongitude\":\"\",\n" +
                "         \"CheckOutTime\":\"\",\n" +
                "         \"City\":\"KENT\",\n" +
                "         \"ContainerName\":\"wostop\",\n" +
                "         \"DelayReason\":\"\",\n" +
                "         \"ETA\":\"\",\n" +
                "         \"FacilityName\":\"Zevia C/O - Holman Distribution\",\n" +
                "         \"FromDate\":\"2020-07-31 08:30:00\",\n" +
                "         \"FromTime\":\"2020-07-31 08:30:00\",\n" +
                "         \"Id\":1654482,\n" +
                "         \"Latitude\":\"47.40109\",\n" +
                "         \"LeftDate\":\"\",\n" +
                "         \"LeftLat\":\"\",\n" +
                "         \"LeftLong\":\"\",\n" +
                "         \"LeftOdo\":\"\",\n" +
                "         \"LoadedORUnLoadedDate\":\"\",\n" +
                "         \"Longitude\":\"-122.23727\",\n" +
                "         \"Notes\":\"*critical production load must deliver on time, and have daily tracking*\",\n" +
                "         \"PONumber\":\"SC072920HM2\",\n" +
                "         \"PhoneExt\":\"\",\n" +
                "         \"PhoneNo\":\"(253) 872-7140\",\n" +
                "         \"ScaleDateTime\":\"\",\n" +
                "         \"ScaleDoc\":\"\",\n" +
                "         \"ScaleWeight\":\"\",\n" +
                "         \"SealDateTime\":\"\",\n" +
                "         \"SealDoc\":\"\",\n" +
                "         \"SealNum\":\"\",\n" +
                "         \"SealNumber\":\"\",\n" +
                "         \"State\":\"WA\",\n" +
                "         \"StopItems\":[\n" +
                "            {\n" +
                "               \"CONumber\":\"\",\n" +
                "               \"Commodity\":\"Zevia Cola\",\n" +
                "               \"Id\":377845,\n" +
                "               \"ItemNumber\":\"\",\n" +
                "               \"Length\":0,\n" +
                "               \"PONumber\":\"SC072920HM2\",\n" +
                "               \"Pallets\":20,\n" +
                "               \"PieceCount\":2400,\n" +
                "               \"UpdatedFields\":[\n" +
                "                  \n" +
                "               ],\n" +
                "               \"Weight\":\"41800\"\n" +
                "            }\n" +
                "         ],\n" +
                "         \"StopNumber\":\"2\",\n" +
                "         \"StopType\":\"Delivery\",\n" +
                "         \"TemperatureDateTime\":\"\",\n" +
                "         \"TemperatureDoc\":\"\",\n" +
                "         \"ToDate\":\"\",\n" +
                "         \"ToTime\":\"\",\n" +
                "         \"TrailerTemperature\":\"\",\n" +
                "         \"UpdatedFields\":[\n" +
                "            \n" +
                "         ],\n" +
                "         \"WOStop_Id\":2008411,\n" +
                "         \"WorkOrderId\":811230,\n" +
                "         \"Zip\":\"98032-2406\",\n" +
                "         \"isChecked\":false,\n" +
                "         \"isExpended\":false\n" +
                "      }\n" +
                "   ],\n" +
                "   \"TotalAmount\":2250.0,\n" +
                "   \"TruckNumber\":\"TRK 001\",\n" +
                "   \"Truck_Id\":\"10742\",\n" +
                "   \"Type\":\"Loads\",\n" +
                "   \"UpdatedFields\":[\n" +
                "      \n" +
                "   ],\n" +
                "   \"WOId\":811230,\n" +
                "   \"status\":0\n" +
                "}"

        val gson = Gson()
        tripLoad = gson.fromJson(jsonString, TripLoad::class.java)
        Log.e("TRIPLOAD", Gson().toJson(tripLoad))
    }

    private fun loadMapScene() {
        // Load a scene from the HERE SDK to render the map with a map scheme.
        mViewBinding.mMapView.mapScene.loadScene(MapScheme.NORMAL_DAY) { mapError ->
            if (mapError == null) {
                loadCurrentPosition()
                TrafficData(this, mViewBinding.mMapView)
                RoutingData(this, mViewBinding.mMapView, tripLoad.StopDetails)
                GesturesData(mViewBinding.mMapView)
            } else {
                Log.d("loadMapScene()", "Loading map failed: mapError: " + mapError.name)
            }
        }
    }

    private fun loadCurrentPosition() {

        val camera = mViewBinding.mMapView.camera
        val distanceInMeters = (1000 * 10).toDouble()
        val mapMeasureZoom = MapMeasure(MapMeasure.Kind.DISTANCE, distanceInMeters)
        camera.lookAt(GeoCoordinates(47.307323,  -122.228455), mapMeasureZoom)

    }

    override fun onPause() {
        mViewBinding.mMapView.onPause()
        super.onPause()
    }

    override fun onResume() {
        mViewBinding.mMapView.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mViewBinding.mMapView.onDestroy()
        HereMaps.app.disposeHERESDK()
        super.onDestroy()
    }

    var twice = false
    override fun onBackPressed() {
        if (twice) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
            System.exit(0)
        }
        twice = true
        Toast.makeText(
            this,
            "Press Back Again to Leave",
            Toast.LENGTH_SHORT
        ).show()
        Handler().postDelayed({ twice = false }, 3000)
    }
}