package com.example.mirela.rxjava.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mirela.rxjava.DI.LaunchApplication
import com.example.mirela.rxjava.R
import com.example.mirela.rxjava.SchoolRepository
import com.example.mirela.rxjava.SchoolsAdapter
import com.example.mirela.rxjava.viewModel.SchoolsViewModel
import com.example.mirela.rxjava.databinding.SchoolsFragmentBinding
import com.example.mirela.rxjava.viewModel.SchoolsViewModelFactory
import javax.inject.Inject

class SchoolsFragment : Fragment() {

    @Inject
    internal lateinit var schoolRepository: SchoolRepository

    private val schoolsAdapter: SchoolsAdapter by lazy {
        SchoolsAdapter(
            this
        )
    }

    private val viewModel: SchoolsViewModel by lazy {
        val factory = SchoolsViewModelFactory(schoolRepository)
        ViewModelProviders.of(this, factory).get(SchoolsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e("on create view ", "called meth")
        val binding =
            DataBindingUtil.inflate<SchoolsFragmentBinding>(
                inflater,
                R.layout.schools_fragment, container, false
            )
        binding.setLifecycleOwner(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = schoolsAdapter
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onAttach(context: Context?) {
        LaunchApplication().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.moveNext.observe(this, Observer {
            val intent = Intent(activity, DisplayDetailsActivity::class.java).apply {
                putExtra("item", it)
            }
            startActivity(intent)
        })
    }

}
