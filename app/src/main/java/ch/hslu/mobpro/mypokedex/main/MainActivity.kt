package ch.hslu.mobpro.mypokedex.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.hslu.mobpro.mypokedex.R

/*
Main Activity replaces on creation the activity_main.xml with fragment_main.xml
 */
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