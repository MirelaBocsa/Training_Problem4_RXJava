package com.example.mirela.rxjava

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mirela.rxjava.databinding.ActivityMainBinding
import com.example.mirela.rxjava.databinding.SchoolsFragmentBinding

class SchoolsFragment : Fragment() {
    private val schoolsAdapter: SchoolsAdapter by lazy { SchoolsAdapter(this.context!!) }

    private val viewModel: SchoolsViewModel by lazy { SchoolsViewModel() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e("on create view ","called meth")
        val binding = DataBindingUtil.inflate<SchoolsFragmentBinding>(inflater, R.layout.schools_fragment, container, false)
        binding.setLifecycleOwner(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = schoolsAdapter
        binding.viewModel = viewModel

        return binding.root
    }
}
