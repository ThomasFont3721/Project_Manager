package fr.orion.siobook

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class Load : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)
        FULLSCREEN(window)

        if (AUTH.currentUser == null || CUSER.emailU == "") {
            startActivity(Intent(this@Load, Register_X_Login::class.java))
            overridePendingTransition(R.anim.hide_to_show, R.anim.show_to_hide)
            finish()
        }
        else{
            DB.collection(USER)
                .whereEqualTo(EMAILU ,AUTH.currentUser!!.email)
                .get()
                .addOnCompleteListener { user ->
                    if (user.isSuccessful) {
                        for (userOne in user.result!!) {
                            CUSER = userOne.toObject(Cuser::class.java)
                            LOG("USER IMG ::: \"${userOne.toObject(Cuser::class.java).affiche()}\"")
                        }
                        LOG("USER ::: ${CUSER.idU}")
                        LOG("USER IMG ::: \"${CUSER.imgUrlU}\"")
                        val ONE_MEGABYTE: Long = 1024 * 1024
                        STORAGE.reference.child("img_user/${CUSER.idU}").getBytes(ONE_MEGABYTE)
                                .addOnSuccessListener {
                                    IMGBITMAP = BitmapFactory.decodeByteArray(it, 0, it.size)
                                    Log.i(FLAG, "img récuperer")
                                    startActivity(Intent(this@Load, list_Project::class.java))
                                    overridePendingTransition(R.anim.hide_to_show, R.anim.show_to_hide)
                                    finish()
                                }
                                .addOnFailureListener {
                                    Log.i(FLAG, "img error ::: $it")
                                    startActivity(Intent(this@Load, Register_X_Login::class.java))
                                    overridePendingTransition(R.anim.hide_to_show, R.anim.show_to_hide)
                                    finish()
                                }
                    }
                    else{
                        startActivity(Intent(this@Load, Register_X_Login::class.java))
                        overridePendingTransition(R.anim.hide_to_show, R.anim.show_to_hide)
                        finish()
                    }
                }
        }
    }
}