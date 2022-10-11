package com.lirctek.heremaps.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lirctek.heremaps.databinding.ActivitySplashBinding
import com.lirctek.heremaps.ui.MapsActivity

class SplashActivity : AppCompatActivity() {

    lateinit var mViewBinding: ActivitySplashBinding

    private val TAG: String = SplashActivity::class.java.simpleName
    private var permissionsRequestor: PermissionsRequestor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)

        handleAndroidPermissions()

    }

    private fun handleAndroidPermissions() {
        permissionsRequestor =
            PermissionsRequestor(this)
        permissionsRequestor?.request(object :
            PermissionsRequestor.ResultListener {
            override fun permissionsGranted() {
                launchMapsActivity()
            }

            override fun permissionsDenied() {
                Log.e(TAG, "Permissions denied by user.")
            }
        })
    }

    private fun launchMapsActivity() {
        startActivity(Intent(this, MapsActivity::class.java))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsRequestor?.onRequestPermissionsResult(requestCode, grantResults)
    }
}