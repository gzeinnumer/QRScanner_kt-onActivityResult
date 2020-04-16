package com.gzeinnumer.qrscannerkt

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    var barcodeDetector: BarcodeDetector? = null
    var cameraSource: CameraSource? = null
    val PERMISSION1 = 1111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()
        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(641, 481)
            .setAutoFocusEnabled(true)
            .build()

        cameraPreview.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {

                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.CAMERA),
                        PERMISSION1
                    )
                    return
                }
                try {
                    cameraSource?.start(cameraPreview.getHolder())
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource?.stop()
            }
        })

        barcodeDetector?.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {

            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val qr = detections.detectedItems
                if (qr.size() != 0) {
                    txtResult.post {
                        val vibrator =
                            applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        vibrator.vibrate(999)
                        txtResult.text = qr.valueAt(0).displayValue
                        barcodeDetector?.release()

                        val index = intent.getIntExtra("index",0)
                        val returnIntent = Intent()
                        returnIntent.putExtra("value", txtResult.text.toString())

                        Log.d(
                            "MyZein",
                            "Main value " + txtResult.text.toString() + " Index " + index
                        )
                        setResult(index, returnIntent)
                        finish()
                    }
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION1 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    try {

                        cameraSource?.start(cameraPreview.holder)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
