package com.cs125.anappleaday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cs125.anappleaday.data.sql.SQLDatabase

class MainActivity : AppCompatActivity() {

    val sqlDatabase by lazy { SQLDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}