package com.example.mirela.rxjava

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class SchoolViewModel(private var school: School, val itemClick: (item: School) -> Unit) : ViewModel() {

    val id: String
        get() = school.settingsCode

    val logo: String
        get() = school.logo

    val state: String?
        get() = school.state

    val district: String
        get() = school.district

    fun onClick() {
        itemClick.invoke(school)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SchoolViewModel

        if (school != other.school) return false

        return true
    }


}

