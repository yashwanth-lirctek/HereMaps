package com.lirctek.heremaps.ui.traffic

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.here.sdk.core.*
import com.here.sdk.core.errors.InstantiationErrorException
import com.here.sdk.gestures.TapListener
import com.here.sdk.mapview.*
import com.here.sdk.mapview.MapViewBase.PickMapContentCallback
import com.here.sdk.traffic.*

class TrafficData(val context: Context, val mapView: MapView) {

    private val TAG: String = TrafficData::class.java.getName()
    private var trafficEngine: TrafficEngine? = null

    // Visualizes traffic incidents found with the TrafficEngine.
    private val mapPolylines: MutableList<MapPolyline> = ArrayList()

    init {
        val camera = mapView.camera
        val distanceInMeters = (1000 * 10).toDouble()
        val mapMeasureZoom = MapMeasure(MapMeasure.Kind.DISTANCE, distanceInMeters)
        camera.lookAt(GeoCoordinates(52.520798, 13.409408), mapMeasureZoom)
        trafficEngine = try {
            TrafficEngine()
        } catch (e: InstantiationErrorException) {
            throw RuntimeException("Initialization of TrafficEngine failed: " + e.error.name)
        }

        // Setting a tap handler to pick and search for traffic incidents around the tapped area.
        setTapGestureHandler()
        enableTrafficVisualization()
    }

    fun enableAll() {
        // Show real-time traffic lines and incidents on the map.
        enableTrafficVisualization()
    }

    fun disableAll() {
        disableTrafficVisualization()
    }

    private fun enableTrafficVisualization() {
        val mapFeatures: MutableMap<String, String> = HashMap()
        // Once these traffic layers are added to the map, they will be automatically updated while panning the map.
        mapFeatures[MapFeatures.TRAFFIC_FLOW] = MapFeatureModes.TRAFFIC_FLOW_WITH_FREE_FLOW
        // MapFeatures.TRAFFIC_INCIDENTS renders traffic icons and lines to indicate the location of incidents.
        mapFeatures[MapFeatures.TRAFFIC_INCIDENTS] = MapFeatureModes.DEFAULT
        mapView.getMapScene().enableFeatures(mapFeatures)
    }

    private fun disableTrafficVisualization() {
        val mapFeatures: MutableList<String> = ArrayList()
        mapFeatures.add(MapFeatures.TRAFFIC_FLOW)
        mapFeatures.add(MapFeatures.TRAFFIC_INCIDENTS)
        mapView.getMapScene().disableFeatures(mapFeatures)

        // This clears only the custom visualization for incidents found with the TrafficEngine.
        clearTrafficIncidentsMapPolylines()
    }

    private fun setTapGestureHandler() {
        mapView.getGestures().setTapListener(TapListener { touchPoint: Point2D ->
            val touchGeoCoords: GeoCoordinates? = mapView.viewToGeoCoordinates(touchPoint)
            // Can be null when the map was tilted and the sky was tapped.
            if (touchGeoCoords != null) {
                // Pick incidents that are shown in MapScene.Layers.TRAFFIC_INCIDENTS.
                pickTrafficIncident(touchPoint)

                // Query for incidents independent of MapScene.Layers.TRAFFIC_INCIDENTS.
                queryForIncidents(touchGeoCoords)
            }
        })
    }

    // Traffic incidents can only be picked, when MapScene.Layers.TRAFFIC_INCIDENTS is visible.
    private fun pickTrafficIncident(touchPointInPixels: Point2D) {
        val originInPixels = Point2D(touchPointInPixels.x, touchPointInPixels.y)
        val sizeInPixels = Size2D(1.0, 1.0)
        val rectangle = Rectangle2D(originInPixels, sizeInPixels)
        mapView.pickMapContent(rectangle,
            PickMapContentCallback { pickMapContentResult ->
                if (pickMapContentResult == null) {
                    // An error occurred while performing the pick operation.
                    return@PickMapContentCallback
                }
                val trafficIncidents = pickMapContentResult.trafficIncidents
                if (trafficIncidents.size == 0) {
                    Log.d(TAG, "No traffic incident found at picked location")
                } else {
                    Log.d(TAG, "Picked at least one incident.")
                    val firstIncident = trafficIncidents[0]

                    // Find more details by looking up the ID via TrafficEngine.
                    findIncidentByID(firstIncident.originalId)
                }

                // Optionally, look for more map content like embedded POIs.
            })
    }

