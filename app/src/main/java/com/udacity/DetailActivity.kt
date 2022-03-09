package com.udacity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

        val button = findViewById<Button>(R.id.buttonDetailOk)
        button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            //Start exit transition
            startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

    }

}
