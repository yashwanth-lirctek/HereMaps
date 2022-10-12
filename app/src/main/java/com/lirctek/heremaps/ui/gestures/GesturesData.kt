package com.lirctek.heremaps.ui.gestures

import android.util.Log
import com.here.sdk.core.Point2D
import com.here.sdk.gestures.*
import com.here.sdk.mapview.MapView
import com.here.sdk.mapview.MapViewBase.PickMapItemsCallback


class GesturesData(val mapView: MapView) {

    private val TAG: String = GesturesData::class.java.simpleName

    private var gestureMapAnimator: GestureMapAnimator? = null

    init {
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
            pickMapMarker(touchPoint)
        }
    }

    private fun pickMapMarker(touchPoint: Point2D) {
        val radiusInPixel = 2f
        mapView.pickMapItems(touchPoint, radiusInPixel.toDouble(),
            PickMapItemsCallback { pickMapItemsResult ->
                if (pickMapItemsResult == null) {
                    // An error occurred while performing the pick operation.
                    return@PickMapItemsCallback
                }
                val mapMarkerList = pickMapItemsResult.markers
                if (mapMarkerList.size == 0) {
                    return@PickMapItemsCallback
                }
                val topmostMapMarker = mapMarkerList[0]
                Log.d(TAG,
                    "Map marker picked: Location: " +
                            topmostMapMarker.coordinates.latitude + ", " +
                            topmostMapMarker.coordinates.longitude)
            })
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