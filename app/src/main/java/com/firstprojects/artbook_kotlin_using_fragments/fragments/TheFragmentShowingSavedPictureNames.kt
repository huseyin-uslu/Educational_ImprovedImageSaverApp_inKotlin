package com.firstprojects.artbook_kotlin_using_fragments.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.firstprojects.artbook_kotlin_using_fragments.R
import com.firstprojects.artbook_kotlin_using_fragments.adapter.INDEXNAMENEEDEDTODELETE


class TheFragmentShowingSavedPictureNames : Fragment() {


    //decleration>start
    private lateinit var imageView    : ImageView
    private lateinit var nameTextView : TextView
    private lateinit var dateTextView : TextView
    //declaration>end

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_the_showing_saved_picture_names, container, false)

        INDEXNAMENEEDEDTODELETE = null //adjust delete mechanic
        //initialize
        imageView    = view.findViewById(R.id.showingFragment_textView_ImageView)
        nameTextView = view.findViewById(R.id.showingFragment_textView_Name)
        dateTextView = view.findViewById(R.id.showingFragment_textView_Date)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments != null) {
            arguments.let {
                if(it != null) {
                    val data = TheFragmentShowingSavedPictureNamesArgs.fromBundle(it).getDataModel

                    //show data
                    imageView.setImageBitmap(data.imagesInArray)
                    nameTextView.text = data.namesInArray
                    dateTextView.text = data.datesInArray
                }
            }
        }
    }
}