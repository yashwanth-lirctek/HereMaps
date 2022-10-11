package com.lirctek.heremaps

import android.app.Activity
import android.app.Application
import android.content.Context
import com.here.sdk.core.engine.SDKNativeEngine
import com.here.sdk.core.engine.SDKOptions
import com.here.sdk.core.errors.InstantiationErrorException
import java.lang.ref.WeakReference

class HereMaps: Application() {

    var currentActivity: WeakReference<Activity> = WeakReference(null)

    companion object {
        lateinit  var app:HereMaps
    }

    fun  getApp():HereMaps{
        return app
    }

    override fun onCreate() {
        super.onCreate()

        app = this
        loadHereMapsSDK()
    }

    private fun loadHereMapsSDK(){

        // Set your credentials for the HERE SDK.
        val accessKeyID = "tHgj5SdX11uviUzJeMgmTQ"
        val accessKeySecret = "iPB8-gZKrILbFxudD0VZHsA8TRmAtv41bVsr9sHHvsWbn0d7dKnv8gPBiRrEQ7GfYitZbIR8JPecLRxkgUGgOQ"
        val options = SDKOptions(accessKeyID, accessKeySecret)
        try {
            val context: Context = this
            SDKNativeEngine.makeSharedInstance(context, options)
        } catch (e: InstantiationErrorException) {
            throw RuntimeException("Initialization of HERE SDK failed: " + e.error.name)
        }
    }

    fun disposeHERESDK() {
        // Free HERE SDK resources before the application shuts down.
        // Usually, this should be called only on application termination.
        // Afterwards, the HERE SDK is no longer usable unless it is initialized again.
        val sdkNativeEngine = SDKNativeEngine.getSharedInstance()
        if (sdkNativeEngine != null) {
            sdkNativeEngine.dispose()
            // For safety reasons, we explicitly set the shared instance to null to avoid situations,
            // where a disposed instance is accidentally reused.
            SDKNativeEngine.setSharedInstance(null)
        }
    }

}