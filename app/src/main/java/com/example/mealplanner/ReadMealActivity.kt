package com.example.mealplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager

class ReadMealActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_meal)
        val bundle: Bundle? = intent.extras
        val id:String?=bundle?.getString("id")
        val description:String?=bundle?.getString("description")
        val name: String? = bundle?.getString("name")
        val textViewName=findViewById<TextView>(R.id.textViewName)
        val textViewDescription=findViewById<TextView>(R.id.textViewDescription)
        textViewName.setText(name)
        textViewDescription.setText(description)
    }

}