package fr.orion.siobook

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register_x_login.*



class Register_X_Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_x_login)
        FULLSCREEN(window)
        var deja =false

        B_RXL_login.setOnClickListener {
            startActivity(Intent(this@Register_X_Login, Login::class.java).apply {})
            overridePendingTransition(R.anim.slide_right_to_center, R.anim.slide_center_to_left)
            finish()
        }
        B_RXL_register.setOnClickListener {
            startActivity(Intent(this@Register_X_Login, Register::class.java).apply {})
            overridePendingTransition(R.anim.slide_left_to_center, R.anim.slide_center_to_right)
            finish()
        }

        val countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (AUTH.currentUser != null && !deja) {
                    deja =true
                    DB.collection(USER)
                        .whereEqualTo(EMAILU ,AUTH.currentUser!!.email)
                        .get()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                for (user in it.result!!) {
                                    CUSER = user.toObject(Cuser::class.java)
                                    LOG(CUSER.affiche())
                                }
                                LOG("USER ::: ${CUSER.idU}")
                                LOG("USER IMG ::: \"${CUSER.imgUrlU}\"")
                                val ONE_MEGABYTE: Long = 1024 * 1024
                                STORAGE.reference.child("img_user/${CUSER.idU}").getBytes(ONE_MEGABYTE)
                                        .addOnSuccessListener {
                                            IMGBITMAP = BitmapFactory.decodeByteArray(it, 0, it.size)
                                            Log.i(FLAG, "img récuperer")
                                            startActivity(Intent(this@Register_X_Login, list_Project::class.java))
                                            overridePendingTransition(R.anim.hide_to_show, R.anim.show_to_hide)
                                            finish()
                                        }
                                        .addOnFailureListener {
                                            Log.i(FLAG, "img error ::: $it")
                                            startActivity(Intent(this@Register_X_Login, Register_X_Login::class.java))
                                            overridePendingTransition(R.anim.hide_to_show, R.anim.show_to_hide)
                                            finish()
                                        }
                                this.cancel()
                                Log.i(FLAG, "success this.cancel()")
                            }
                            else{
                                startActivity(Intent(this@Register_X_Login, Register_X_Login::class.java))
                                overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
                                finish()
                                Log.i(FLAG, "no success this.cancel()")
                                this.cancel()
                            }
                        }
                } else {
                    Log.i(FLAG, "no current user")
                }
            }
            override fun onFinish() { }
        }
        countDownTimer.start()
    }
}
