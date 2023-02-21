package com.example.app1

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondPage : AppCompatActivity() {
    private var picBackup: String? = null
    private var firstName: String? = null
    private var lastName: String? = null
    private var imgThumbnail: ImageView? = null
    private var tvFirstName: TextView? = null
    private var tvLastName: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)

        tvFirstName = findViewById<TextView>(R.id.tv_FirstNameArea)
        tvLastName = findViewById<TextView>(R.id.tv_LastNameArea)
        imgThumbnail = findViewById<ImageView>(R.id.img_Profile)

        val finalIntent = intent

        tvFirstName!!.text = finalIntent.getStringExtra("firstName")
        tvLastName!!.text = finalIntent.getStringExtra("LastName")



        val btnEdit = findViewById<Button>(R.id.btn_Restart)
        btnEdit.setOnClickListener {
            val main = Intent(this, MainActivity::class.java)
            startActivity(main)
        }

        val imagePath = finalIntent.getStringExtra("IMG_PATH")
        val thumbnailImage = BitmapFactory.decodeFile(imagePath)
        if (thumbnailImage != null) {
            imgThumbnail!!.setImageBitmap(thumbnailImage)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var currentIntent = intent

        picBackup = currentIntent.getStringExtra("IMG_PATH")
        firstName = tvFirstName!!.text.toString()
        lastName = tvLastName!!.text.toString()

        outState.putString("IMG_PATH", picBackup)
        outState.putString("firstName", firstName)
        outState.putString("lastName", lastName)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val imagePath = savedInstanceState.getString("IMG_PATH")
        val thumbnailImage = BitmapFactory.decodeFile(imagePath)

        imgThumbnail!!.setImageBitmap(thumbnailImage)
        tvFirstName!!.text = savedInstanceState.getString("firstName")
        tvLastName!!.text = savedInstanceState.getString("LastName")
    }
}