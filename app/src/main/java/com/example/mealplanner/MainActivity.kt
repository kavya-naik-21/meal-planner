package com.example.mealplanner

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnAdd=findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener{
            addRecord()
        }
        setupListofDataIntoRecyclerView()
    }
    //Method for saving the employee records in database
    private fun addRecord() {
        val editTextItemName=findViewById<EditText>(R.id.editTextItemName)
        val editTextItemDescription=findViewById<EditText>(R.id.editTextItemDescription)
        val name = editTextItemName.text.toString()
        val description = editTextItemDescription.text.toString()
        val databaseHandler: DataBaseHandler = DataBaseHandler(this)
        if (!name.isEmpty() && !description.isEmpty()) {
            val status =
                databaseHandler.addMeal(MealDataClass(0, name, description))
            if (status > -1) {
                Toast.makeText(applicationContext, "Meal saved", Toast.LENGTH_LONG).show()
                editTextItemName.text.clear()
                editTextItemDescription.text.clear()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Name or Desc cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
        setupListofDataIntoRecyclerView()
    }


    /**
     * Function is used to get the Items List which is added in the database table.
     */
    private fun getItemsList(): ArrayList<MealDataClass> {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DataBaseHandler = DataBaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val mealList: ArrayList<MealDataClass> = databaseHandler.viewMeal()

        return mealList
    }
    /**
     * Function is used to show the list on UI of inserted data.
     */
    private fun setupListofDataIntoRecyclerView() {
        val recyclerViewItemsList=findViewById<RecyclerView>(R.id.recyclerViewItemsList)
        val noMealsAvailable=findViewById<TextView>(R.id.noMealsAvailable)
        if (getItemsList().size > 0) {

            recyclerViewItemsList.visibility = View.VISIBLE
            noMealsAvailable.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.

            recyclerViewItemsList.layoutManager = LinearLayoutManager(this)
            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = ItemsAdapter(this, getItemsList())
            // adapter instance is set to the recyclerview to inflate the items.
            recyclerViewItemsList.adapter = itemAdapter
        } else {

            recyclerViewItemsList.visibility = View.GONE
            noMealsAvailable.visibility = View.VISIBLE
        }
    }
    /**
     * Method is used to show the custom update dialog.
     */
    fun updateRecordDialog(mealDataClass: MealDataClass) {
        val updateDialog = Dialog(this,R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog.setContentView(R.layout.edit_dialog)
        updateDialog.findViewById<EditText>(R.id.editTextUpdateName).setText(mealDataClass.name)
        updateDialog.findViewById<EditText>(R.id.editTextUpdateDescription).setText(mealDataClass.name)
        Toast.makeText(this, "Came here 1", Toast.LENGTH_LONG).show()
        updateDialog.findViewById<TextView>(R.id.textViewUpdate).setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Came here 2", Toast.LENGTH_LONG).show()

            val name = updateDialog.findViewById<EditText>(R.id.editTextUpdateName).text.toString()
            val description = updateDialog.findViewById<EditText>(R.id.editTextUpdateDescription).text.toString()

            val databaseHandler: DataBaseHandler = DataBaseHandler(this)

            if (!name.isEmpty() && !description.isEmpty()) {
                val status =
                    databaseHandler.updateMeal(MealDataClass(mealDataClass.id, name, description))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Meal Updated.", Toast.LENGTH_LONG).show()

                    setupListofDataIntoRecyclerView()

                    updateDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Name or Meal cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        updateDialog.findViewById<TextView>(R.id.textViewCancel).setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })

        //Start the dialog and display it on screen.
        updateDialog.show()
    }
    /**
    * Method is used to show the Alert Dialog.
    */
    fun deleteRecordAlertDialog(mealDataClass: MealDataClass) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Meal")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete ${mealDataClass.name}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            val databaseHandler: DataBaseHandler = DataBaseHandler(this)
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteMeal(MealDataClass(mealDataClass.id, "", ""))
            if (status > -1) {
                Toast.makeText(
                    applicationContext,
                    "Meal deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()

                setupListofDataIntoRecyclerView()
            }

            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }
}
