package ch.hslu.mobpro.mypokedex.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.hslu.mobpro.mypokedex.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, MainFragment())
            transaction.commit()
        }
    }
}