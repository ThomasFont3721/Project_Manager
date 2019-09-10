package fr.orion.siobook

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_add_user_in_project.*
import kotlinx.android.synthetic.main.x_bottom_nav_solo_back.*
import kotlinx.android.synthetic.main.z_toolbar_project.*

class add_user_in_project : AppCompatActivity() {

    private val listAllUser: ArrayList<Cuser> = arrayListOf()

    private val listnothing: ArrayList<String> = arrayListOf()
    private var listRole: ArrayList<String> = arrayListOf("Project manager","Developer","Disigner","Class creator","Function creator","Undefined")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user_in_project)
        FULLSCREEN(window)
        getproject()
        getuser()
        bottomnav()
        B_add_UIP_Add.setOnClickListener {
            val _emailU =ACTV_add_UIP_email.text.toString()

            if(TextUtils.isEmpty(_emailU.trim())){
                ACTV_add_UIP_email.error ="Enter a email for this contributor"
                return@setOnClickListener
            }
            var user =Cuser()
            var present =false
            listAllUser.forEach {
                if (_emailU == it.emailU){
                    present =true
                    user =it
                }
            }
            if (!present){
                ACTV_add_UIP_email.error ="Enter a valid email for this contributor"
                return@setOnClickListener
            }

            var already =false
            LISTUIP.forEach {
                if (_emailU == it.emailUIP){
                    already =true
                }
            }
            if (already){
                ACTV_add_UIP_email.error ="This contributor is already part of the project"
                return@setOnClickListener
            }

            ADDUIP(CUserInProject(idU = user.idU, idProject = CIDPROJECT, roleUIP = ACTV_add_UIP_role.text.toString(), nameUIP = user.nameU, fnameUIP = user.fNameU, emailUIP = _emailU, imgUip = user.imgUrlU))

            startActivity(Intent(this@add_user_in_project, Home_Project::class.java).apply {})
            overridePendingTransition(R.anim.slide_bottom_to_center, R.anim.slide_center_to_top)
            finish()
        }
    }
    private fun itemlist(){
        /*ACTV_add_UIP_name.setAdapter(MyAdapterNameList(this@add_user_in_project, email = listnothing, user = listAllUser))
        ACTV_add_UIP_name.threshold = 0
        ACTV_add_UIP_name.onFocusChangeListener = View.OnFocusChangeListener{ _, b ->
            if(b){ ACTV_add_UIP_name.showDropDown() }
        }*/
        ACTV_add_UIP_role.setAdapter(MyAdapterRoleList(this@add_user_in_project, name = listRole))
        ACTV_add_UIP_role.threshold = 0
        ACTV_add_UIP_role.onFocusChangeListener = View.OnFocusChangeListener{ _, b ->
            if(b){ ACTV_add_UIP_role.showDropDown() }
        }
    }

    private fun getproject(){
        DB.collection(PROJECT)
                .whereEqualTo(IDP, CIDPROJECT)
                .addSnapshotListener( EventListener<QuerySnapshot> { snapshots, error ->
                    if (error != null){
                        LOG("error project ::: $error")
                        startActivity(Intent(this@add_user_in_project, list_Project::class.java).apply {})
                        overridePendingTransition(R.anim.slide_bottom_to_center, R.anim.slide_center_to_top)
                        finish()
                        return@EventListener
                    }
                    for (project in snapshots!!){
                        val _project =project.toObject(Cproject::class.java)
                        if (_project.idP == CIDPROJECT){
                            CPROJECT =_project
                            toolbar()
                            getUserInProject(CIDPROJECT)
                            GONE(I_add_user_in_project_load)
                            LOG("List userInProject ::: $LISTUIP")
                        }
                    }
                })
    }
    private fun getUserInProject(id: String){
        DB.collection(USERINPROJECT)
                .whereEqualTo(IDPROJECT, id)
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
                        if (uip.toObject(CUserInProject::class.java).idU == CUSER.idU) MYIDUIP =uip.toObject(CUserInProject::class.java).idUIP
                    }
                    UPDATERATIOANDSTATEPROJECT()
                })
    }

    private fun getuser(){
        DB.collection(USER)
                .get()
                .addOnCompleteListener { read ->
                    if (read.isSuccessful) {
                        for (user in read.result!!) {
                            listnothing.add(user.toObject(Cuser::class.java).emailU)
                            listAllUser.add(user.toObject(Cuser::class.java))
                        }
                        itemlist()
                    } else {
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    @SuppressLint("SetTextI18n")
    private fun toolbar(){
        TV_z_toolbar_project_name.text =CPROJECT.nameP
        TV_z_toolbar_project_progress_pourcentage.text ="${CPROJECT.ratioP}%"
        CPB_z_toolbar_project_progress_advanced.progress = CPROJECT.ratioP
        CPB_z_toolbar_project_progress_advanced.progressDrawable.setColorFilter(
                when(CPROJECT.state){
                    1-> ContextCompat.getColor(this@add_user_in_project, R.color.colorGoodProject)
                    2-> ContextCompat.getColor(this@add_user_in_project, R.color.colorMidlleProject)
                    3-> ContextCompat.getColor(this@add_user_in_project, R.color.colorBadProject)
                    4-> ContextCompat.getColor(this@add_user_in_project, R.color.colorPrimary)
                    else -> ContextCompat.getColor(this@add_user_in_project, R.color.colorErrorProject)
                }, PorterDuff.Mode.SRC_IN)
    }

    private fun bottomnav(){
        BTN_solo_back.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id._solo_back ->{
                    startActivity(Intent(this@add_user_in_project, Home_Project::class.java).apply {})
                    overridePendingTransition(R.anim.slide_bottom_to_center, R.anim.slide_center_to_top)
                    finish()
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@add_user_in_project, Home_Project::class.java).apply {})
        overridePendingTransition(R.anim.slide_bottom_to_center, R.anim.slide_center_to_top)
        finish()
    }
}
