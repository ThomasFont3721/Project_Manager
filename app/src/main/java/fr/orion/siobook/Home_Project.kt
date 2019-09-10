package fr.orion.siobook

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_home_project.*
import kotlinx.android.synthetic.main.x_bottom_nav_project.*
import kotlinx.android.synthetic.main.y_more_project.*
import kotlinx.android.synthetic.main.z_toolbar_project.*

@Suppress("UNCHECKED_CAST")
class Home_Project : AppCompatActivity() {

    private var deploiSearch =false
    private var deploiMore =false
    private var research: ArrayList<java.util.ArrayList<out Any>> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_project)
        FULLSCREEN(window)
        getproject()
        search()
        bottomMenu()
        itemclick()
        GONE(TL_y_more_project_SA)
    }
    private fun itemclick(){
        LV_home_project_listUser.setOnItemClickListener { _, _, position, _ ->
            CIDUIP = if (deploiSearch && !ET_HomeProject_search.text.toString().isBlank() && ET_HomeProject_search.text.toString().isNotEmpty()){ research[0][position].toString() }
            else {LISTIDUIP[position]}
            LOG("ID UIP ::: $CIDUIP")
            startActivity(Intent(this@Home_Project, task_list_user_in_project::class.java).apply {})
            overridePendingTransition(R.anim.slide_top_to_center, R.anim.slide_center_to_bottom)
            finish()
        }
    }
    private fun getproject(){
        DB.collection(PROJECT)
                .whereEqualTo(IDP, CIDPROJECT)
                .addSnapshotListener( EventListener<QuerySnapshot> { snapshots, error ->
                    if (error != null){
                        LOG("error project ::: $error")
                        startActivity(Intent(this@Home_Project, list_Project::class.java).apply {})
                        overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
                        finish()
                        return@EventListener
                    }
                    for (project in snapshots!!){
                        val _project =project.toObject(Cproject::class.java)
                        if (_project.idP == CIDPROJECT){
                            CPROJECT =_project
                            toolbar()
                            getUserInProject()
                            I_home_project_load.visibility =View.GONE
                            LOG("List userInProject ::: $LISTUIP")
                        }
                    }
                })
    }
    private fun getUserInProject() {
        Log.i(FLAG, CIDPROJECT)
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
                        if (uip.toObject(CUserInProject::class.java).idU == CUSER.idU) MYIDUIP =uip.toObject(CUserInProject::class.java).idUIP
                    }
                    if (LISTUIP.size > 0){
                        LV_home_project_listUser.adapter = MyListAdapterHome_Project(this@Home_Project, listID = LISTIDUIP, listUIP = LISTUIP, index = 0)
                        Log.i(FLAG, "LISTUIP.size ---> ${LISTUIP.size} ::: $LISTIDUIP")
                        UPDATERATIOANDSTATEPROJECT()
                    }
                    else{
                        Toast.makeText(this@Home_Project, "Error Load User", Toast.LENGTH_SHORT).show()
                    }

                })
    }

    private fun search(){
        ET_HomeProject_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable){}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int){}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty() && s.toString().isBlank()){
                    LV_home_project_listUser.adapter = MyListAdapterHome_Project(this@Home_Project, listID = LISTIDUIP, listUIP = LISTUIP, index = 1)
                }
                else{
                    research =research(s.toString())
                    if (research.size >0){
                        LV_home_project_listUser.adapter = MyListAdapterHome_Project(this@Home_Project, listID = research[0] as ArrayList<String>, listUIP = research[1] as ArrayList<CUserInProject>, index = 1)
                    }
                }
            }
        })
    }
    private fun research(enter: String): ArrayList<java.util.ArrayList<out Any>> {
        val listID :ArrayList<Any> = arrayListOf()
        val listUIP :ArrayList<Any> = arrayListOf()
        val string =enter.toUpperCase()

        if (string.length ==1){
            LISTUIP.forEach { UIP->
                val name ="${UIP.nameUIP} ${UIP.fnameUIP}"
                if (name.toUpperCase().toCharArray().contains(string.toCharArray()[0].toUpperCase())){
                    if (!listUIP.contains(UIP)){
                        listID.add(UIP.idUIP)
                        listUIP.add(UIP)
                    }
                }
            }
        }
        else{
            LOG("Enter :: ${string.toUpperCase()}")
            LISTUIP.forEach { UIP->
                var j=0
                val name ="${UIP.fnameUIP} ${UIP.nameUIP}"
                while(j< name.toCharArray().size){
                    string.toUpperCase().toCharArray().forEach {
                        if (name.toUpperCase().toCharArray()[j] == it.toUpperCase()){
                            LOG("$j = ${name.toUpperCase().toCharArray()[j]} ---> ${it.toUpperCase()}")
                            var i =j
                            var text =""
                            var size =0
                            while(size<string.length && i<name.toCharArray().size){
                                LOG("lettre add to text ::: ${name.toUpperCase().toCharArray()[i]}")
                                text +=name.toUpperCase().toCharArray()[i]
                                i++
                                size++
                            }
                            LOG("name :: ${name} ///  text === $text")
                            if (text == string){
                                if (!listUIP.contains(UIP)){
                                    LOG("uip : ${UIP.idUIP} add to list")
                                    listID.add(UIP.idUIP)
                                    listUIP.add(UIP)
                                }
                            }
                        }
                    }
                    j++
                }
            }
        }
        return arrayListOf(listID,listUIP)
    }

    @SuppressLint("SetTextI18n")
    private fun toolbar(){
        TV_z_toolbar_project_name.text =CPROJECT.nameP
        TV_z_toolbar_project_progress_pourcentage.text ="${CPROJECT.ratioP}%"
        CPB_z_toolbar_project_progress_advanced.progress = CPROJECT.ratioP
        CPB_z_toolbar_project_progress_advanced.progressDrawable.setColorFilter(
                when(CPROJECT.state){
                    1-> ContextCompat.getColor(this@Home_Project, R.color.colorGoodProject)
                    2->ContextCompat.getColor(this@Home_Project, R.color.colorMidlleProject)
                    3->ContextCompat.getColor(this@Home_Project, R.color.colorBadProject)
                    4-> ContextCompat.getColor(this@Home_Project, R.color.colorPrimary)
                    else -> ContextCompat.getColor(this@Home_Project, R.color.colorErrorProject)
                }, PorterDuff.Mode.SRC_IN)
    }
    private fun bottomMenu(){
        BTN_project.selectedItemId =R.id._project_home_project
        BTN_project.menu.findItem(R.id._project_more).isVisible =true
        BTN_project.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id._project_back ->{
                    startActivity(Intent(this@Home_Project, list_Project::class.java).apply {})
                    overridePendingTransition(R.anim.slide_bottom_to_center, R.anim.slide_center_to_top)
                    finish()
                }
                R.id._project_home_project ->{
                    if (TL_y_more_project_SA.visibility ==View.VISIBLE){
                        GONE(TL_y_more_project_SA)
                        GONE(LL_search_homeProject)
                    }
                }
                R.id._project_message ->{
                    startActivity(Intent(this@Home_Project, Message_project::class.java).apply {})
                    overridePendingTransition(R.anim.slide_right_to_center, R.anim.slide_center_to_left)
                    finish()
                }
                R.id._project_settings ->{
                    startActivity(Intent(this@Home_Project, Param_project::class.java).apply {})
                    overridePendingTransition(R.anim.slide_right_to_center, R.anim.slide_center_to_left)
                    finish()
                }
                R.id._project_more ->{
                    if (deploiMore){
                        deploiMore =false
                        GONE(TL_y_more_project_SA)
                        GONE(LL_search_homeProject)
                        BTN_project.selectedItemId =R.id._project_home_project
                    }
                    else{
                        deploiMore =true
                        VISIBLE(TL_y_more_project_SA)
                    }
                }
            }
            true
        }
        TR_y_more_project_search.setOnClickListener{
            VISIBLE(LL_search_homeProject)
            deploiSearch =true
        }
        TR_y_more_project_add_uip.setOnClickListener {
            startActivity(Intent(this@Home_Project, add_user_in_project::class.java).apply {})
            overridePendingTransition(R.anim.slide_top_to_center, R.anim.slide_center_to_bottom)
            finish()
        }
        IV_home_project_hide_search.setOnClickListener {
            GONE(LL_search_homeProject)
        }
    }

    override fun onBackPressed() {
        if (deploiSearch || deploiMore){
            deploiSearch =false
            deploiMore =false
            BTN_project.selectedItemId =R.id._project_home_project
            LV_home_project_listUser.adapter = MyListAdapterHome_Project(this@Home_Project, listID = LISTIDUIP, listUIP = LISTUIP, index = 1)
            val anim = AnimationUtils.loadAnimation(this@Home_Project, R.anim.show_to_hide)
            LL_search_homeProject.startAnimation(anim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(arg0: Animation) {}
                override fun onAnimationRepeat(arg0: Animation) {}
                override fun onAnimationEnd(arg0: Animation) {GONE(LL_search_homeProject); GONE(TL_y_more_project_SA)}
            })
        }
        else{
            startActivity(Intent(this@Home_Project, list_Project::class.java).apply {})
            overridePendingTransition(R.anim.slide_bottom_to_center, R.anim.slide_center_to_top)
            finish()
        }
    }
}
