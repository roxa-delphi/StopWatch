package jp.example.roxa.stopwatch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    val handler = Handler()
    var timeValue = 0
    var status  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timeText = findViewById(R.id.timeText) as TextView
        val startButton = findViewById(R.id.start) as Button
        val stopButton = findViewById(R.id.stop) as Button
        val resetButton = findViewById(R.id.reset) as Button
        val watchButton = findViewById(R.id.watch) as Button

        val runnable = object : Runnable {
            override fun run() {
                timeValue ++

                timeToText(timeValue).let {
                    timeText.text = it
                }
                handler.postDelayed(this, 1000)
            }
        }

        val runwatch = object : Runnable {
            override fun run() {
                val date = Date()
                val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                timeText.text = format.format(date)
                handler.postDelayed(this, 1000)
            }
        }

        startButton.setOnClickListener {
            if (status == 0) {
                handler.post(runnable)
                status = 1

                startButton.isEnabled = false
                stopButton.isEnabled = true
                resetButton.isEnabled = true
                watchButton.isEnabled = false
            }
        }

        stopButton.setOnClickListener{
            if (status == 1) {
                handler.removeCallbacks(runnable)
                status = 0

                startButton.isEnabled = true
                stopButton.isEnabled = false
                resetButton.isEnabled = true
                watchButton.isEnabled = true
            }
        }

        resetButton.setOnClickListener{
            if (status == 1) {
                handler.removeCallbacks(runnable)
                timeValue = 0
            } else if (status == 2) {
                handler.removeCallbacks(runwatch)
            }
            status = 0
            timeValue = 0

            timeToText().let{
                timeText.text = it
            }

            startButton.isEnabled = true
            stopButton.isEnabled = false
            resetButton.isEnabled = false
            watchButton.isEnabled = true
        }

        watchButton.setOnClickListener{
            if (status == 0) {
                handler.post(runwatch)
                status = 2

                startButton.isEnabled = false
                stopButton.isEnabled = false
                resetButton.isEnabled = true
                watchButton.isEnabled = false
            }
        }
    }

    private fun timeToText(time: Int = 0): String {
        return if (time < 0) {
            "00:00:00"
        } else if (time == 0) {
            "00:00:00"
        } else {
            val h = time / 3600
            val m = time % 3600 / 60
            val s = time % 60
            "%1$02d:%2$02d:%3$02d".format(h, m, s)
        }
    }
}
