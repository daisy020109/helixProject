package com.example.helixproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.helixproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {



    private var vBinding : ActivityMainBinding? = null
    private val binding get() = vBinding!!
    private val adapter by lazy { ViewPagerAdapter(supportFragmentManager) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val viewPager: ViewPager = findViewById(R.id.viewPager)
        //val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        //tabLayout.setupWithViewPager(viewPager)
    }

    override fun onDestroy() {
        vBinding = null
        super.onDestroy()
    }

}


