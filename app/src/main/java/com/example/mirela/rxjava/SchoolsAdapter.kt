package com.example.mirela.rxjava

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.mirela.rxjava.databinding.SchoolItemBinding

interface Myadapter{
    fun notifChanges(list : List<SchoolViewModel>?)
}

class SchoolsAdapter(private val fragment:Fragment) : RecyclerView.Adapter<ViewHolder>(), Myadapter {

    private val schoolsViewModel: SchoolsViewModel by lazy {  ViewModelProviders.of(fragment).get(SchoolsViewModel::class.java) }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = SchoolItemBinding.inflate(layoutInflater, viewGroup, false)
        binding.setLifecycleOwner(fragment)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return schoolsViewModel.items.value?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, p: Int) {
        viewHolder.binding?.viewModel = schoolsViewModel.items.value?.get(p)

    }

    override fun notifChanges(list:List<SchoolViewModel>?) {
        notifyDataSetChanged()
    }

}

class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var binding: SchoolItemBinding? = DataBindingUtil.bind(view)


}