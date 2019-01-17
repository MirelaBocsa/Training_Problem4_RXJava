package com.example.mirela.rxjava

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mirela.rxjava.databinding.ItemViewBinding

class DisplayDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.item_view)

//        val binding: ItemViewBinding = ItemViewBinding.inflate(layoutInflater)
        val binding: ItemViewBinding = DataBindingUtil.setContentView(
            this, R.layout.item_view)

        val school = intent.extras.getParcelable<School>("item")
        Log.e(" item details   ", school.district)

        binding.viewModel = school
    }
}