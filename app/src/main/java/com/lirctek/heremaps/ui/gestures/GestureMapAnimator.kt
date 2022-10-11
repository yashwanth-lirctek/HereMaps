package com.lirctek.heremaps.ui.gestures

import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import com.here.sdk.core.Point2D
import com.here.sdk.mapview.MapCamera

class GestureMapAnimator(val camera: MapCamera) {

    private var zoomValueAnimator: ValueAnimator? = null
    var zoomOrigin: Point2D? = null

    // Starts the zoom in animation.
    fun zoomIn(touchPoint: Point2D?) {
        zoomOrigin = touchPoint
        startZoomAnimation(true)
    }

    // Starts the zoom out animation.
    fun zoomOut(touchPoint: Point2D?) {
        zoomOrigin = touchPoint
        startZoomAnimation(false)
    }

    private fun startZoomAnimation(zoomIn: Boolean) {
        stopAnimations()

        // A new Animator that zooms the map.
        zoomValueAnimator = createZoomValueAnimator(zoomIn)

        // Start the animation.
        zoomValueAnimator!!.start()
    }

    private fun createZoomValueAnimator(zoomIn: Boolean): ValueAnimator? {
        val zoomValueAnimator = ValueAnimator.ofFloat(0.1f, 0f)
        zoomValueAnimator.interpolator = AccelerateDecelerateInterpolator()
        zoomValueAnimator.addUpdateListener { animation: ValueAnimator ->
            // Called periodically until zoomVelocity is zero.
            val zoomVelocity = animation.animatedValue as Float
            var zoomFactor = 1.0
            zoomFactor = if (zoomIn) zoomFactor + zoomVelocity else zoomFactor - zoomVelocity
            // zoomFactor values > 1 will zoom in and values < 1 will zoom out.
            camera.zoomBy(zoomFactor, zoomOrigin!!)
        }
        val halfSecond: Long = 500
        zoomValueAnimator.duration = halfSecond
        return zoomValueAnimator
    }

    // Stop any ongoing zoom animation.
    fun stopAnimations() {
        if (zoomValueAnimator != null) {
            zoomValueAnimator!!.cancel()
        }
    }
}