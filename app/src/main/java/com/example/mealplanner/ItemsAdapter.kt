package com.example.mealplanner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter(val context: Context, val items: ArrayList<MealDataClass>) :
    RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    /**
     * Inflates the item views which is designed in the XML layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.items_row,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)

        holder.textViewName.text = item.name

        // Updating the background color according to the odd/even positions in list.
        if (position % 2 == 0) {
            holder.linearlayoutMain.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.gray
                )
            )
        } else {
            holder.linearlayoutMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
        holder.btnViewMeal.setOnClickListener { view ->

            if (context is MainActivity) {
                context.viewMeal(item)
            }
        }
        holder.imageViewEdit.setOnClickListener { view ->

            if (context is MainActivity) {
                context.updateRecordDialog(item)
            }
        }

        holder.imageViewDelete.setOnClickListener { view ->

            if (context is MainActivity) {
                context.deleteRecordAlertDialog(item)
            }
        }

    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val linearlayoutMain: LinearLayout
        val textViewName: TextView
        val btnViewMeal: Button
        val imageViewEdit: ImageView
        val imageViewDelete: ImageView
        init {
            // Define click listener for the ViewHolder's View.
            linearlayoutMain = view.findViewById(R.id.linearlayoutMain)
            textViewName = view.findViewById(R.id.textViewName)
            btnViewMeal = view.findViewById(R.id.btnViewMeal)
            imageViewEdit = view.findViewById(R.id.imageViewEdit)
            imageViewDelete = view.findViewById(R.id.imageViewDelete)
        }

    }
}