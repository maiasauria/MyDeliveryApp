package com.mleon.mydeliveryapp.presentation.activities

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.text.TextWatcher
import com.mleon.mydeliveryapp.presentation.fragments.AFragment
import com.mleon.mydeliveryapp.presentation.fragments.BFragment
import com.mleon.mydeliveryapp.R

class LifecycleDemoActivity : AppCompatActivity() {

    private val TAG = "LifecycleDemoActivity"
    private var counter = 0
    private var isAFragmentVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lifecycle_demo)
        Log.d(TAG, "onCreate")

        counter = savedInstanceState?.getInt("counter") ?: 0

        val tvCounter = findViewById<EditText>(R.id.tvCounter)
        val btnCounter = findViewById<Button>(R.id.btnIncrementar)
        tvCounter.setText(counter.toString())

        tvCounter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                counter = s?.toString()?.toIntOrNull() ?: 0
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnCounter.setOnClickListener {
            counter++
            tvCounter.setText(counter.toString())
            Log.d("MainActivity", "onCreate: Contador incrementado a $counter")
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, AFragment.newInstance("Hola", "Mundo"))
                .commit()
            isAFragmentVisible = true
        }

        isAFragmentVisible = true

        val btnChangeFg = findViewById<Button>(R.id.btnChangeFragment)
        btnChangeFg.setOnClickListener {
            if (isAFragmentVisible) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, BFragment.newInstance("Hola", "Mundo"))
                    .commit()
            } else {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, AFragment.newInstance("Hola", "Mundo"))
                    .commit()
            }
            isAFragmentVisible = !isAFragmentVisible
        }
    }

    override fun onSaveInstanceState(outState : Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("counter", counter)
        Log.d("MainActivity", "onSaveInstanceState: Guardando el estado de la actividad.")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}