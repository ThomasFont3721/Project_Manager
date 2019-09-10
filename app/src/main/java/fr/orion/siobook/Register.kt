package fr.orion.siobook

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*
import android.provider.MediaStore.Images
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream


@SuppressLint("ByteOrderMark")
class Register : AppCompatActivity() {

    private var selectedPhotoUri: ByteArray? =null
    private var MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE =101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        FULLSCREEN(window)

        setupPermissionsRead()

        B_Register_register.setOnClickListener {
            val _name =ET_Register_name.text.toString()
            val _fname =ET_Register_fname.text.toString()
            val _email =ET_Register_email.text.toString()
            val _password =ET_Register_password.text.toString()
            val _repassword =ET_Register_repassword.text.toString()


            if (selectedPhotoUri == null){
                Toast.makeText(this@Register, "Choose a photo", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(_name.trim())) {

                this.ET_Register_name.error ="Enter name"//--------
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(_fname.trim())) {

                this.ET_Register_fname.error ="Enter firstname"//--------
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(_email.trim())) {

                this.ET_Register_email.error ="Enter email"//--------
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {

                this.ET_Register_email.error ="Enter valid email"//--------
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(_password.trim())) {

                this.ET_Register_password.error ="Enter password"//--------
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(_repassword.trim())) {

                this.ET_Register_repassword.error ="Enter password"//--------
                return@setOnClickListener
            }
            if (_password != _repassword) {

                this.ET_Register_repassword.error ="Passwords are different"//--------
                return@setOnClickListener
            }
            if (VERIFUPPER(_password)) {
                this.ET_Register_password.error ="The password must contain a capital letter"//--------
                return@setOnClickListener
            }
            if (VERIFLOWER(_password)) {
                this.ET_Register_password.error ="The password must contain a lowercase letter"//--------
                return@setOnClickListener
            }
            if (VERIFNUMBER(_password)) {
                this.ET_Register_password.error ="The password must contain a number"//--------
                return@setOnClickListener
            }
            if (_password.length < 8) {
                this.ET_Register_password.error ="The password must contain more than 8 characters"//--------
                return@setOnClickListener
            }

            if (verif()){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        CREATEUSER(Cuser("", ET_Register_email.text.toString(), ET_Register_name.text.toString(), ET_Register_fname.text.toString()),  ET_Register_password.text.toString(), selectedPhotoUri, Register(), this@Register)
                        IMGBITMAP =BitmapFactory.decodeByteArray(selectedPhotoUri, 0, selectedPhotoUri!!.size)
                        startActivity(Intent(this@Register, Load::class.java).apply {})
                    overridePendingTransition(R.anim.hide_to_show, R.anim.show_to_hide)
                        finish()
                } else { Log.i(FLAG, "permission denied read") }
            } else { Log.i(FLAG, "textbox empty") }
        }

        var boolean = false
        IV_Register_showhide.setOnClickListener {
            if (boolean){
                ET_Register_password.transformationMethod = PasswordTransformationMethod.getInstance()
                ET_Register_repassword.transformationMethod = PasswordTransformationMethod.getInstance()
                boolean = !boolean
            }
            else{
                ET_Register_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ET_Register_repassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                boolean = !boolean
            }
        }

        IV_Register_photo.setOnClickListener {
            val intent =Intent(Intent.ACTION_PICK)
            intent.type ="image/*"
            startActivityForResult(intent, 0)
        }

        IV_Register_back.setOnClickListener {
            startActivity(Intent(this@Register, Register_X_Login::class.java).apply {})
            overridePendingTransition(R.anim.slide_right_to_center, R.anim.slide_center_to_left)
            finish()
        }
    }

    private fun compressBitmap(bitmap:Bitmap, quality:Int):ByteArray{
        // Initialize a new ByteArrayStream
        val stream = ByteArrayOutputStream()

        // Compress the bitmap with JPEG format and quality 50%
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream)

        val byteArray = stream.toByteArray()

        // Finally, return the compressed bitmap
        return byteArray
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == AppCompatActivity.RESULT_OK && data !=null){

            val bitmap =Images.Media.getBitmap(contentResolver, data.data)
            val img =compressBitmap(bitmap = bitmap, quality = 10)
            IV_Register_photo.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.size))
            selectedPhotoUri = img
        }
    }

    private fun setupPermissionsRead() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(FLAG, "Permission to record denied")
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Permission to access the storage is required for this application.")
                                .setTitle("Permission required")

                                builder.setPositiveButton("OK"
                                ) { dialog, id ->
                            Log.i(FLAG, "Clicked")
                            makeRequestRead()
                        }

                        val dialog = builder.create()
                dialog.show()
            } else {
                makeRequestRead()
            }
        }
    }

    private fun makeRequestRead() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode){
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE ->
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                Log.i(FLAG, "Permission has been denied by user")
            } else {
                Log.i(FLAG, "Permission has been granted by user")
            }
        }
    }

    private fun verif(): Boolean{
        return ET_Register_password.text.toString() == ET_Register_repassword.text.toString() && ET_Register_email.text.toString() !="" && ET_Register_password.text.toString() !="" && ET_Register_name.text.toString() !="" && ET_Register_fname.text.toString() !=""
    }

    override fun onBackPressed() {
        startActivity(Intent(this@Register, Register_X_Login::class.java).apply {})
        overridePendingTransition(R.anim.slide_right_to_center, R.anim.slide_center_to_left)
        finish()
    }
}
