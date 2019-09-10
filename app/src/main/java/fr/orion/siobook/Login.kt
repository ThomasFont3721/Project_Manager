package fr.orion.siobook

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FULLSCREEN(window)


        B_Login_log_in.setOnClickListener {
            val _email = ET_Login_email.text.toString()
            val _password = ET_Login_password.text.toString()


            if (TextUtils.isEmpty(_email.trim())) {

                this.ET_Login_email.error = "Enter Email"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(_password.trim())) {

                this.ET_Login_password.error = "Enter Password"
                return@setOnClickListener
            }
            LOGINUSER(email = _email, password = _password, activity = Login(), context = this@Login)

            Toast.makeText(this@Login, "Connection in progress...", Toast.LENGTH_LONG).show()
            val countDownTimer = object : CountDownTimer(3000, 100) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    startActivity(Intent(this@Login, Load::class.java).apply {})
                    overridePendingTransition(R.anim.hide_to_show, R.anim.show_to_hide)
                    finish()
                    this.cancel()
                }
            }
            countDownTimer.start()
        }

        IV_Login_back.setOnClickListener {
            startActivity(Intent(this@Login, Register_X_Login::class.java).apply {})
            overridePendingTransition(R.anim.slide_left_to_center, R.anim.slide_center_to_right)
            finish()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@Login, Register_X_Login::class.java).apply {})
        overridePendingTransition(R.anim.slide_left_to_center, R.anim.slide_center_to_right)
        finish()
    }
}
