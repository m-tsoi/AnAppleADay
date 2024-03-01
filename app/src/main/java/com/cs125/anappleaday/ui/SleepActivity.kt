package com.cs125.anappleaday.ui

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.record.models.SleepData
import com.cs125.anappleaday.data.record.models.SleepSession
import com.cs125.anappleaday.services.firestore.FbSleepServices
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import nl.joery.timerangepicker.TimeRangePicker
import java.util.Date
import kotlin.math.min

class SleepActivity : AppCompatActivity() {

    private lateinit var sleep_rec : TextView
    private lateinit var sleep_score : TextView
    private lateinit var sleep_duration : TextView
    private lateinit var sleep_duration_picker : TimeRangePicker
    private lateinit var enter_next_sleep : Button
    private lateinit var time_range_confirm : Button
    private lateinit var sleepDataDocRef : DocumentReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep)

        sleep_score = findViewById(R.id.sleep_score)
        sleep_duration = findViewById(R.id.sleep_duration)
        sleep_rec = findViewById(R.id.sleep_recommendation)
        sleep_duration_picker = findViewById<TimeRangePicker>(R.id.sleep_duration_picker)
        enter_next_sleep = findViewById<Button>(R.id.enter_next_sleep)
        time_range_confirm = findViewById<Button>(R.id.time_range_confirm)

        val db = Firebase.firestore

        //TODO: get the right document from the right user
        sleepDataDocRef =  db.collection("SleepData").document("rWZTS6CwoL3aq8fnMci8")

        // Initialize the UI with the info from db
        updateUI()

        // Record last night's sleep duration to calculate the sleep score
        sleep_duration_picker
            .setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener{
                override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
                    Log.d("TimeRangePicker", "Start time: " + startTime)
                }

                override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
                    Log.d("TimeRangePicker", "End time: " + endTime)
                }

                override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {
                    Log.d("TimeRangePicker", "Duration: " + duration)

                    time_range_confirm.setVisibility(View.VISIBLE)
                    sleep_duration.setText("")
                    sleep_score.setText("??")
                }
            })

        // Click this button to send the sleep duration from the duration picker to the backend
        time_range_confirm
            .setOnClickListener{
                Log.d("BUTTONS", "User confirmed time range")
                val duration = sleep_duration_picker.duration
                sleep_duration.setText("You have slept for " + duration)

                //  Sleep score formula
                val score = min((duration.durationMinutes / (8.0 * 60) * 100).toInt(), 100)
                sleep_score.setText(score.toString())

                time_range_confirm.setVisibility(View.GONE)

                val today = Date()
                val startTime = Date(today.year, today.month, today.date,
                    sleep_duration_picker.startTime.hour,
                    sleep_duration_picker.startTime.minute)
                val endTime = Date(today.year, today.month, today.date,
                    sleep_duration_picker.endTime.hour,
                    sleep_duration_picker.endTime.minute)
                updateDB(startTime, endTime, score, null)
            }

        // Record next morning's wake up time for sleep recommendation
        enter_next_sleep
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")

                val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker, hour, minute ->
                    val sleep_hour = Math.floorMod(hour - 8, 24)
                    sleep_rec.setText("To wake up refreshed tomorrow, you should head to bed at "
                            + sleep_hour + ":" + minute)
                    val today = Date()
                    updateDB(null, null, null,
                        Date(today.year, today.month, today.date, sleep_hour, minute))
                }

                TimePickerDialog(this, timeSetListener, 24, 60, false).show()
            }
    }

    // Update UI with data from db
    fun updateUI() {
        sleepDataDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("SLEEP DATA", document.data.toString())
                    val sleepData = document.toObject(SleepData::class.java)!!
                    Log.d("SLEEP DATA", sleepData.dailySleepRecords.toString())

                    val sleepRecordsToday = sleepData.dailySleepRecords.last()
                    if (!isToday(sleepRecordsToday.enteredDate))  {
                        time_range_confirm.setVisibility(View.VISIBLE)
                        sleep_score.setText("??")
                    } else {
                        val sleepStartTime : TimeRangePicker.Time?
                        if (sleepRecordsToday.startTime != null) {
                            sleepStartTime = TimeRangePicker.Time(
                                sleepRecordsToday.startTime!!.hours,
                                sleepRecordsToday.startTime!!.minutes
                            )
                        } else {
                            sleepStartTime = null
                        }

                        val sleepEndTime : TimeRangePicker.Time?
                        if (sleepRecordsToday.endTime != null) {
                            sleepEndTime = TimeRangePicker.Time(
                                sleepRecordsToday.endTime!!.hours,
                                sleepRecordsToday.endTime!!.minutes
                            )
                        } else {
                            sleepEndTime = null
                        }

                        if (sleepStartTime != null && sleepEndTime != null) {
                            val sleepEndTimeMinutes = sleepRecordsToday.endTime!!.hours * 60 + sleepRecordsToday.endTime!!.minutes
                            sleep_duration_picker.startTime = sleepStartTime
                            sleep_duration_picker.endTimeMinutes = sleepEndTimeMinutes
                            time_range_confirm.setVisibility(View.GONE)
                            sleep_duration.setText("You have slept for " + TimeRangePicker.TimeDuration(sleepStartTime, sleepEndTime).toString())
                            sleep_score.setText(sleepRecordsToday.sleepScore.toString())
                        } else {
                            sleep_duration_picker.startTime = TimeRangePicker.Time(1, 0)
                            sleep_duration_picker.endTimeMinutes = 2 * 60
                            time_range_confirm.setVisibility(View.VISIBLE)
                            sleep_duration.setText("")
                            sleep_score.setText("??")
                        }

                        sleep_rec.setText("To wake up refreshed tomorrow, you should head to bed at "
                                + sleepRecordsToday.recomEndTime?.hours + ":" + sleepRecordsToday.recomEndTime?.minutes)
                    }
                } else {
                    Log.d("SLEEP DATA", "failed :<")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("SLEEP DATA", "get failed with ", exception)
            }
    }

    // Update db with data entered from the UI
    fun updateDB(startTime: Date?, endTime: Date?, sleepScore: Int?, recomEndTime: Date?) {
        sleepDataDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val sleepData = document.toObject(SleepData::class.java)!!
                    val sleepRecordsToday = sleepData.dailySleepRecords.last()

                    if (isToday(sleepRecordsToday.enteredDate))  {
                        sleepDataDocRef.update("dailySleepRecords", FieldValue.arrayRemove(sleepRecordsToday))
                        if (startTime != null) {
                            sleepRecordsToday.startTime = startTime
                        }
                        if (endTime != null) {
                            sleepRecordsToday.endTime = endTime
                        }
                        if (sleepScore != null) {
                            sleepRecordsToday.sleepScore = sleepScore
                        }
                        if (recomEndTime != null) {
                            sleepRecordsToday.recomEndTime = recomEndTime
                        }
                        sleepDataDocRef.update("dailySleepRecords", FieldValue.arrayUnion(sleepRecordsToday))
                    } else {
                        val sleepSession = SleepSession(startTime, endTime, sleepScore, recomEndTime, Date())
                        sleepDataDocRef.update("dailySleepRecords", FieldValue.arrayUnion(sleepSession))
                    }

                } else {
                Log.d("SLEEP DATA", "failed :<")
            }
        }
        .addOnFailureListener { exception ->
            Log.d("SLEEP DATA", "get failed with ", exception)
        }
    }

    // Check if the input date is today's date
    fun isToday(date: Date?): Boolean {
        val today = Date()
        if (date != null
            && date.day == today.day
            && date.month == today.month
            && date.year == today.year) {
            return true
        } else {
            return false
        }
    }
}