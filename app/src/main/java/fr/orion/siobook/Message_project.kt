package fr.orion.siobook

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_message_project.*
import kotlinx.android.synthetic.main.x_bottom_nav_project.*
import kotlinx.android.synthetic.main.z_toolbar_project.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList

@Suppress("PLUGIN_WARNING")
class Message_project : AppCompatActivity() {

    private val listmsg: ArrayList<Cmessage> = arrayListOf()
    private val listmsgID: ArrayList<String> = arrayListOf()

    private var selectedPhotoUri: ArrayList<ByteArray> = arrayListOf()
    private val listnothing: ArrayList<String> = arrayListOf()
    private var MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE =101
    private var MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE =101

    private var save =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_project)
        FULLSCREEN(window)
        bottomMenu()
        getMessage()
        getproject()
        onclickMSG()
        onclicksend()
        setupPermissionsRead()
        setupPermissionsWrite()
        getimage()
        //RV_message_project_listImgInMsg.layoutManager = LinearLayoutManager(this@Message_project, OrientationHelper.HORIZONTAL, false)
        CL_message_project_listImgInMsg.visibility =View.GONE
        deleteImg()
    }

    private fun deleteImg(){
        /*RV_message_project_listImgInMsg.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                selectedPhotoUri.removeAt(position)
                RV_message_project_listImgInMsg.adapter =MyAdapterImgInMsg(selectedPhotoUri)
                if (selectedPhotoUri.size ==0){
                    RV_message_project_listImgInMsg.visibility =View.GONE
                }
            }
        })*/
        RV_message_project_listImgInMsg.setOnItemClickListener { _, _, position, _ ->
            selectedPhotoUri.removeAt(position)
            listnothing.removeAt(position)
            RV_message_project_listImgInMsg.adapter =MyListAdapterImgInMsg(this@Message_project, listnothing = listnothing, listIMG = selectedPhotoUri)
            when {
                selectedPhotoUri.size ==0 -> CL_message_project_listImgInMsg.visibility =View.GONE
                selectedPhotoUri.size >1 -> TV_message_project_scroll.visibility =View.VISIBLE
            }
        }
    }

    private fun getimage(){
        CL_message_project_addS.setOnClickListener {
            val intent =Intent(Intent.ACTION_PICK)
            intent.type ="image/*"
            startActivityForResult(intent, 0)
        }
    }
    private fun compressBitmap(bitmap:Bitmap, quality:Int): ByteArray{

        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, bos)
        return bos.toByteArray()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data !=null){

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
            val img =compressBitmap(bitmap = bitmap, quality = 100)
            //val drawable = BitmapDrawable(resources, BitmapFactory.decodeByteArray(img, 0, img.size))
            selectedPhotoUri.add(img)
            listnothing.add("")
            //RV_message_project_listImgInMsg.adapter =MyAdapterImgInMsg(listIMG = selectedPhotoUri)
            RV_message_project_listImgInMsg.adapter =MyListAdapterImgInMsg(this@Message_project, listnothing = listnothing, listIMG = selectedPhotoUri)
            CL_message_project_listImgInMsg.visibility =View.VISIBLE
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
                ) { _, _ ->
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
    private fun setupPermissionsWrite() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(FLAG, "Permission to record denied")
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Permission to access the storage is required for this application.")
                        .setTitle("Permission required")

                builder.setPositiveButton("OK"
                ) { _, _ ->
                    Log.i(FLAG, "Clicked")
                    makeRequestWrite()
                }

                val dialog = builder.create()
                dialog.show()
            } else {
                makeRequestWrite()
            }
        }
    }
    private fun makeRequestWrite() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode){
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE ->
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(FLAG, "Permission has been denied by user")
                } else {
                    Log.i(FLAG, "Permission has been granted by user")
                }
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE ->
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(FLAG, "Permission has been denied by user")
                } else {
                    Log.i(FLAG, "Permission has been granted by user")
                }
        }
    }

    private fun onclicksend(){
        LL_message_project_send.setOnClickListener {
            val date = Calendar.getInstance()
            date.timeInMillis = Timestamp.now().seconds*1000L
            when {
                selectedPhotoUri.isEmpty() && ET_message_project_msg.text.toString().isNotEmpty() -> {
                    val msg =Cmessage(idM = "",idUIPSM = MYIDUIP, idPM = CIDPROJECT, idUSM = CUSER.idU, nameUIPSM = CUSER.fNameU, contentM = ET_message_project_msg.text.toString(), millisM = date.timeInMillis, urlimgUIP = CUSER.imgUrlU, typeMsg = SMS)
                    ADDSMS(message = msg)
                    ET_message_project_msg.setText("")
                }
                selectedPhotoUri.isNotEmpty() && ET_message_project_msg.text.toString().isNotEmpty() -> {
                    val msg =Cmessage(idM = "",idUIPSM = MYIDUIP, idPM = CIDPROJECT, idUSM = CUSER.idU, nameUIPSM = CUSER.fNameU, contentM = ET_message_project_msg.text.toString(), millisM = date.timeInMillis, urlimgUIP = CUSER.imgUrlU, typeMsg = SMS)
                    ADDSMS(message = msg)
                    ET_message_project_msg.setText("")

                    val msgWI =Cmessage(idM = "",idUIPSM = MYIDUIP, idPM = CIDPROJECT, idUSM = CUSER.idU, nameUIPSM = CUSER.fNameU, millisM = date.timeInMillis, urlimgUIP = CUSER.imgUrlU, typeMsg = MMS)
                    selectedPhotoUri.forEach {
                        ADDMMS(message = msgWI, selectedPhoto = it)
                    }
                    selectedPhotoUri.clear()
                    listnothing.clear()
                    CL_message_project_listImgInMsg.visibility =View.GONE
                }
                selectedPhotoUri.isNotEmpty() && ET_message_project_msg.text.toString().isEmpty() -> {
                    val msg =Cmessage(idM = "",idUIPSM = MYIDUIP, idPM = CIDPROJECT, idUSM = CUSER.idU, nameUIPSM = CUSER.fNameU, millisM = date.timeInMillis, urlimgUIP = CUSER.imgUrlU, typeMsg = MMS)
                    selectedPhotoUri.forEach {
                        ADDMMS(message = msg, selectedPhoto = it)
                    }
                    selectedPhotoUri.clear()
                    listnothing.clear()
                    CL_message_project_listImgInMsg.visibility =View.GONE
                }
            }
        }
    }

    private fun getMessage(){
        val dateNow = Calendar.getInstance()
        dateNow.timeInMillis = Timestamp.now().seconds*1000L
        val date = Calendar.getInstance()
        DB.collection(MESSAGE)
                .whereEqualTo(IDPM, CIDPROJECT)
                .orderBy(MILLISM, ASC)
                //.limit(100)
                .addSnapshotListener(EventListener<QuerySnapshot> {snapshot, error ->
                    if (error != null){
                        LOG("error message ::: $error")
                        return@EventListener
                    }

                    listmsg.clear()
                    //LISTLLM.clear()
                    listmsgID.clear()
                    for (message in snapshot!!){
                        date.timeInMillis =message.toObject(Cmessage::class.java).millisM
                        if (date.get(Calendar.MONTH) == dateNow.get(Calendar.MONTH)){
                            listmsg.add(message.toObject(Cmessage::class.java))
                            listmsgID.add(message.toObject(Cmessage::class.java).idM)
                        }
                    }
                    if (listmsg.size > 0){
                        LOG("Mise en place liste Message")
                        //RV_messageProject.layoutManager =LinearLayoutManager(this@Message_project, OrientationHelper.VERTICAL, true)
                        //RV_messageProject.adapter =MyAdapterMessage_Project(context = this@Message_project, listM = listmsg, rv = RV_messageProject)
                        LV_messageProject.adapter =MyListAdapterMessage_Project(context = this@Message_project, listID = listmsgID, listM = listmsg, lv = LV_messageProject)
                    }
                })
    }

    private fun onclickMSG(){
         /*RV_messageProject.addOnItemClickListener(object : OnItemClickListener {
             override fun onItemClicked(position: Int, view: View) {
                 val messages =listmsg[position]
                 if (messages.typeMsg == MMS){
                     LISTALLIMG.getImg(messages.idM)
                     startActivity(Intent(this@Message_project, FullScreenMMS::class.java).apply {})
                     overridePendingTransition(R.anim.hide_to_show, R.anim.show_to_hide)
                 }
             }
         })*/
        LV_messageProject.setOnItemClickListener { _, _, position, _ ->
            val messages =listmsg[position]
            if (messages.typeMsg == MMS){
                LISTALLIMG.getImg(messages.idM)
                startActivity(Intent(this@Message_project, FullScreenMMS::class.java).apply {})
                overridePendingTransition(R.anim.hide_to_show, R.anim.show_to_hide)
            }
        }
        LV_messageProject.setOnItemLongClickListener { _, _, position, _ ->
            val messages =listmsg[position]
            if (messages.typeMsg == MMS){
                LISTALLIMG.getImg(messages.idM)
                IV_message_project_IMGShowBS.setImageBitmap(IMGFULLSCREEN)
                CL_message_project_saveImg.visibility =View.VISIBLE
                save =true
            }
            true
        }
        TV_message_project_saveOK.setOnClickListener {
            if (saveFile()){
                Toast.makeText(this@Message_project, "Image save", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this@Message_project, "An error has occurred", Toast.LENGTH_SHORT).show()
            }
            CL_message_project_saveImg.visibility =View.GONE
            save =false
        }
        TV_message_project_saveCancel.setOnClickListener {
            CL_message_project_saveImg.visibility =View.GONE
            save =false
        }
    }

    fun saveFile(): Boolean{
        val myDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + File.separator + "project_manager/")
        myDir.mkdirs()
        val file = File (myDir, "IMG_$IDIMGFULLSCREEN.jpg")
        LOG("Chemin ::: $file")
        if (file.exists ()) file.delete()
        return try {
            val out = FileOutputStream(file)
            IMGFULLSCREEN.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
            MediaScannerConnection.scanFile(this@Message_project, arrayOf(file.absolutePath), null) { path, uri ->
                LOG("Scanned ::: $path")
                LOG("uri ::: $uri")
            }
            true

        } catch (e: Exception) {
            LOG("ERROR ::: $e")
            false
        }
    }

    private fun bottomMenu(){
        BTN_project.selectedItemId =R.id._project_message
        BTN_project.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id._project_back ->{
                    startActivity(Intent(this@Message_project, list_Project::class.java).apply {})
                    overridePendingTransition(R.anim.slide_bottom_to_center, R.anim.slide_center_to_top)
                    finish()
                }
                R.id._project_home_project ->{
                    startActivity(Intent(this@Message_project, Home_Project::class.java).apply {})
                    overridePendingTransition(R.anim.slide_left_to_center, R.anim.slide_center_to_right)
                    finish()
                }
                R.id._project_message ->{}
                R.id._project_settings ->{
                    startActivity(Intent(this@Message_project, Param_project::class.java).apply {})
                    overridePendingTransition(R.anim.slide_right_to_center, R.anim.slide_center_to_left)
                    finish()
                }
            }
            true
        }
    }

    private fun getproject(){
        DB.collection(PROJECT)
                .whereEqualTo(IDP, CIDPROJECT)
                .addSnapshotListener( EventListener<QuerySnapshot> { snapshots, error ->
                    if (error != null){
                        LOG("error project ::: $error")
                        startActivity(Intent(this@Message_project, list_Project::class.java).apply {})
                        overridePendingTransition(R.anim.slide_bottom_to_center, R.anim.slide_center_to_top)
                        finish()
                        return@EventListener
                    }
                    for (project in snapshots!!){
                        val _project =project.toObject(Cproject::class.java)
                        if (_project.idP == CIDPROJECT){
                            CPROJECT =_project
                            toolbar()
                            GONE(I_message_project_load)
                        }
                    }
                })
    }

    @SuppressLint("SetTextI18n")
    private fun toolbar(){
        TV_z_toolbar_project_name.text =CPROJECT.nameP
        TV_z_toolbar_project_progress_pourcentage.text ="${CPROJECT.ratioP}%"
        CPB_z_toolbar_project_progress_advanced.progress = CPROJECT.ratioP
        CPB_z_toolbar_project_progress_advanced.progressDrawable.setColorFilter(
                when(CPROJECT.state){
                    1-> ContextCompat.getColor(this@Message_project, R.color.colorGoodProject)
                    2->ContextCompat.getColor(this@Message_project, R.color.colorMidlleProject)
                    3->ContextCompat.getColor(this@Message_project, R.color.colorBadProject)
                    4-> ContextCompat.getColor(this@Message_project, R.color.colorPrimary)
                    else -> ContextCompat.getColor(this@Message_project, R.color.colorErrorProject)
                }, PorterDuff.Mode.SRC_IN)
    }
    override fun onBackPressed() {
        if (!save){
            startActivity(Intent(this@Message_project, Home_Project::class.java).apply {})
            overridePendingTransition(R.anim.slide_left_to_center, R.anim.slide_center_to_right)
            finish()
        }
        else{
            CL_message_project_saveImg.visibility =View.GONE
            save =false
        }
    }
}
