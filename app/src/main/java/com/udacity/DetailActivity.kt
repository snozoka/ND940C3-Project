package com.udacity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val fileNameTextView = findViewById<TextView>(R.id.textViewFileNameContent)
        val statusTextView = findViewById<TextView>(R.id.textViewStatusContent)

        fileNameTextView.text = intent.getStringExtra("FileName")
        statusTextView.text = intent.getStringExtra("Status")
    }

}
