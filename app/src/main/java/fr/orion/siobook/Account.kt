package fr.orion.siobook

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.z_menu_home.*
import kotlinx.android.synthetic.main.z_toolbar.*

class Account : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        FULLSCREEN(window)
        initialise()
        toolbar()
        textView.text = CUSER.affiche()
    }
    private fun initialise(){
        IV_menu_home_img_account.setImageBitmap(IMGBITMAP)
        IV_menu_home_img_account.borderColor = getColor(R.color.colorPrimary)
        bottomMenu()
    }
    private fun bottomMenu(){
        LL_menu_home_listproject.setOnClickListener {
            startActivity(Intent(this@Account, list_Project::class.java).apply {})
            overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
            finish()
        }
        LL_menu_home_account.setOnClickListener {
            startActivity(Intent(this@Account, Account::class.java).apply {})
            overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
            finish()
        }
    }
    private fun toolbar(){
        TV_z_toolbar_title.text ="Account"
        IV_z_toolbar_add.visibility =View.GONE
        IV_z_toolbar_log_out.setOnClickListener {
            LOGOUTUSER()
            startActivity(Intent(this@Account, Load::class.java).apply {})
            overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
            finish()
        }
    }
}
