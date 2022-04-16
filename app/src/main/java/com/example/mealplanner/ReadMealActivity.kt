package com.example.mealplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class ReadMealActivity : AppCompatActivity() {

    private lateinit var btnContribute: Button
    private lateinit var textViewName:   TextView
    private lateinit var textViewDescription: TextView
    private lateinit var textViewMethod: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_meal)

        val bundle: Bundle? = intent.extras

        val id:String?=bundle?.getString("id")
        val description:String?=bundle?.getString("description")
        val name: String? = bundle?.getString("name")
        val method: String? = bundle?.getString("method")

        textViewName=findViewById(R.id.textViewName)
        textViewDescription=findViewById(R.id.textViewDescription)
        textViewMethod=findViewById(R.id.textViewMethod)

        textViewName.setText(name)
        textViewDescription.setText(description)
        textViewMethod.setText(method)

        btnContribute=findViewById(R.id.btnContribute)

        btnContribute.setOnClickListener{
            Contribute(MealDataClass(0,name.toString(),description.toString(),method.toString()))
        }
    }

    private fun Contribute(meal: MealDataClass){
        Toast.makeText(this, meal.id.toString()+" "+meal.name+" "+meal.description+" "+meal.method+" ", Toast.LENGTH_LONG).show()
    }
}