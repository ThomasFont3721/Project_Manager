package fr.orion.siobook

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_task_list_user_in_project.*
import kotlinx.android.synthetic.main.w_long_click_task.*
import kotlinx.android.synthetic.main.x_bottom_nav_uip.*
import kotlinx.android.synthetic.main.z_toolbar_project.*

@SuppressLint("SetTextI18n")
class task_list_user_in_project : AppCompatActivity() {

    private var longclick =false
    private var posi =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list_user_in_project)
        FULLSCREEN(window)
        getuip()
        bottomnav()
        clickitem()
        getproject()
        clickSaveAndClose()
        addTask()
    }

    private fun addTask(){
        IV_task_list_uip_add.setOnClickListener {
            if (!ET_task_list_text_add.text.isNullOrEmpty()){
                CUSERINPROJECT.listbooltaskUIP.add(false)
                CUSERINPROJECT.listInttaskUIP.add(1)
                CUSERINPROJECT.listtaskUIP.add(ET_task_list_text_add.text.toString())
                ET_task_list_text_add.setText("")//alors probleme avec la list view qui fait au click 1 devient 2 2->3 3->1 !!logique!! cette ligne ne fonctionne pas et probleme avec listView et delete
                UPDATEUIP()
            }
        }
    }

    private fun clickitem(){
        LV_task_uip.setOnItemClickListener { _, _, position, _ ->
            CUSERINPROJECT.listbooltaskUIP[position] = !CUSERINPROJECT.listbooltaskUIP[position]
            if (CUSERINPROJECT.listbooltaskUIP[position]) CUSERINPROJECT.listInttaskUIP[position] =4
            else CUSERINPROJECT.listInttaskUIP[position] =1
            UPDATEUIP()
        }
        LV_task_uip.setOnItemLongClickListener { _, _, position, _ ->
            posi =position
            VISIBLE(objects = I_task_uip_w_long_clisk_task)
            TV_w_long_click_task_name.text =CUSERINPROJECT.listtaskUIP[position]
            CB_w_long_clisk_task_check.isChecked =CUSERINPROJECT.listbooltaskUIP[position]
            RG_w_long_click_task_choose_color.check(when(CUSERINPROJECT.listInttaskUIP[position]){
                1->R.id.RB_w_long_click_task_Good
                2->R.id.RB_w_long_click_task_Middle
                3->R.id.RB_w_long_click_task_Bad
                else->{R.id.RB_w_long_click_task_End}
            })
            longclick =true
            true
        }
    }

    private fun clickSaveAndClose(){
        B_w_long_clisk_task_save_change.setOnClickListener {
        }
        IB_w_long_click_task_close.setOnClickListener {
            GONE(objects = I_task_uip_w_long_clisk_task)
            longclick =false
        }
        IB_w_long_click_task_delete.setOnClickListener {
            CUSERINPROJECT.listInttaskUIP.removeAt(posi)
            CUSERINPROJECT.listbooltaskUIP.removeAt(posi)
            CUSERINPROJECT.listtaskUIP.removeAt(posi)
            UPDATEUIP()
        }
    }

    private fun getproject(){
        DB.collection(PROJECT)
                .whereEqualTo(IDP, CIDPROJECT)
                .addSnapshotListener( EventListener<QuerySnapshot> { snapshots, error ->
                    if (error != null){
                        LOG("error project ::: $error")
                        startActivity(Intent(this@task_list_user_in_project, list_Project::class.java).apply {})
                        overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
                        finish()
                        return@EventListener
                    }
                    for (project in snapshots!!){
                        val _project =project.toObject(Cproject::class.java)
                        if (_project.idP == CIDPROJECT){
                            CPROJECT =_project
                        }
                    }
                })
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
                        if (uip.toObject(CUserInProject::class.java).idUIP == CIDUIP) {
                            CUSERINPROJECT =uip.toObject(CUserInProject::class.java)
                            I_task_uip_load.visibility =View.GONE
                            TV_z_toolbar_project_name.text = CUSERINPROJECT.nameUIP
                            TV_z_toolbar_project_progress_pourcentage.text = "${CUSERINPROJECT.ratioUIP}%"
                            CPB_z_toolbar_project_progress_advanced.progress = CUSERINPROJECT.ratioUIP
                            CPB_z_toolbar_project_progress_advanced.progressDrawable.setColorFilter(
                                    when(CUSERINPROJECT.stateUIP){
                                        1-> ContextCompat.getColor(this@task_list_user_in_project, R.color.colorGoodProject)
                                        2->ContextCompat.getColor(this@task_list_user_in_project, R.color.colorMidlleProject)
                                        3->ContextCompat.getColor(this@task_list_user_in_project, R.color.colorBadProject)
                                        4->ContextCompat.getColor(this@task_list_user_in_project, R.color.colorEndProject)
                                        else -> ContextCompat.getColor(this@task_list_user_in_project, R.color.colorErrorProject)
                                    }, PorterDuff.Mode.SRC_IN)
                            listview()
                        }
                    }
                    if (LISTUIP.size > 0) UPDATERATIOANDSTATEPROJECT()
                    else Toast.makeText(this@task_list_user_in_project, "Error Load User", Toast.LENGTH_SHORT).show()

                })
    }

    private fun listview(){
        LV_task_uip.adapter =MyListAdapterTaskUIP(context = this@task_list_user_in_project, listbool = CUSERINPROJECT.listbooltaskUIP, listtask = CUSERINPROJECT.listtaskUIP, listint = CUSERINPROJECT.listInttaskUIP)
    }

    private fun bottomnav(){
        BTN_uip.selectedItemId =R.id._uip_task_list
        BTN_uip.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id._uip_back ->{
                    startActivity(Intent(this@task_list_user_in_project, Home_Project::class.java).apply {})
                    overridePendingTransition(R.anim.slide_bottom_to_center, R.anim.slide_center_to_top)
                    finish()
                }
                R.id._uip_task_list ->{}
                R.id._uip_home ->{
                    startActivity(Intent(this@task_list_user_in_project, Home_UIP::class.java).apply {})
                    overridePendingTransition(R.anim.slide_right_to_center, R.anim.slide_center_to_left)
                    finish()
                }
                R.id._uip_settings ->{
                    startActivity(Intent(this@task_list_user_in_project, Param_uip::class.java).apply {})
                    overridePendingTransition(R.anim.slide_right_to_center, R.anim.slide_center_to_left)
                    finish()
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        if (longclick){
            GONE(objects = I_task_uip_w_long_clisk_task)
            longclick =false
        }
        else {
            startActivity(Intent(this@task_list_user_in_project, Home_Project::class.java).apply {})
            overridePendingTransition(R.anim.slide_bottom_to_center, R.anim.slide_center_to_top)
            finish()
        }
    }
}
