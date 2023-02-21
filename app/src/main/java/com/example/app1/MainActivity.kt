package com.example.app1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    private var imgProfileImage: Bitmap? = null
    private var imgThumbnail: ImageView? = null
    private var picBackup: String? = null

    private var displayIntent: Intent? = null

   // private lateinit var binding: ActivityMainBinding

    private var firstName: String? = null
    private var middleName: String? = null
    private var lastName: String? = null


    private var tv_FirstName: TextView? = null
    private var tv_MiddleName: TextView? = null
    private var tv_LastName: TextView? = null
    private var et_FirstName: EditText? = null
    private var et_MiddleName: EditText? = null
    private var et_LastName: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (displayIntent == null){
            displayIntent = Intent(this, SecondPage::class.java)
        }
        val btnCamera = findViewById<Button>(R.id.btn_Picture)
        btnCamera.setOnClickListener{
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                cameraLauncher.launch(cameraIntent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Camera was unable to open", Toast.LENGTH_SHORT).show()
            }
        }
        imgThumbnail = findViewById<ImageView>(R.id.img_Profile)

        val btnSubmit = findViewById<Button>(R.id.btn_Submit)
        btnSubmit.setOnClickListener {
            et_FirstName = findViewById<EditText>(R.id.et_FirstName)
            firstName = et_FirstName!!.text.toString()

            et_MiddleName = findViewById<EditText>(R.id.et_MiddleName)
            middleName = et_MiddleName!!.text.toString()

            et_LastName = findViewById<EditText>(R.id.et_LastName)
            lastName = et_LastName!!.text.toString()

            if (firstName.isNullOrBlank()) {
                Toast.makeText(this, "Invalid or Blank First Name, please fix!", Toast.LENGTH_SHORT).show()
            } else if (lastName.isNullOrBlank()) {
                Toast.makeText(this, "Invalid or Blank Last Name, please fix!", Toast.LENGTH_SHORT).show()
            } else {
                if ((!lastName.isNullOrBlank())){
                    displayIntent!!.putExtra("LastName", middleName)
                }
                displayIntent!!.putExtra("firstName", firstName)
                displayIntent!!.putExtra("LastName", lastName)
                startActivity(displayIntent)
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        try {
            val imagePath = savedInstanceState.getString("IMG_PATH")
            val thumbnailImage = BitmapFactory.decodeFile(imagePath)

            imgThumbnail!!.setImageBitmap(thumbnailImage)
            tv_FirstName!!.text = savedInstanceState.getString("firstName")
            tv_MiddleName!!.text = savedInstanceState.getString("MiddleName")
            tv_LastName!!.text = savedInstanceState.getString("LastName")
        } catch (e: Exception) {
            Toast.makeText(this, "Did not get save data", Toast.LENGTH_SHORT)
        }

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save Bundle
        outState.putString("firstName", firstName)
        outState.putString("MiddleName", middleName)
        outState.putString("LastName", lastName)
        picBackup = displayIntent?.getStringExtra("IMG_PATH")

        outState.putString("IMG_PATH", picBackup)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    private var cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val extras = result.data!!.extras
                imgProfileImage = extras!!["data"] as Bitmap?

                if (isExternalStorageWritable) {
                    val filePathString = writeImage(imgProfileImage)
                    displayIntent!!.putExtra("IMG_PATH", filePathString)
                    val imagePath = displayIntent!!.getStringExtra("IMG_PATH")
                    val thumbnailImage = BitmapFactory.decodeFile(imagePath)
                    imgThumbnail!!.setImageBitmap(thumbnailImage)

                } else {
                    Toast.makeText(this, "Try Again, no external storage!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }

    private val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }

    private fun writeImage(finalBitmap: Bitmap?): String {
        val directory = File("${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/saved_images")
        directory.mkdirs()

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "Thumbnail_$timeStamp.jpg"

        val file = File(directory, fileName)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            Toast.makeText(this, "Not able to get directory!", Toast.LENGTH_SHORT)
        }
        return file.absolutePath
    }
}