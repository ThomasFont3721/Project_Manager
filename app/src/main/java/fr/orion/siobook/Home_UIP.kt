package fr.orion.siobook

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.x_bottom_nav_uip.*

class Home_UIP : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_uip)
        FULLSCREEN(window)
        getuip()
        bottomnav()
    }

    private fun getuip(){
        DB.collection(USERINPROJECT)
                .whereEqualTo(IDPROJECT, CIDPROJECT)
                .addSnapshotListener(EventListener<QuerySnapshot> { snapshots, e ->
                    if (e != null) {
                        LOG("error uip ::: $e")
                        return@EventListener
                    }
                    LISTUIP.clear()
                    LISTIDUIP.clear()
                    for (uip in snapshots!!) {
                        LISTUIP.add(uip.toObject(CUserInProject::class.java))
                        LISTIDUIP.add(uip.toObject(CUserInProject::class.java).idUIP)
                        if (uip.toObject(CUserInProject::class.java).idUIP == CIDUIP) CUSERINPROJECT =uip.toObject(CUserInProject::class.java); getimguip()
                    }

                    if (LISTUIP.size > 0){
                        LOG("LISTUIP.size ---> ${LISTUIP.size} ::: $LISTIDUIP")
                        UPDATERATIOANDSTATEPROJECT()
                    }
                    else{
                        Toast.makeText(this@Home_UIP, "Error Load User", Toast.LENGTH_SHORT).show()
                    }

                })
    }
    private fun getimguip(){
        try {
            if (LISTALLIMG.size > 0){
                var bool =false
                var i =0
                var find =0
                LISTALLIMG.forEach {
                    if (it[0] == CIDUIP){
                        bool =true
                        find =i
                    }
                    i++
                }
                if (!bool){
                    bool =true
                    val ONE_MEGABYTE: Long = 1024 * 1024
                    STORAGE.reference.child("img_user/${CUSERINPROJECT.idU}").getBytes(ONE_MEGABYTE)
                            .addOnSuccessListener {

                                LISTALLIMG.add(arrayListOf(CIDUIP, BitmapFactory.decodeByteArray(it, 0, it.size)))

                                BTN_uip.menu.findItem(R.id._uip_home).icon =BitmapDrawable(resources, BitmapFactory.decodeByteArray(it, 0, it.size))
                            }
                            .addOnFailureListener {
                                LOG("img uip error ::: $it")
                                bool =false
                            }
                }
                else{
                    BTN_uip.menu.findItem(R.id._uip_home).icon =BitmapDrawable(resources, LISTALLIMG[find][1] as Bitmap)
                }

            }
            else{
                val ONE_MEGABYTE: Long = 1024 * 1024
                STORAGE.reference.child("img_user/${CUSERINPROJECT.idU}").getBytes(ONE_MEGABYTE)
                        .addOnSuccessListener {

                            LISTALLIMG.add(arrayListOf(CIDUIP, BitmapFactory.decodeByteArray(it, 0, it.size)))

                            BTN_uip.menu.findItem(R.id._uip_home).icon =BitmapDrawable(resources, BitmapFactory.decodeByteArray(it, 0, it.size))
                        }
                        .addOnFailureListener {
                            LOG("img uip error ::: $it")
                        }
            }


        }
        catch (e: Exception){
            LOG("error download img uip ::: $e")
        }
    }
    private fun bottomnav(){
        BTN_uip.selectedItemId =R.id._uip_home
        BTN_uip.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id._uip_back ->{
                    startActivity(Intent(this@Home_UIP, Home_Project::class.java).apply {})
                    overridePendingTransition(R.anim.slide_bottom_to_center, R.anim.slide_center_to_top)
                    finish()
                }
                R.id._uip_task_list ->{
                    startActivity(Intent(this@Home_UIP, task_list_user_in_project::class.java).apply {})
                    overridePendingTransition(R.anim.slide_right_to_center, R.anim.slide_center_to_left)
                    finish()
                }
                R.id._uip_home ->{}
                R.id._uip_settings ->{
                    startActivity(Intent(this@Home_UIP, Param_uip::class.java).apply {})
                    overridePendingTransition(R.anim.slide_right_to_center, R.anim.slide_center_to_left)
                    finish()
                }
            }
            true
        }
    }
}
