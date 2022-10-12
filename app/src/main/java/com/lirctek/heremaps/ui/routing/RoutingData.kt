package com.lirctek.heremaps.ui.routing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.here.sdk.animation.EasingFunction
import com.here.sdk.core.*
import com.here.sdk.core.errors.InstantiationErrorException
import com.here.sdk.mapview.*
import com.here.sdk.routing.*
import com.here.time.Duration
import com.lirctek.heremaps.R
import com.lirctek.heremaps.models.StopDetail


class RoutingData(val context: Context, val mapView: MapView, val stopDetailsList: List<StopDetail>) {

    lateinit var routingEngine: RoutingEngine
    private val TAG: String = RoutingData::class.java.getName()

    private val mapPolylines: MutableList<MapPolyline> = ArrayList()
    private val mapMarkerList: MutableList<MapMarker> = ArrayList()

    init {
        try {
            routingEngine = RoutingEngine()
        } catch (e: InstantiationErrorException) {
            throw RuntimeException("Initialization of RoutingEngine failed: " + e.error.name)
        }
        addWaypoints()
    }

    fun addWaypoints() {

        val waypoints: MutableList<Waypoint> = ArrayList()
        waypoints.add(Waypoint(createGeoCoOrdinator(47.307323,  -122.228455)))
        for (i in stopDetailsList.indices){
            waypoints.add(Waypoint(createGeoCoOrdinator(stopDetailsList[i].Latitude!!.toDouble(), stopDetailsList[i].Longitude!!.toDouble())))
        }

        routingEngine.calculateRoute(
            waypoints,
            CarOptions()
        ) { routingError, routes ->
            if (routingError == null) {
                val route = routes!![0]
                showRouteOnMap(route)
                animateToRoute(route)
            } else {
//                showDialog("Error while calculating a route:", routingError.toString())
            }
        }
    }

    private fun animateToRoute(route: Route) {
        // The animation should result in an untilted and unrotated map.
        val bearing = 0.0
        val tilt = 0.0
        // We want to show the route fitting in the map view with an additional padding of 100 pixels
        val origin = Point2D(100.0, 100.0)
        val sizeInPixels = Size2D(
            (mapView.width - 100).toDouble(),
            (mapView.height - 100).toDouble()
        )
        val mapViewport = Rectangle2D(origin, sizeInPixels)

        // Animate to the route within a duration of 3 seconds.
        val update = MapCameraUpdateFactory.lookAt(
            route.boundingBox,
            GeoOrientationUpdate(bearing, tilt),
            mapViewport
        )
        val animation = MapCameraAnimationFactory.createAnimation(
            update,
            Duration.ofMillis(3000),
            EasingFunction.IN_CUBIC
        )
        mapView.camera.startAnimation(animation)
    }

    private fun showRouteOnMap(route: Route) {
        // Optionally, clear any previous route.
        clearMap()

        // Show route as polyline.
        val routeGeoPolyline = route.geometry
        val widthInPixels = 20f
        val routeMapPolyline = MapPolyline(
            routeGeoPolyline,
            widthInPixels.toDouble(),
            Color.valueOf(0f, 0.56f, 0.54f, 0.63f)
        ) // RGBA
        mapView.mapScene.addMapPolyline(routeMapPolyline)
        mapPolylines.add(routeMapPolyline)
        for (i in 1 until route.sections.size) {
            val startPoint = route.sections[i].departurePlace.mapMatchedCoordinates
            addCircleMapMarker(startPoint, R.drawable.p_letter)
        }


        val destination = route.sections[route.sections.size - 1].arrivalPlace.mapMatchedCoordinates
        addCircleMapMarker(destination, R.drawable.d_letter)

        // Log maneuver instructions per route section.
        val sections = route.sections
        for (section in sections) {
            logManeuverInstructions(section)
        }
    }

    private fun addCircleMapMarker(geoCoordinates: GeoCoordinates, resourceId: Int) {
        val mapImage = MapImageFactory.fromResource(context.resources, resourceId)
        val anchor2D = Anchor2D(0.5, 1.0)
        val mapMarker = MapMarker(geoCoordinates, mapImage, anchor2D)
        mapView.mapScene.addMapMarker(mapMarker)
        mapMarkerList.add(mapMarker)
    }

    fun clearMap() {
        clearWaypointMapMarker()
        clearRoute()
    }

    private fun clearWaypointMapMarker() {
        for (mapMarker in mapMarkerList) {
            mapView.mapScene.removeMapMarker(mapMarker)
        }
        mapMarkerList.clear()
    }

    private fun clearRoute() {
        for (mapPolyline in mapPolylines) {
            mapView.mapScene.removeMapPolyline(mapPolyline)
        }
        mapPolylines.clear()
    }

    private fun logManeuverInstructions(section: Section) {
        Log.d(TAG, "Log maneuver instructions per route section:")
        val maneuverInstructions = section.maneuvers
        for (maneuverInstruction in maneuverInstructions) {
            val maneuverAction = maneuverInstruction.action
            val maneuverLocation = maneuverInstruction.coordinates
            val maneuverInfo = (maneuverInstruction.text
                    + ", Action: " + maneuverAction.name
                    + ", Location: " + maneuverLocation.toString())
            Log.d(TAG, maneuverInfo)
        }
    }

    private fun createGeoCoOrdinator(latitude: Double, longitude: Double): GeoCoordinates {
        return GeoCoordinates(latitude, longitude)
    }

}