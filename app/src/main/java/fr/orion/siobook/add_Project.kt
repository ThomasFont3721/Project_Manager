package fr.orion.siobook

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.activity_add_project.*
import kotlinx.android.synthetic.main.z_toolbar.*
import java.util.*

class add_Project : AppCompatActivity() {

    private val listAllUser: ArrayList<Cuser> = arrayListOf()

    private val listUserInProject: ArrayList<CUserInProject> = arrayListOf()

    private val listnothing: ArrayList<String> = arrayListOf()
    private var listRole: ArrayList<String> = arrayListOf("Project manager","Developer","Disigner","Class creator","Function creator","Undefined")

    private val listETUser: ArrayList<AutoCompleteTextView> = arrayListOf()
    private val listETRole: ArrayList<AutoCompleteTextView> = arrayListOf()

    private val listTILUser: ArrayList<TextInputLayout> = arrayListOf()
    private val listTILRole: ArrayList<TextInputLayout> = arrayListOf()

    private val IVlistbar: ArrayList<ImageView> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)
        IV_Add_project_exemple.visibility =View.GONE
        ACTV_add_project_nameUser.setText(CUSER.emailU)
        ACTV_add_project_role.setText(listRole[0])
        FULLSCREEN(window)
        initialise()
        getuser()

        IV_Add_project_removeUser.setOnClickListener {
            if (listETUser.size > 1 && listETRole.size > 1){
                Log.i(FLAG, listETUser.size.toString())
                listETUser[listETUser.lastIndex].visibility =View.GONE
                listETUser.removeAt(listETUser.lastIndex)

                listETRole[listETRole.lastIndex].visibility =View.GONE
                listETRole.removeAt(listETRole.lastIndex)

                listTILUser[listTILUser.lastIndex].visibility =View.GONE
                listTILUser.removeAt(listTILUser.lastIndex)

                listTILRole[listTILRole.lastIndex].visibility =View.GONE
                listTILRole.removeAt(listTILRole.lastIndex)

                IVlistbar[IVlistbar.lastIndex].visibility =View.GONE
                IVlistbar.removeAt(IVlistbar.lastIndex)

                Log.i(FLAG, listETUser.size.toString())
            }
        }

        listETUser.add(ACTV_add_project_nameUser)
        listETRole.add(ACTV_add_project_role)

        IV_Add_project_addUser.setOnClickListener {

            val IVBar =ImageView(this@add_Project)
            IVBar.setImageResource(R.color.colorPrimary)
            IVBar.layoutParams = IV_Add_project_exemple.layoutParams
            IVlistbar.add(IVBar)

            val TIL_Name = TextInputLayout(this@add_Project)
            TIL_Name.layoutParams =TIL_add_project_name.layoutParams
            listTILUser.add(TIL_Name)

            val Name = AutoCompleteTextView(this@add_Project)
            Name.inputType = InputType.TYPE_CLASS_TEXT
            Name.layoutParams = LL_Add_project_nameU.layoutParams
            Name.hint ="Contributor's email"
            /*Name.setAdapter(MyAdapterNameList(this@add_Project, email = listnothing, user = listAllUser))
            Name.threshold = 0
            Name.onFocusChangeListener = View.OnFocusChangeListener{ _, b ->
                if(b){ Name.showDropDown() }
            }*/
            listETUser.add(Name)
            TIL_Name.addView(Name)
            val TIL_Role = TextInputLayout(this@add_Project)
            TIL_Role.layoutParams =TIL_add_project_role.layoutParams
            listTILRole.add(TIL_Role)

            val Role = AutoCompleteTextView(this@add_Project)
            Role.inputType = InputType.TYPE_CLASS_TEXT
            Role.layoutParams = LL_Add_project_nameU.layoutParams
            Role.setAdapter(MyAdapterRoleList(this@add_Project, name = listRole))
            Role.threshold = 0
            Role.onFocusChangeListener = View.OnFocusChangeListener{ _, b ->
                if(b){ Role.showDropDown() }
            }
            Role.hint ="Contributor's role"
            listETRole.add(Role)
            TIL_Role.addView(Role)

            LL_Add_project_nameU.addView(IVBar)
            LL_Add_project_nameU.addView(TIL_Name)
            LL_Add_project_nameU.addView(TIL_Role)
        }

        B_add_project_generated.setOnClickListener {
            val _nameP =ET_Add_project_nameP.text.toString()


            if(TextUtils.isEmpty(_nameP.trim())){
                ET_Add_project_nameP.error ="Enter a project name"
                return@setOnClickListener
            }
            listETUser.forEach {
                if(TextUtils.isEmpty(it.text.toString().trim())){
                    it.error ="Enter a name for this contributor"
                    return@setOnClickListener
                }
            }
            listETRole.forEach {
                if(TextUtils.isEmpty(it.text.toString().trim())){
                    it.error ="Enter a role for this contributor"
                    return@setOnClickListener
                }
            }
            listETUser.forEach { email ->
                var boolean = false
                listAllUser.forEach {
                    if (email.text.toString() == it.emailU){
                        boolean =true
                    }
                }
                if (!boolean){
                    email.error ="This email is unknown"
                    return@setOnClickListener
                }
            }

            val listUserEmail = spaceX(listETUser)
            val listRoleUser =spaceY(listETRole)

            Log.i(FLAG, "${listETUser.size} ::: ${listETRole.size}")
            Log.i(FLAG, "${listUserEmail.size} ::: ${listRoleUser.size}")

            Log.i(FLAG, "${Timestamp.now().seconds}")


            val date = Calendar.getInstance()
            date.timeInMillis = Timestamp.now().seconds*1000L
            val cproject = Cproject(nameP = _nameP, millisP = date.timeInMillis)
            if (listUserEmail.size > 0){
                var i =0
                while (i < listUserEmail.size){
                    listAllUser.forEach {
                        if (listUserEmail[i] == it.emailU){
                            val cUserInProject =CUserInProject("",it.idU,"",listRoleUser[i], it.nameU, it.fNameU, listUserEmail[i])
                            listUserInProject.add(cUserInProject)
                            cproject.memberP.add(cUserInProject.idU)
                        }
                    }
                    i++
                }
            }
            Log.i(FLAG, "listUserInProject   ${listUserInProject.size}")
            CREATEPROJECT(cproject = cproject, listCUserInProject = listUserInProject)

            startActivity(Intent(this@add_Project, list_Project::class.java).apply {})
            overridePendingTransition(R.anim.slide_bottom_to_center, R.anim.slide_center_to_top)
            finish()
        }
    }
    private fun getuser(){
        DB.collection(USER)
                .get()
                .addOnCompleteListener { read ->
                    if (read.isSuccessful) {
                        for (user in read.result!!) {
                            listnothing.add(user.toObject(Cuser::class.java).emailU)
                            listAllUser.add(user.toObject(Cuser::class.java))
                            LOG(user.toObject(Cuser::class.java).emailU)
                        }
                        itemlist()
                    } else {
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                    }
                }

    }

    private fun itemlist(){
        /*ACTV_add_project_nameUser.setAdapter(MyAdapterNameList(this@add_Project, email = listnothing, user = listAllUser))
        ACTV_add_project_nameUser.threshold = 0
        ACTV_add_project_nameUser.onFocusChangeListener = View.OnFocusChangeListener{ _, b ->
            if(b){ ACTV_add_project_nameUser.showDropDown() }
        }*/
        ACTV_add_project_role.setAdapter(MyAdapterRoleList(this@add_Project, name = listRole))
        ACTV_add_project_role.threshold = 0
        ACTV_add_project_role.onFocusChangeListener = View.OnFocusChangeListener{ _, b ->
            if(b){ ACTV_add_project_role.showDropDown() }
        }
    }

    private fun initialise(){
        IV_z_toolbar_modify.visibility =View.GONE
        IV_z_toolbar_log_out.visibility =View.GONE
        IV_z_toolbar_add.visibility =View.GONE
        IV_z_toolbar_search.visibility =View.GONE
        TV_z_toolbar_title.text ="Add Project"
    }

    private fun spaceX(ListACTV: ArrayList<AutoCompleteTextView>): ArrayList<String> {
        val listResult: ArrayList<String> = arrayListOf()
        ListACTV.forEach {
            if (it.text.toString() != ""){
                var name: String =it.text.toString()
                name.toCharArray().forEach { _ ->
                    if (name.toCharArray()[name.toCharArray().lastIndex] == ' '){
                        Log.i(FLAG, "space")
                        name =it.text.toString().substring(0, it.text.toString().length-1)
                        it.setText(name)
                    }
                }
                if (name != ""){
                    listResult.add(it.text.toString())
                }
            }
        }
        Log.i(FLAG, listResult.toString())
        return listResult
    }
    private fun spaceY(ListACTV: ArrayList<AutoCompleteTextView>): ArrayList<String> {
        val listResult: ArrayList<String> = arrayListOf()
        ListACTV.forEach {
            if (it.text.isNotEmpty()){
                var name: String =it.text.toString()
                name.toCharArray().forEach { _ ->
                    if (name.toCharArray()[name.toCharArray().lastIndex] == ' '){
                        Log.i(FLAG, "space")
                        name =it.text.toString().substring(0, it.text.toString().length-1)
                        it.setText(name)
                    }
                }
                listResult.add(it.text.toString())
            }
        }
        Log.i(FLAG, listResult.toString())
        return listResult
    }

    override fun onBackPressed() {
        startActivity(Intent(this@add_Project, list_Project::class.java).apply {})
        overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
        finish()
    }
}
