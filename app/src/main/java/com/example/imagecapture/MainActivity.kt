package com.example.imagecapture

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.ByteArrayOutputStream
import java.security.Permission
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    lateinit var tvGallary: TextView
    lateinit var tvCamera: TextView
    lateinit var captureimage: Bitmap
    lateinit var imagedata: String
    lateinit var imageuri: Uri
    var PICKETOGALLARY_CODE=100
    var PICKTOCAMERA_CODE=200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<View>(R.id.bottomSheet_Layout)
        bottomSheetBehavior = BottomSheetBehavior.from(view)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN


        imageView = findViewById(R.id.image_view)
        tvGallary = findViewById(R.id.tv_Gallary)
        tvCamera = findViewById(R.id.tv_Camera)
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                PICKTOCAMERA_CODE
            )
        }
        tvGallary.setOnClickListener {
            var intenGallary = Intent(Intent.ACTION_GET_CONTENT)
            intenGallary.setType("image/*")
            startActivityForResult(intenGallary, PICKETOGALLARY_CODE)
        }
        tvCamera.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, PICKTOCAMERA_CODE)
        }

        imageView.setOnClickListener {

            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Camera Request
        if (resultCode == RESULT_OK && requestCode == PICKTOCAMERA_CODE) {
            captureimage = data!!.extras!!.get("data") as Bitmap


            imageView.setImageBitmap(captureimage)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        } else if (resultCode == RESULT_CANCELED)
          {

          }
         // Gallary Request
         else if (resultCode== RESULT_OK && requestCode== PICKETOGALLARY_CODE)
         {
        if (data != null) {
            imageuri = data.data!!
            var captureimages = MediaStore.Images.Media.getBitmap(contentResolver, imageuri)
            imageView.setImageBitmap(captureimages)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        }

    }

    }

    fun imageTostring(bitmap: Bitmap): String {
        var outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        var imagebyte: ByteArray = outputStream.toByteArray()
        var encodedimage: String = Base64.encodeToString(imagebyte, Base64.DEFAULT)
        return encodedimage
    }

}