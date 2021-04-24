package com.firstprojects.artbook_kotlin_using_fragments.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.firstprojects.artbook_kotlin_using_fragments.R
import com.firstprojects.artbook_kotlin_using_fragments.fragments.TheFragmentForMainScreenAndRecyclerViewDirections
import com.firstprojects.artbook_kotlin_using_fragments.model.InformationAboutPicturesModel

var INDEXNAMENEEDEDTODELETE : String? = null //when you long click an index , this variable will take your index number and you will have a chance to delete the item.

class AdapterForRecyclerView (private var arrayList : ArrayList<InformationAboutPicturesModel>,
                              private var context  : Activity
): RecyclerView.Adapter<AdapterForRecyclerView.InformationHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformationHolder {
       //inflate layout
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.rows_for_recyclerview,parent,false)
        return InformationHolder(view)

    }

    override fun onBindViewHolder(holder: InformationHolder, position: Int) {

       //show data
        holder.nameTextView.text = arrayList[position].namesInArray
        holder.noteTextView.text = arrayList[position].notesInArray
        holder.dateTextView.text = arrayList[position].datesInArray
        holder.imageView.setImageBitmap(arrayList[position].imagesInArray)

        holder.itemView.setOnClickListener { view ->
            //action && sending args
            val action = TheFragmentForMainScreenAndRecyclerViewDirections.actionTheFragmentForMainScreenAndRecyclerViewToTheFragmentShowingSavedPictureNames(arrayList[holder.adapterPosition])
            Navigation.findNavController(view).navigate(action)
        }

        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
               Toast.makeText(context, "${arrayList[position].namesInArray}, CLICK THE DELETE BUTTON TO DESTROY THIS PICTURE...", Toast.LENGTH_LONG).show()
               INDEXNAMENEEDEDTODELETE = arrayList[position].namesInArray //because of deleting
                println(INDEXNAMENEEDEDTODELETE.toString())
                return v != null
            }

        })

    }
    override fun getItemCount(): Int {

        return arrayList.size
    }

    class InformationHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        //holder declaration
        var nameTextView : TextView  = itemView.findViewById(R.id.row_inRecyclerView_textView_Name)
        var noteTextView : TextView  = itemView.findViewById(R.id.row_inRecyclerView_textView_Note)
        var dateTextView : TextView  = itemView.findViewById(R.id.row_inRecyclerView_textView_Date)
        var imageView    : ImageView = itemView.findViewById(R.id.row_inRecyclerView_textView_ImageView)

    }
}