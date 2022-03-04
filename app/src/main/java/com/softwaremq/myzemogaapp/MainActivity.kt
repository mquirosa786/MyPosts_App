package com.softwaremq.myzemogaapp

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.softwaremq.myzemogaapp.base.BaseActivity
import com.softwaremq.myzemogaapp.databinding.ActivityMainBinding
import com.softwaremq.myzemogaapp.posts.HomeFragment

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        goToFragment(HomeFragment())
    }
}