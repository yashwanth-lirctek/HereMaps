package com.lirctek.heremaps.ui.gestures

import android.content.Context
import android.util.Log
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.gestures.*
import com.here.sdk.mapview.MapMeasure
import com.here.sdk.mapview.MapView

class GesturesData(val context: Context, val mapView: MapView) {

    private val TAG: String = GesturesData::class.java.simpleName

    private var gestureMapAnimator: GestureMapAnimator? = null

    init {
        val camera = mapView.camera
        val distanceInMeters = (1000 * 10).toDouble()
        val mapMeasureZoom = MapMeasure(MapMeasure.Kind.DISTANCE, distanceInMeters)
        camera.lookAt(GeoCoordinates(52.520798, 13.409408), mapMeasureZoom)
        gestureMapAnimator = GestureMapAnimator(mapView.camera)
        setTapGestureHandler(mapView)
        setDoubleTapGestureHandler(mapView)
        setTwoFingerTapGestureHandler(mapView)
        setLongPressGestureHandler(mapView)

        // Disable the default map gesture behavior for DoubleTap (zooms in) and TwoFingerTap (zooms out)
        // as we want to enable custom map animations when such gestures are detected.
        mapView.gestures.disableDefaultAction(GestureType.DOUBLE_TAP)
        mapView.gestures.disableDefaultAction(GestureType.TWO_FINGER_TAP)
    }

    private fun setTapGestureHandler(mapView: MapView) {
        mapView.gestures.tapListener = TapListener { touchPoint ->
            val geoCoordinates = mapView.viewToGeoCoordinates(touchPoint)
            Log.d(TAG, "Tap at: $geoCoordinates")
        }
    }

    private fun setDoubleTapGestureHandler(mapView: MapView) {
        mapView.gestures.doubleTapListener = DoubleTapListener { touchPoint ->
            val geoCoordinates = mapView.viewToGeoCoordinates(touchPoint)
            Log.d(TAG, "Default zooming in is disabled. DoubleTap at: $geoCoordinates")

            // Start our custom zoom in animation.
            gestureMapAnimator!!.zoomIn(touchPoint)
        }
    }

    private fun setTwoFingerTapGestureHandler(mapView: MapView) {
        mapView.gestures.twoFingerTapListener =
            TwoFingerTapListener { touchCenterPoint ->
                val geoCoordinates = mapView.viewToGeoCoordinates(touchCenterPoint)
                Log.d(
                    TAG,
                    "Default zooming in is disabled. TwoFingerTap at: $geoCoordinates"
                )

                // Start our custom zoom out animation.
                gestureMapAnimator!!.zoomOut(touchCenterPoint)
            }
    }

    private fun setLongPressGestureHandler(mapView: MapView) {
        mapView.gestures.longPressListener =
            LongPressListener { gestureState, touchPoint ->
                val geoCoordinates = mapView.viewToGeoCoordinates(touchPoint)
                if (gestureState == GestureState.BEGIN) {
                    Log.d(TAG, "LongPress detected at: $geoCoordinates")
                }
                if (gestureState == GestureState.UPDATE) {
                    Log.d(TAG, "LongPress update at: $geoCoordinates")
                }
                if (gestureState == GestureState.END) {
                    Log.d(TAG, "LongPress finger lifted at: $geoCoordinates")
                }
                if (gestureState == GestureState.CANCEL) {
                    Log.d(
                        TAG,
                        "Map view lost focus. Maybe a modal dialog is shown or the app is sent to background."
                    )
                }
            }
    }

    // This is just an example how to clean up.
    private fun removeGestureHandler(mapView: MapView) {
        // Stop listening.
        mapView.gestures.tapListener = null
        mapView.gestures.doubleTapListener = null
        mapView.gestures.twoFingerTapListener = null
        mapView.gestures.longPressListener = null

        // Bring back the default map gesture behavior for DoubleTap (zooms in)
        // and TwoFingerTap (zooms out). These actions were disabled above.
        mapView.gestures.enableDefaultAction(GestureType.DOUBLE_TAP)
        mapView.gestures.enableDefaultAction(GestureType.TWO_FINGER_TAP)
    }
}