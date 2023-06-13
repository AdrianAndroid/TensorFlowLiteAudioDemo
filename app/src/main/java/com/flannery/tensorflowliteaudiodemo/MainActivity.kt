package com.flannery.tensorflowliteaudiodemo

import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.tensorflow.lite.support.label.Category

class MainActivity : AppCompatActivity() {

    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this@MainActivity, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private var audioHelper: AudioClassificationHelper? = null
    private val audioClassificationListener = object : AudioClassificationListener {
        override fun onError(error: String) {
            Log.e("MainActivity", "error message: $error")
        }

        override fun onResult(results: List<Category>, inferenceTime: Long) {
            val sb = StringBuilder()
            results.forEach {
                sb.append(it.label).append(",")
            }
            Log.i("MainActivity", sb.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this, RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestPermissionLauncher.launch(RECORD_AUDIO)
        }

        findViewById<View>(R.id.startRecording).setOnClickListener {
            audioHelper = AudioClassificationHelper(this@MainActivity, audioClassificationListener)
        }
    }
}