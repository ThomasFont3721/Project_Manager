package fr.orion.siobook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Param_project : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_param_project)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@Param_project, Home_Project::class.java).apply {})
        overridePendingTransition(R.anim.slide_left_to_center, R.anim.slide_center_to_right)
        finish()
    }
}
