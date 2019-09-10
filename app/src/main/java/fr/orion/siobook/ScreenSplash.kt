package fr.orion.siobook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ScreenSplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_splash)
        FULLSCREEN(window)
        startActivity(Intent(this@ScreenSplash, Load::class.java).apply {})
        finish()
    }
}
