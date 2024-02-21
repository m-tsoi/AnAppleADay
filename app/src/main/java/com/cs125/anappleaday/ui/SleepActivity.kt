package com.cs125.anappleaday.ui

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.cs125.anappleaday.R
import nl.joery.timerangepicker.TimeRangePicker
import kotlin.math.min

class SleepActivity : AppCompatActivity() {

    private lateinit var sleep_rec : TextView
    private lateinit var sleep_score : TextView
    private lateinit var sleep_duration : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep)

        sleep_score = findViewById(R.id.sleep_score)
        sleep_duration = findViewById(R.id.sleep_duration)
        sleep_rec = findViewById(R.id.sleep_recommendation)

        // Record last night's sleep to calculate the sleep score
        findViewById<TimeRangePicker>(R.id.sleep_duration_picker)
            .setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener{
                override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
                    Log.d("TimeRangePicker", "Start time: " + startTime)
                }

                override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
                    Log.d("TimeRangePicker", "End time: " + endTime.hour)
                }

                override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {
                    Log.d("TimeRangePicker", "Duration: " + duration.hour)
                    sleep_duration.setText("You have slept for " + duration)

                    //  Sleep score formula
                    var score = min((duration.durationMinutes / (8.0 * 60) * 100).toInt(), 100)
                    sleep_score.setText(score.toString())
                }
            })

        // Record next morning's wake up time for sleep recommendation
        findViewById<Button>(R.id.enter_next_sleep)
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")

                val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker, hour, minute ->
                    var sleep_hour = Math.floorMod(hour - 8, 24)
                    sleep_rec.setText("To wake up refreshed tomorrow, you should head to bed at "
                            + sleep_hour + ":" + minute)
                }

                TimePickerDialog(this, timeSetListener, 24, 60, false).show()
            }
    }
}