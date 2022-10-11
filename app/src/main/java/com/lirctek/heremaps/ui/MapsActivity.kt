package com.lirctek.heremaps.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.here.sdk.mapview.MapScheme
import com.lirctek.heremaps.HereMaps
import com.lirctek.heremaps.databinding.ActivityMapsBinding
import com.lirctek.heremaps.ui.gestures.GesturesData
import com.lirctek.heremaps.ui.traffic.TrafficData


class MapsActivity : AppCompatActivity() {

    lateinit var mViewBinding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        mViewBinding.mMapView.onCreate(savedInstanceState)

        loadMapScene()
    }

    private fun loadMapScene() {
        // Load a scene from the HERE SDK to render the map with a map scheme.
        mViewBinding.mMapView.mapScene.loadScene(MapScheme.NORMAL_DAY) { mapError ->
            if (mapError == null) {
                GesturesData(this, mViewBinding.mMapView)
                TrafficData(this, mViewBinding.mMapView)
            } else {
                Log.d("loadMapScene()", "Loading map failed: mapError: " + mapError.name)
            }
        }
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