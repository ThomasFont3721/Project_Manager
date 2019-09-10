package fr.orion.siobook

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

fun CREATEUSER(cuser: Cuser, password: String, selectedPhoto: ByteArray?, activity: AppCompatActivity, context: Context) {
    AUTH.createUserWithEmailAndPassword(cuser.emailU, password)
        .addOnCompleteListener(activity) { create ->
            if (create.isSuccessful) {
                Toast.makeText(context, "Successful",Toast.LENGTH_SHORT).show()
                Log.i(FLAG, "createUserWithEmail:sucess")

                AUTH.signInWithEmailAndPassword(cuser.emailU, password)
                    .addOnCompleteListener(activity) { login ->
                        if (login.isSuccessful) {
                            Log.d(FLAG, "signInWithEmail:success")

                            DB.collection(USER)
                                .add(cuser)
                                .addOnSuccessListener { newuser ->
                                    Toast.makeText(context, "add",Toast.LENGTH_SHORT).show()
                                    cuser.idU = newuser.id
                                    DB.collection(USER).document(cuser.idU).update(IDUSER, cuser.idU)
                                    Log.i(FLAG, "img put start")

                                    val filename = cuser.idU
                                    val ref = STORAGE.getReference("/img_user/$filename")

                                    ref.putBytes(selectedPhoto!!)
                                            .addOnSuccessListener {
                                                Log.i(FLAG, "img put")
                                                ref.downloadUrl.addOnSuccessListener {
                                                    DB.collection(USER).document(cuser.idU).update(IMGURLU, it.toString())
                                                    cuser.imgUrlU = it.toString()
                                                }
                                            }
                                            .addOnFailureListener {
                                                Log.i(FLAG, it.toString())
                                            }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "lose",Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Log.w(FLAG, "signInWithEmail:failure", login.exception)
                            Toast.makeText(context, "Authentication failed.",Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Log.i(FLAG, "createUserWithEmail:failure", create.exception)
                Toast.makeText(context, "This email address is already used",Toast.LENGTH_SHORT).show()
                Toast.makeText(context, create.exception.toString(),Toast.LENGTH_LONG).show()
            }
        }
}

fun LOGINUSER(email: String, password: String, activity: AppCompatActivity, context: Context): Boolean {
    var boolean: Boolean = false
    AUTH.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { task ->
            boolean = if (task.isSuccessful) {
                Log.d(FLAG, "signInWithEmail:success")
                true
            } else {
                Log.w(FLAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(context, "Authentication failed.",Toast.LENGTH_SHORT).show()
                false
            }
        }
    return boolean
}
fun LOGOUTUSER(){
    CUSER = Cuser()
    AUTH.signOut()
}

fun CREATEPROJECT(cproject: Cproject, listCUserInProject: ArrayList<CUserInProject>){
    DB.collection(PROJECT)
            .add(cproject)
            .addOnSuccessListener { doc ->
                Log.i(FLAG, "sucess : project")
                DB.collection(PROJECT).document(doc.id).update(IDP, doc.id)
                CIDPROJECT =doc.id
                listCUserInProject.forEach {
                    it.idProject =doc.id
                    CREATEUSERUIP(it)
                }
            }
            .addOnFailureListener {
                Log.i(FLAG, it.toString())
            }
}

fun CREATEUSERUIP(cUserInProject: CUserInProject){
    DB.collection(USERINPROJECT)
            .add(cUserInProject)
            .addOnSuccessListener {
                Log.i(FLAG, "sucess : create user in project")
                DB.collection(USERINPROJECT).document(it.id).update(IDUIP, it.id)
            }
            .addOnFailureListener {
                LOG("ERROR add uip ::: $it")
            }
}
fun ADDUIP(cUserInProject: CUserInProject){
    val memberP = CPROJECT.memberP
    DB.collection(USERINPROJECT)
            .add(cUserInProject)
            .addOnSuccessListener { uip ->
                DB.collection(USERINPROJECT).document(uip.id).update(IDUIP, uip.id)
                        .addOnSuccessListener {
                            memberP.add(cUserInProject.idU)
                            DB.collection(PROJECT).document(CIDPROJECT).update(MEMBERP, memberP)
                                    .addOnCompleteListener {
                                        LOG("sucess : add user in project")
                                    }
                                    .addOnFailureListener {
                                        LOG("ERROR update list uip ::: $it")
                                    }
                        }
                        .addOnFailureListener {
                            LOG("ERROR update id uip ::: $it")
                        }
            }
            .addOnFailureListener {
                LOG("ERROR add uip ::: $it")
            }
}
fun UPDATERATIOANDSTATEPROJECT(){
    var ratio =0
    var state =1
    LISTUIP.forEach {
        ratio += it.ratioUIP
        if (state < it.stateUIP) state =it.stateUIP
    }
    ratio /= LISTUIP.size
    LOG("nouveaux ratio ::: $ratio\nnouvel state ::: $state")
    if (ratio != CPROJECT.ratioP){
        DB.collection(PROJECT).document(CIDPROJECT)
                .update(RATIOP, ratio)
                .addOnCompleteListener {
                    CPROJECT.ratioP =ratio
                }
                .addOnFailureListener {
                    LOG("ERROR change ratio ::: $it")
                }
    }
    if (state != CPROJECT.state){
        DB.collection(PROJECT).document(CIDPROJECT)
                .update(STATEP, state)
                .addOnCompleteListener {
                    CPROJECT.state =state
                }
                .addOnFailureListener {
                    LOG("ERROR change state ::: $it")
                }
    }
}
fun UPDATERATIOANDSTATEUIP(){
    val nball = CUSERINPROJECT.listtaskUIP.size
    var nbtrue =0.0
    var stateU =1
    CUSERINPROJECT.listbooltaskUIP.forEach {
        if (it) nbtrue++
    }
    CUSERINPROJECT.listInttaskUIP.forEach {
        if (it > stateU && it != 4) stateU =it
    }
    var i=0
    CUSERINPROJECT.listInttaskUIP.forEach {
        if (it == 4) i++
    }
    if (i == nball && i == nbtrue.toInt()) stateU =4
    DB.collection(USERINPROJECT).document(CUSERINPROJECT.idUIP)
            .update(LISTBOOLTASKUIP, CUSERINPROJECT.listbooltaskUIP)
            .addOnSuccessListener {
                LOG("List bool changed")
                UPDATERATIOANDSTATEPROJECT()
            }
            .addOnFailureListener {
                LOG("ERROR CHANGE LIST BOOL ::: $it")
            }
    if (CUSERINPROJECT.stateUIP != stateU){
        DB.collection(USERINPROJECT).document(CUSERINPROJECT.idUIP)
                .update(STATEUIP, stateU)
                .addOnSuccessListener {
                    LOG("State uip changed")
                }
                .addOnFailureListener {
                    LOG("ERROR CHANGE STATE UIP ::: $it")
                }
    }
    DB.collection(USERINPROJECT).document(CUSERINPROJECT.idUIP)
            .update(RATIOUIP, (nbtrue/nball)*100)
            .addOnSuccessListener {
                LOG("ratio changed")
                UPDATERATIOANDSTATEPROJECT()
            }
            .addOnFailureListener {
                LOG("ERROR CHANGE RATIO ::: $it")
            }

}

fun UPDATEUIP(){
    val user = DB.collection(USERINPROJECT).document(CUSERINPROJECT.idUIP)
    user.update(LISTBOOLTASKUIP, CUSERINPROJECT.listbooltaskUIP)
    user.update(LISTINTTASKUIP, CUSERINPROJECT.listInttaskUIP)
    user.update(LISTTASKUIP, CUSERINPROJECT.listtaskUIP)
            .addOnSuccessListener {
                LOG("Update uip success")
                UPDATERATIOANDSTATEUIP()
            }
            .addOnFailureListener {
                LOG("ERROR UPDATE UIP ::: $it")
            }
}

fun DELETEPROJECT(idP: String){
    DB.collection(PROJECT).document(idP)
            .delete()
            .addOnCompleteListener {
                Log.i(FLAG, "Project deleted")
            }

    DB.collection(USERINPROJECT)
            .whereEqualTo(IDPROJECT, idP)
            .get()
            .addOnCompleteListener { listUIP ->
                for (uip in listUIP.result!!) {
                    DB.collection(USERINPROJECT).document(uip.id).delete()
                            .addOnSuccessListener {
                                Log.i(FLAG, uip.id + " deleted")
                                STORAGE.reference.child("img_msg/")
                            }
                            .addOnFailureListener {
                                LOG("error delete ${uip.id} ::: $it")
                            }
                }
            }
            .addOnFailureListener {
                Log.i(FLAG, "Error : $it")
            }
    DB.collection(MESSAGE)
            .whereEqualTo(IDPM, idP)
            .get()
            .addOnCompleteListener { listmsg ->
                for (msg in listmsg.result!!) {
                    DB.collection(MESSAGE).document(msg.id).delete()
                            .addOnSuccessListener {
                                Log.i(FLAG, msg.id + " deleted")
                            }
                            .addOnFailureListener {
                                LOG("error delete ${msg.id} ::: $it")
                            }
                }
            }
            .addOnFailureListener {
                Log.i(FLAG, "Error get message ::: $it")
            }
}
/*fun GETIMG(message: Cmessage, context: Context, position: Int): Bitmap{
    var img :Bitmap = (ContextCompat.getDrawable(context, R.drawable.men1) as BitmapDrawable).bitmap
    val ONE_MEGABYTE: Long = 1024 * 1024

    STORAGE.reference.child("img_user/${message.idUSM}").getBytes(ONE_MEGABYTE)
            .addOnSuccessListener {
                img =BitmapFactory.decodeByteArray(it, 0, it.size)
                Log.i(FLAG, "img ${message.idUSM} récuperer")
            }
            .addOnFailureListener {
                Log.i(FLAG, "img error ::: $it")
            }
    return img
}
fun DELETEMSG(idM: String){
    DB.collection(MESSAGE).document(idM).delete()
            .addOnSuccessListener {
                LOG("$idM deleted")
            }
            .addOnFailureListener {
                LOG("error delete $idM ::: $it")
            }
}
fun DELETEMSGWITHIMG(message: Cmessage){
    DB.collection(MESSAGE).document(message.idM).delete()
            .addOnSuccessListener {
                LOG("${message.idM} deleted")

            }
            .addOnFailureListener {
                LOG("error delete ${message.idM} ::: $it")
            }
}*/

fun ADDSMS(message: Cmessage){
    DB.collection(MESSAGE)
            .add(message)
            .addOnSuccessListener {msg->
                DB.collection(MESSAGE).document(msg.id).update(IDM, msg.id)
                        .addOnSuccessListener {
                            LOG("msg added ${msg.id} ::: content ${message.contentM}")
                        }
                        .addOnFailureListener {
                            LOG("Error UPDATE msg ::: $it")
                        }
            }
            .addOnFailureListener {
                LOG("Error SEND msg ::: $it")
            }
}

fun ADDMMS(message: Cmessage, selectedPhoto: ByteArray){
    DB.collection(MESSAGE)
            .add(message)
            .addOnSuccessListener { msg ->
                LOG("msg added without ID")
                DB.collection(MESSAGE).document(msg.id).update(IDM, msg.id)
                        .addOnSuccessListener {
                            message.idM =msg.id
                            LOG("msg added with ID ::: ${msg.id}")
                            val ref = STORAGE.getReference("/img_msg/${msg.id}")
                            ref.putBytes(selectedPhoto)
                                    .addOnSuccessListener {
                                        ref.downloadUrl.addOnSuccessListener {
                                            message.contentM =it.toString()
                                            DB.collection(MESSAGE).document(msg.id).update(CONTENTM, message.contentM)
                                                    .addOnSuccessListener {
                                                        LOG("img added to message ${msg.id}")
                                                    }
                                                    .addOnFailureListener {
                                                        LOG("ERROR img NOT added to message ${msg.id} ::: $it")
                                                    }
                                        }
                                    }
                                    .addOnFailureListener {
                                        LOG("ERROR send img in msg ${msg.id} ::: $it")
                                    }
                        }
                        .addOnFailureListener {
                            LOG("ERROR update msg ::: $it")
                        }
            }
            .addOnFailureListener {
                LOG("ERROR add msg ::: $it")
            }
    /*DB.collection(MESSAGE)
            .add(message)
            .addOnSuccessListener {msg->
                DB.collection(MESSAGE).document(msg.id).update(IDM, msg.id)
                        .addOnSuccessListener {
                            LOG("msg added ${msg.id} ::: content ${message.contentM}")
                            selectedPhoto.forEachIndexed { index, bytesArray ->
                                val filename = "$index${msg.id}"
                                val ref = STORAGE.getReference("/img_msg/$filename")
                                ref.putBytes(bytesArray)
                                        .addOnSuccessListener {
                                            ref.downloadUrl.addOnSuccessListener {
                                                message.contentM =it.toString()
                                                DB.collection(MESSAGE).document(msg.id).update(CONTENTM, message.contentM)
                                                        .addOnSuccessListener {
                                                            LOG("img $index added to message ${msg.id}")
                                                        }
                                                        .addOnFailureListener {
                                                            LOG("img $index NOT added to message ${msg.id} ::: $it")
                                                        }
                                            }
                                        }
                                        .addOnFailureListener {
                                            LOG("error send img $index in msg ${msg.id} ::: $it")
                                        }
                            }
                        }
                        .addOnFailureListener {
                            LOG("Error UPDATE msg ::: $it")
                        }
            }
            .addOnFailureListener {
                LOG("Error SEND msg ::: $it")
            }*/
}


fun VERIFUPPER(_password: String): Boolean{
    return !_password.matches("[A-Z].*".toRegex()) && !_password.matches(".*[A-Z].*".toRegex()) && !_password.matches(".*[A-Z]".toRegex())
}
fun VERIFLOWER(_password: String): Boolean{
    return !_password.matches("[a-z].*".toRegex()) && !_password.matches(".*[a-z].*".toRegex()) && !_password.matches(".*[a-z]".toRegex())
}
fun VERIFNUMBER(_password: String): Boolean{
    return !_password.matches("\\d.*".toRegex()) && !_password.matches(".*\\d.*".toRegex()) && !_password.matches(".*\\d".toRegex())
}

fun FULLSCREEN(window: Window){
    val decorView = window.decorView
    decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            // Hide the nav bar and status bar
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN)
}

/*fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
    this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            view.setOnClickListener {
                val holder = getChildViewHolder(view)
                onClickListener.onItemClicked(holder.adapterPosition, view)
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {
            view.setOnClickListener(null)
        }
    })
}*/

fun ArrayList<ArrayList<out Any>>.getImg(id: String){
    LISTALLIMG.forEach {
        if (it[0] ==id){
            IMGFULLSCREEN =(it[1] as Bitmap)
            IDIMGFULLSCREEN =it[0].toString()
        }
    }
}

fun VISIBLE(objects: View){
    objects.visibility =View.VISIBLE
}
fun GONE(objects: View){
    objects.visibility =View.GONE
}

fun LOG(string: String){
    Log.i(FLAG, string)
}
fun TOAST(string: String, context: Context){
    Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
}



