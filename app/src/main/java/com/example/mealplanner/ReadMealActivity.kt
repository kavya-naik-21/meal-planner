package com.example.mealplanner

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject

class ReadMealActivity : AppCompatActivity() {

    private lateinit var btnContribute: Button
    private lateinit var textViewName:   TextView
    private lateinit var textViewDescription: TextView
    private lateinit var textViewMethod: TextView
    lateinit var auth: FirebaseAuth

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
            auth= FirebaseAuth.getInstance()
            var currentUser=auth.currentUser
            Toast.makeText(this,currentUser.toString(),Toast.LENGTH_LONG).show()
            if(currentUser==null){
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
//                Toast.makeText(this,"Your are not logged in",Toast.LENGTH_LONG).show()
            }
            else{
                Contribute(MealDataClass(0,name.toString(),description.toString(),method.toString()))
            }
        }
    }

    private fun Contribute(meal: MealDataClass){

        var volleyRequestQueue: RequestQueue? = null
        var dialog: ProgressDialog? = null

        val serverAPIURL = "https://mealsplannerapi.herokuapp.com/meals/store"

        volleyRequestQueue = Volley.newRequestQueue(this)
        dialog = ProgressDialog.show(this, "", "Please wait...", true)

        val parameters: MutableMap<String, String> = HashMap()

        // Add your parameters in HashMap
        parameters.put("name",meal.name)
        parameters.put("description",meal.description)
        parameters.put("method",meal.method)

        val strReq: StringRequest = object : StringRequest(
            Method.POST,serverAPIURL,
            Response.Listener { response ->
                dialog?.dismiss()

                // Handle Server response here
                try {
                    val responseObj = JSONObject(response)
                    val message = responseObj.getString("message")
                    Toast.makeText(this,message,Toast.LENGTH_LONG).show()

                } catch (e: Exception) { // caught while parsing the response
                        e.printStackTrace()
                }
                              },
            Response.ErrorListener { volleyError -> // error occurred
                    Log.e("error", "problem occurred, volley error: " + volleyError.message)
            })
                {

                override fun getParams(): MutableMap<String, String> {
                    return parameters;
                } //End of getParams

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers: MutableMap<String, String> = HashMap()
                    // Add your Header paramters here
                    return headers
                }//End of get Headers

            } //End of Override functions



            // Adding request to request queue
            volleyRequestQueue?.add(strReq)

        } //End of Contribute function


    }

