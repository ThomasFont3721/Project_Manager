package fr.orion.siobook

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_list_project.*
import kotlinx.android.synthetic.main.z_menu_home.*
import kotlinx.android.synthetic.main.z_toolbar.*
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity


@Suppress("UNCHECKED_CAST")
class list_Project : AppCompatActivity() {

    private val listProject: ArrayList<Cproject> = arrayListOf()
    private val listID: ArrayList<String> = arrayListOf()
    private var deploi =false
    private var research: ArrayList<java.util.ArrayList<out Any>> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_project)
        FULLSCREEN(window)
        initialise()
        toolbar()
        search()
        DB.collection(PROJECT)
                .whereArrayContains(MEMBERP,CUSER.idU)
                /*.orderBy(MILLISP, DESC)*/
            .addSnapshotListener(EventListener<QuerySnapshot> { snapshots, e ->
                if (e != null) {
                    return@EventListener
                }
                listProject.clear()
                listID.clear()
                for (project in snapshots!!) {
                    project.toObject(Cproject::class.java).memberP.forEach {
                        if (it == CUSER.idU){
                            listProject.add(project.toObject(Cproject::class.java))
                            listID.add(project.toObject(Cproject::class.java).idP)
                        }
                    }
                }
                /*for (dc in snapshots.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> actualise(dc.document.id, 1)
                        DocumentChange.Type.MODIFIED -> actualise(dc.document.id,2)
                        DocumentChange.Type.REMOVED -> actualise(dc.document.id, 3)
                    }
                }*/
                if (listProject.size > 0){
                    LV_list_project_listemsg.adapter = MyListAdapter(this@list_Project, listID =listID, listproject =listProject, index=0)
                    LL_list_project_No_project.visibility =View.GONE
                }
                else{
                    LV_list_project_listemsg.visibility = View.GONE
                }
            })

        LV_list_project_listemsg.setOnItemClickListener { _, _, position, _ ->
            CIDPROJECT = if (deploi && !ET_search_listProject.text.toString().isBlank() && ET_search_listProject.text.toString().isNotEmpty()){research[0][position].toString()}
                         else {listID[position]}
            startActivity(Intent(this@list_Project, Home_Project::class.java).apply {})
            overridePendingTransition(R.anim.slide_top_to_center, R.anim.slide_center_to_bottom)
            finish()
        }

        LL_list_project_No_project.setOnClickListener {
            startActivity(Intent(this@list_Project, add_Project::class.java).apply {})
            overridePendingTransition(R.anim.slide_top_to_center, R.anim.slide_center_to_bottom)
            finish()
        }
    }

    private fun search(){

        ET_search_listProject.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable){}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int){}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty() && s.toString().isNullOrBlank()){
                    LV_list_project_listemsg.adapter = MyListAdapter(this@list_Project, listID =listID, listproject =listProject, index=1)
                }
                else{
                    research =research(s.toString())
                    if (research.size >0){
                        LV_list_project_listemsg.adapter = MyListAdapter(this@list_Project, listID =research[0] as ArrayList<String>, listproject =research[1] as ArrayList<Cproject>, index=1)
                    }
                }
            }
        })
    }

    private fun research(enter: String): ArrayList<java.util.ArrayList<out Any>> {
        val listeName :ArrayList<Any> = arrayListOf()
        val listeProject :ArrayList<Any> = arrayListOf()
        val string =enter.toUpperCase()

        if (string.length ==1){
            listProject.forEach { project->
                if (project.nameP.toUpperCase().toCharArray().contains(string.toCharArray()[0])){
                    if (!listeProject.contains(project)){
                        listeName.add(project.idP)
                        listeProject.add(project)
                    }
                }
            }
        }
        else{
            LOG("Enter :: $string")
            listProject.forEach { project->
                var j=0
                while(j< project.nameP.toCharArray().size){
                    string.toCharArray().forEach {
                        if (project.nameP.toUpperCase().toCharArray()[j] == it){
                            LOG("$j = ${project.nameP.toUpperCase().toCharArray()[j]} ---> $it")
                            var i =j
                            var text =""
                            var size =0
                            while(size<string.length && i<project.nameP.toCharArray().size){
                                LOG("lettre add to text ::: ${project.nameP.toUpperCase().toCharArray()[i]}")
                                text +=project.nameP.toUpperCase().toCharArray()[i]
                                i++
                                size++
                            }
                            LOG("project :: ${project.nameP} ///  text === $text")
                            if (text == string){
                                if (!listeProject.contains(project)){
                                    LOG("project : ${project.nameP} add to list")
                                    listeName.add(project.nameP)
                                    listeProject.add(project)
                                }
                            }
                        }
                    }
                    j++
                }
            }
        }
        return arrayListOf(listeName,listeProject)
    }


    private fun initialise(){
        IV_menu_home_listproject.setImageResource(R.drawable.ic_list_white_24dp)
        IV_menu_home_img_account.setImageBitmap(IMGBITMAP)
        bottomMenu()
    }
    private fun bottomMenu(){
        LL_menu_home_listproject.setOnClickListener {
            startActivity(Intent(this@list_Project, list_Project::class.java).apply {})
            overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
            finish()
        }
        LL_menu_home_account.setOnClickListener {
            startActivity(Intent(this@list_Project, Account::class.java).apply {})
            overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
            finish()
        }
    }
    private fun toolbar(){
        IV_z_toolbar_modify.visibility =View.GONE
        IV_z_toolbar_log_out.visibility =View.GONE
        IV_z_toolbar_add.setOnClickListener {
            startActivity(Intent(this@list_Project, add_Project::class.java).apply {})
            overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
            finish()
        }
        IV_z_toolbar_search.setOnClickListener {
            deploi =!deploi
            val anim =AnimationUtils.loadAnimation(this@list_Project, R.anim.slide_left_to_center)
            LL_search_listProject.startAnimation(anim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(arg0: Animation) {LL_search_listProject.visibility =View.VISIBLE}
                override fun onAnimationRepeat(arg0: Animation) {}
                override fun onAnimationEnd(arg0: Animation) {}
            })
            val anime =AnimationUtils.loadAnimation(this@list_Project, R.anim.slide_center_to_right)
            I_z_toolbar_listProject.startAnimation(anime)
            anime.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(arg0: Animation) {}
                override fun onAnimationRepeat(arg0: Animation) {}
                override fun onAnimationEnd(arg0: Animation) {I_z_toolbar_listProject.visibility =View.GONE}
            })
        }
        IV_back_toolbar_listProject.setOnClickListener {
            deploi =!deploi
            LV_list_project_listemsg.adapter = MyListAdapter(this@list_Project, listID =listID, listproject =listProject, index=1)
            val anim =AnimationUtils.loadAnimation(this@list_Project, R.anim.slide_right_to_center)
            I_z_toolbar_listProject.startAnimation(anim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(arg0: Animation) {I_z_toolbar_listProject.visibility =View.VISIBLE}
                override fun onAnimationRepeat(arg0: Animation) {}
                override fun onAnimationEnd(arg0: Animation) {}
            })
            val anime =AnimationUtils.loadAnimation(this@list_Project, R.anim.slide_center_to_left)
            LL_search_listProject.startAnimation(anime)
            anime.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(arg0: Animation) {}
                override fun onAnimationRepeat(arg0: Animation) {}
                override fun onAnimationEnd(arg0: Animation) {LL_search_listProject.visibility =View.GONE}
            })
            ET_search_listProject.setText("")
        }
        TV_z_toolbar_title.text ="Project list"
    }

    override fun onBackPressed() {
        if (deploi){
            deploi =!deploi
            val anim =AnimationUtils.loadAnimation(this@list_Project, R.anim.slide_right_to_center)
            I_z_toolbar_listProject.startAnimation(anim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(arg0: Animation) {I_z_toolbar_listProject.visibility =View.VISIBLE}
                override fun onAnimationRepeat(arg0: Animation) {}
                override fun onAnimationEnd(arg0: Animation) {}
            })
            val anime =AnimationUtils.loadAnimation(this@list_Project, R.anim.slide_center_to_left)
            LL_search_listProject.startAnimation(anime)
            anime.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(arg0: Animation) {}
                override fun onAnimationRepeat(arg0: Animation) {}
                override fun onAnimationEnd(arg0: Animation) {LL_search_listProject.visibility =View.GONE}
            })
        }else{finish()}
    }
}
