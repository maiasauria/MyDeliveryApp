package com.mleon.mydeliveryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val btnNavigate = findViewById<Button>(R.id.btnNavigateToDemo)

        btnNavigate.setOnClickListener {
            val intent = Intent(this, LifecycleDemoActivity::class.java)
            startActivity(intent)
        }
    }
}


