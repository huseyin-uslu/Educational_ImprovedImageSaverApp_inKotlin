package com.firstprojects.artbook_kotlin_using_fragments.fragments

import android.app.Activity
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firstprojects.artbook_kotlin_using_fragments.R
import com.firstprojects.artbook_kotlin_using_fragments.adapter.AdapterForRecyclerView
import com.firstprojects.artbook_kotlin_using_fragments.adapter.INDEXNAMENEEDEDTODELETE
import com.firstprojects.artbook_kotlin_using_fragments.model.InformationAboutPicturesModel
import java.lang.Exception


class TheFragmentForMainScreenAndRecyclerView : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_linking_activities,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
     if(item.itemId == R.id.anItemOftheOptionsMenuForUploading) {

         //destinationProcessFromMainFragmentToUploadFragment
         val fragmentManager = requireActivity().supportFragmentManager
         val action = TheFragmentForMainScreenAndRecyclerViewDirections.actionTheFragmentForMainScreenAndRecyclerViewToTheFragmentUploadingSelectedPictures()
         val navHostFragment : NavHostFragment = fragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
         val navController = navHostFragment.navController
         navController.navigate(action)

     }else if (item.itemId == R.id.anItemOftheOptionsMenuForDeletingASelectedObject){
         if (INDEXNAMENEEDEDTODELETE == null) {
             Toast.makeText(requireActivity(), "to delete a picture , you must long click an item you want to delete.", Toast.LENGTH_LONG).show()
         }else {
             //delete process
             db.execSQL("DELETE from data WHERE name = ?", arrayOf(INDEXNAMENEEDEDTODELETE.toString()))

             // Reload current fragment // Refresh Fragment , Database and RecyclerView
             refreshCurrentFragment()
             INDEXNAMENEEDEDTODELETE = null
         }
     }
        return super.onOptionsItemSelected(item)
    }

    //decleration>start
    private lateinit var recyclerView      : RecyclerView
    private lateinit var informationArrays : ArrayList<InformationAboutPicturesModel>
    private lateinit var infoAboutPictures : InformationAboutPicturesModel
    private lateinit var arrayAdapter      : AdapterForRecyclerView
    private lateinit var db                : SQLiteDatabase
    //decleration>end

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        INDEXNAMENEEDEDTODELETE = null //adjust delete mechanic

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_the_for_main_screen_and_recycler_view, container, false)

        //initialize
        recyclerView      = view.findViewById(R.id.recyclerView)
        informationArrays = arrayListOf()
        arrayAdapter      = AdapterForRecyclerView(informationArrays,requireActivity())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //after initialized
        if(arrayAdapter.itemCount == 0) {
            Toast.makeText(requireActivity(), "You don't have any pictures I can show you.", Toast.LENGTH_LONG).show()
        }

        //install database
        db = requireActivity().openOrCreateDatabase("Data", Activity.MODE_PRIVATE,null)
        db.execSQL("CREATE TABLE IF NOT EXISTS data(id INTEGER PRIMARY KEY ,name VANCHAR ,date VANCHAR,note VANCHAR,image BLOB)")

        //getDatafrom
        getDataFromDB(informationArrays)
    }

    private fun getDataFromDB (arrayList : ArrayList<InformationAboutPicturesModel>){

        try {
            //install cursor
            val cursor : Cursor = db.rawQuery("SELECT * FROM data",null)

            //cursor column index
            val nameIX  = cursor.getColumnIndex("name")
            val noteIX  = cursor.getColumnIndex("note")
            val dateIX  = cursor.getColumnIndex("date")
            val imageIX = cursor.getColumnIndex("image")

            //run cursor
            while (cursor.moveToNext()) {

                val name  = cursor.getString(nameIX)
                val note  = cursor.getString(noteIX)
                val date  = cursor.getString(dateIX)
                val image = cursor.getBlob(imageIX)

                //image decode process
                val bitmap = BitmapFactory.decodeByteArray(image,0,image.size)

                //put array
                infoAboutPictures = InformationAboutPicturesModel(name,note,date,bitmap)
                arrayList.add(infoAboutPictures)
                arrayAdapter.notifyDataSetChanged()
            }
            cursor.close()

        }catch (e : Exception) {
            e.printStackTrace()
            println(e.localizedMessage)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = arrayAdapter
    }

    private fun refreshCurrentFragment(){
        val navHostFragment : NavHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val id = navController.currentDestination?.id
        navController.popBackStack(id!!,true)
        navController.navigate(id)
    }



}