package com.glucode.about_you.about.views

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.glucode.about_you.R
import com.glucode.about_you.databinding.FragmentRankedEngineersBinding
import com.glucode.about_you.engineers.models.Engineer
import com.glucode.about_you.mockdata.MockData
import EngineersRecyclerViewAdapter

class RankedEngineersFragment : Fragment() {
    // Declarations
    private var _binding: FragmentRankedEngineersBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EngineersRecyclerViewAdapter
    private var engineers: MutableList<Engineer> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankedEngineersBinding.inflate(inflater, container, false)
        setupRecyclerView()

        // Get the sort type from arguments and sort
        arguments?.getString("sortType")?.let { sortType ->
            sortEngineers(sortType)
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        engineers = MockData.engineers.toMutableList()
        adapter = EngineersRecyclerViewAdapter(engineers) { engineer ->
            val bundle = Bundle().apply {
                putString("name", engineer.name)
            }
            findNavController().navigate(R.id.action_engineersFragment_to_aboutFragment, bundle)
        }

        binding.rankedList.apply {
            adapter = this@RankedEngineersFragment.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun sortEngineers(sortType: String) {
        //Order the list of engineers ASCENDING based on the number of years, coffees or bugs.
        val sortedEngineers = when (sortType) {
            "coffees" -> engineers.sortedByDescending { it.quickStats.coffees }
            "bugs" -> engineers.sortedBy { it.quickStats.bugs }
            "years" -> engineers.sortedBy { it.quickStats.years }
            else -> engineers
        }

        engineers.clear()
        engineers.addAll(sortedEngineers)
        adapter.notifyDataSetChanged()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}