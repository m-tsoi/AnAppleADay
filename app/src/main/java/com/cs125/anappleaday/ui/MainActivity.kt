package com.cs125.anappleaday.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.cs125.anappleaday.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class MainActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        drawerLayout = findViewById(R.id.drawer_layout)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_drawer_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        if (savedInstanceState == null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_profile -> print("Do something")
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



}