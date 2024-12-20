package com.design2.chili2.util.secured

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.edit
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.myapplication.secure_view_component.contracts.OnApplicationSecureGestureListener

class ApplicationSecureGestureDelegate : OnApplicationSecureGestureListener, SensorEventListener {

    private lateinit var _prefs: SharedPreferences
    private lateinit var _context: Context
    private var _isHasVibratePermission: Boolean = false

    private lateinit var _sensorManager: SensorManager
    private lateinit var _vibrator: Vibrator
    private var _accelerometer: Sensor? = null

    private var _isScreenDown = false
    private var _screenDownTriggerTime: Long = 0
    private var _isSecuredNow = false
    private var _isAppActiveNow = false

    override fun onApplicationCreated(context: Context, sharedPreferences: SharedPreferences) {
        _context = context.applicationContext
        _prefs = sharedPreferences
        _isSecuredNow = _prefs.getBoolean(BROADCAST_TAG, false)
        _isHasVibratePermission = _context.packageManager.checkPermission(
            android.Manifest.permission.VIBRATE,
            _context.packageName
        ) == PackageManager.PERMISSION_GRANTED

        initComponents()
        setupAppLifecycleListener()
        _sensorManager.registerListener(this, _accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun setupAppLifecycleListener() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                _isAppActiveNow = true
                _sensorManager
                    .registerListener(
                        this@ApplicationSecureGestureDelegate,
                        _accelerometer,
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
            }

            override fun onStop(owner: LifecycleOwner) {
                _isAppActiveNow = false
                _sensorManager.unregisterListener(this@ApplicationSecureGestureDelegate)
                super.onStop(owner)
            }
        })
    }

    private fun initComponents() {
        _sensorManager = _context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        _accelerometer = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        _vibrator = _context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun isSecuredNow(): Boolean = _isSecuredNow

    override fun switchSecuredState() {
        _isSecuredNow = !_isSecuredNow
        _prefs.edit { putBoolean(BROADCAST_TAG, _isSecuredNow) }
        LocalBroadcastManager.getInstance(_context).sendBroadcast(Intent(BROADCAST_TAG))
        pushVibration()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER && _isAppActiveNow) {
            val isValidYAxis = event.values[1] in -1.0..1.0
            val zAxis = event.values[2]

            if (zAxis < -8.0 && isValidYAxis && !_isScreenDown) {
                _isScreenDown = true
                _screenDownTriggerTime = System.currentTimeMillis()
            } else if (zAxis > 8.0 && _isScreenDown) {
                val upTime = System.currentTimeMillis()
                val timeDiff = upTime - _screenDownTriggerTime
                if (timeDiff <= GESTURE_THRESHOLD) switchSecuredState()
                _isScreenDown = false
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun pushVibration() {
        when {
            !_isHasVibratePermission -> return
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                val heartbeatPattern = longArrayOf(0, 50, 100, 60)
                val vibrationEffect = VibrationEffect.createWaveform(heartbeatPattern, -1)
                _vibrator.vibrate(vibrationEffect)
            }

            else -> {
                _vibrator.vibrate(100)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    companion object {
        private const val GESTURE_THRESHOLD = 1000L
    }
}