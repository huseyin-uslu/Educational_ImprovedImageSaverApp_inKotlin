package com.firstprojects.artbook_kotlin_using_fragments.fragments

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity.MODE_PRIVATE
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import androidx.navigation.Navigation
import com.firstprojects.artbook_kotlin_using_fragments.R
import com.firstprojects.artbook_kotlin_using_fragments.adapter.INDEXNAMENEEDEDTODELETE
import java.io.ByteArrayOutputStream


class TheFragmentUploadingSelectedPictures : Fragment() {

    //decleration>start
    lateinit var picturesNameEditText: EditText
    lateinit var picturesDateEditText: EditText
    lateinit var noteAboutPictureEditText: EditText
    lateinit var imageView : ImageView
    lateinit var button : Button
             var bitmap : Bitmap? = null
    //decleration>stop

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        INDEXNAMENEEDEDTODELETE = null //adjust delete mechanic
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_the_uploading_selected_pictures, container, false)

        //initialising-start
        picturesNameEditText     = view.findViewById(R.id.uploadingFragment_picturesName_editText)
        picturesDateEditText     = view.findViewById(R.id.uploadingFragment_picturesDate_editText)
        noteAboutPictureEditText = view.findViewById(R.id.uploadingFragment_noteAboutPicture_editText)
        imageView                = view.findViewById(R.id.imageView2)
        button                   = view.findViewById(R.id.button)
        //-end
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //SELECT IMAGE ONCLICK
        imageView.setOnClickListener { v ->

                imageOnClick(v)
        }

        //SAVE IMAGE ONCLICK
        button.setOnClickListener    { v ->

                saveOnClick(v)
        }
    }

    private fun imageOnClick(view : View) {

    if(ContextCompat.checkSelfPermission(requireContext(),READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)       {
                         ActivityCompat.requestPermissions(requireActivity(), arrayOf(READ_EXTERNAL_STORAGE),1)
    }else {
                         val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                         startActivityForResult(intent,2)
       }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1 && permissions[0] == READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,2)
        }else {
            Toast.makeText(requireActivity(), "You have to approve the request permission to upload an image!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 2 && resultCode == RESULT_OK && data != null) {
            val getDataUri : Uri = data.data!!

            if(Build.VERSION.SDK_INT >= 28) {
            val source = ImageDecoder.createSource(requireActivity().contentResolver,getDataUri)
                bitmap = ImageDecoder.decodeBitmap(source)
                imageView.setImageBitmap(sizeMakerForPictures(bitmap!!))


            }else {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,getDataUri)
                imageView.setImageBitmap(sizeMakerForPictures(bitmap!!))

            }
        }
    }

    //<Data Decleration>
    private lateinit var picturesname : String
    private lateinit var picturesdate : String
    private lateinit var picturesnote : String
    //>Declaration End<

    private fun saveOnClick(view : View) {

        if(bitmap != null) {

            //pull writings which belong to the user..
                if(picturesNameEditText.length() != 0) {
                                picturesname = picturesNameEditText.text.toString()
                    if(picturesDateEditText.length() != 0) {
                                picturesdate = picturesDateEditText.text.toString()
                                picturesnote = noteAboutPictureEditText.text.toString() //you don't have to enter a note
                    }else {
                        Toast.makeText(requireActivity(), "Enter Picture's date", Toast.LENGTH_SHORT).show()
                     }
                    }else {
                        Toast.makeText(requireActivity(), "Enter Picture's name", Toast.LENGTH_SHORT).show()
                     }

            //CREATE DATABASE
            try {

                //create
               val db = requireActivity().openOrCreateDatabase("Data", MODE_PRIVATE,null)
               db.execSQL("CREATE TABLE IF NOT EXISTS data(id INTEGER PRIMARY KEY ,name VANCHAR ,date VANCHAR,note VANCHAR,image BLOB)")

               //fromBitmapToArray
               val byteOutputArrayStream = ByteArrayOutputStream()
               bitmap!!.compress(Bitmap.CompressFormat.PNG,100,byteOutputArrayStream)
               val bytes = byteOutputArrayStream.toByteArray()

               //statement
                val dbStatement = db.compileStatement("INSERT INTO data(name,date,note,image) VALUES(?,?,?,?)")

                dbStatement.bindString(1,picturesname)
                dbStatement.bindString(2,picturesdate)
                dbStatement.bindString(3,picturesnote)
                dbStatement.bindBlob  (4,bytes)
                dbStatement.execute()

                //when it's successful
                val action = TheFragmentUploadingSelectedPicturesDirections.actionTheFragmentUploadingSelectedPicturesToTheFragmentForMainScreenAndRecyclerView()
                Navigation.findNavController(view).navigate(action)
                Toast.makeText(requireActivity(), "Successful..!", Toast.LENGTH_SHORT).show()

           }catch (e : Exception) {
               println("DB EROR : ${e.localizedMessage}")
               e.printStackTrace()
           }

        }
    }

    private fun sizeMakerForPictures (bitmap : Bitmap) : Bitmap { //to make fixed pictures.

        if(bitmap.height > bitmap.width) {

            return bitmap.scale(700,1200,false)
        }else {

            return bitmap.scale(1050,750,false)
        }
    }
}