package com.example.mirela.rxjava

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.mirela.rxjava.databinding.SchoolItemBinding
import java.io.Console

interface Myadapter{
    fun setItems(list : List<SchoolViewModel>?)
}

class SchoolsAdapter(private val fragment:Fragment) : RecyclerView.Adapter<ViewHolder>(), Myadapter {

    private val schoolsViewModel: SchoolsViewModel by lazy {  ViewModelProviders.of(fragment).get(SchoolsViewModel::class.java) }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = SchoolItemBinding.inflate(layoutInflater, viewGroup, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return schoolsViewModel.items.value?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, p: Int) {
        viewHolder.binding?.viewModel = schoolsViewModel.items.value?.get(p)

    }

    override fun setItems(list:List<SchoolViewModel>?) {
        Log.e("set items ",schoolsViewModel.items.value?.size.toString())
        schoolsViewModel.items.value=list
        notifyDataSetChanged()
    }

}

class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var binding: SchoolItemBinding? = DataBindingUtil.bind(view)


}