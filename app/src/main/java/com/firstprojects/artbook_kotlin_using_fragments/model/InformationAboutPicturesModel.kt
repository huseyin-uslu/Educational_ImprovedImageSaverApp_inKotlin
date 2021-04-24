package com.firstprojects.artbook_kotlin_using_fragments.model

import android.graphics.Bitmap
import java.io.Serializable

class InformationAboutPicturesModel (name : String?,
                                     note : String?,
                                     date : String?,
                                     image: Bitmap?) : Serializable
{
    var namesInArray = name
    private set
    var notesInArray = note
    private set
    var datesInArray = date
    private set
    var imagesInArray = image
    private set
}

