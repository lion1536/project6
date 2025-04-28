package com.example.project6

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Adapter.NO_SELECTION
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private val categories = listOf(
        "Length",
        "Area",
        "Data Transfer Rate",
        "Digital Storage",
        "Energy",
        "Frequency",
        "Fuel Economy",
        "Mass",
        "Plane Angle",
        "Pressure",
        "Speed",
        "Temperature",
        "Time",
        "Volume"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spinner = findViewById(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(NO_SELECTION, false)

    }

    override fun onStart() {
        super.onStart()
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val selectedCategory = categories[position]
                val intent = Intent(this@MainActivity, ConversionActivity::class.java)
                intent.putExtra("category", selectedCategory)
                startActivity(intent)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
