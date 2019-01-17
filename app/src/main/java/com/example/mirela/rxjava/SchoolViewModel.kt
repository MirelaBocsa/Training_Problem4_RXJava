package com.example.mirela.rxjava
import androidx.lifecycle.ViewModel

class SchoolViewModel(private var school: School, private val itemClick: (item: School) -> Unit) : ViewModel() {

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

