package com.lirctek.heremaps.ui.`interface`

import com.here.sdk.core.Point2D
import com.here.sdk.mapview.MapMarker

interface ToolTipInterface {
    fun showToolTip(topmostMapMarker: MapMarker, touchPoint: Point2D)
}