    private fun findIncidentByID(originalId: String) {
        val trafficIncidentsQueryOptions = TrafficIncidentLookupOptions()
        // Optionally, specify a language:
        // the language of the country where the incident occurs is used.
        // trafficIncidentsQueryOptions.languageCode = LanguageCode.EN_US;
        trafficEngine!!.lookupIncident(
            originalId, trafficIncidentsQueryOptions
        ) { trafficQueryError, trafficIncident ->
            if (trafficQueryError == null) {
                Log.d(
                    TAG, "Fetched TrafficIncident from lookup request." +
                            " Description: " + trafficIncident!!.description.text
                )
                addTrafficIncidentsMapPolyline(trafficIncident.location.polyline)
            }
        }
    }

    private fun addTrafficIncidentsMapPolyline(geoPolyline: GeoPolyline) {
        // Show traffic incident as polyline.
        val widthInPixels = 20f
        val routeMapPolyline = MapPolyline(
            geoPolyline,
            widthInPixels.toDouble(),
            Color.valueOf(0f, 0f, 0f, 0.5f)
        ) // RGBA
        mapView.getMapScene().addMapPolyline(routeMapPolyline)
        mapPolylines.add(routeMapPolyline)
    }

    private fun queryForIncidents(centerCoords: GeoCoordinates) {
        val radiusInMeters = 1000
        val geoCircle = GeoCircle(centerCoords, radiusInMeters.toDouble())
        val trafficIncidentsQueryOptions = TrafficIncidentsQueryOptions()
        // Optionally, specify a language:
        // the language of the country where the incident occurs is used.
        // trafficIncidentsQueryOptions.languageCode = LanguageCode.EN_US;
        trafficEngine!!.queryForIncidents(
            geoCircle, trafficIncidentsQueryOptions
        ) { trafficQueryError, trafficIncidentsList ->
            if (trafficQueryError == null) {
                // If error is null, it is guaranteed that the list will not be null.
                var trafficMessage = "Found " + trafficIncidentsList!!.size + " result(s)."
                val nearestIncident = getNearestTrafficIncident(centerCoords, trafficIncidentsList)
                if (nearestIncident != null) {
                    trafficMessage += " Nearest incident: " + nearestIncident.description.text
                }
                Log.d(TAG, "Nearby traffic incidents: $trafficMessage")
                for (trafficIncident in trafficIncidentsList) {
                    Log.d(TAG, "" + trafficIncident.description.text)
                }
            } else {
                Log.d(TAG, "TrafficQueryError: $trafficQueryError")
            }
        }
    }

    private fun getNearestTrafficIncident(
        currentGeoCoords: GeoCoordinates,
        trafficIncidentsList: List<TrafficIncident>?
    ): TrafficIncident? {
        if (trafficIncidentsList!!.size == 0) {
            return null
        }

        // By default, traffic incidents results are not sorted by distance.
        var nearestDistance = Double.MAX_VALUE
        var nearestTrafficIncident: TrafficIncident? = null
        for (trafficIncident in trafficIncidentsList) {
            // In case lengthInMeters == 0 then the polyline consistes of two equal coordinates.
            // It is guaranteed that each incident has a valid polyline.
            for (geoCoords in trafficIncident.location.polyline.vertices) {
                val currentDistance = currentGeoCoords.distanceTo(geoCoords)
                if (currentDistance < nearestDistance) {
                    nearestDistance = currentDistance
                    nearestTrafficIncident = trafficIncident
                }
            }
        }
        return nearestTrafficIncident
    }

    private fun clearTrafficIncidentsMapPolylines() {
        for (mapPolyline in mapPolylines) {
            mapView.getMapScene().removeMapPolyline(mapPolyline)
        }
        mapPolylines.clear()
    }

    private fun showDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.show()
    }
}