package com.glucode.about_you.engineers

import EngineersRecyclerViewAdapter
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.glucode.about_you.R
import com.glucode.about_you.databinding.FragmentEngineersBinding
import com.glucode.about_you.engineers.models.Engineer
import com.glucode.about_you.mockdata.MockData

class EngineersFragment : Fragment() {
    //Declarations
    private lateinit var binding: FragmentEngineersBinding
    private lateinit var adapter: EngineersRecyclerViewAdapter
    private var engineers: MutableList<Engineer> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEngineersBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        engineers = MockData.engineers.toMutableList()

        adapter = EngineersRecyclerViewAdapter(engineers) {
            goToAbout(it)
        }

        binding.list.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(dividerItemDecoration)

        return binding.root
    }

    //Shows menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_engineers, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    //Actions performed when menu items are selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //Order the list of engineers ASCENDING based on the number of years, coffees or bugs.
        val sortedEngineers = when(item.itemId) {
            R.id.action_years -> {
                engineers.sortedBy { it.quickStats.years }
            }
            R.id.action_coffees -> {
                engineers.sortedBy { it.quickStats.coffees }
            }
            R.id.action_bugs -> {
                engineers.sortedBy { it.quickStats.bugs }
            }
            else -> return super.onOptionsItemSelected(item)
        }

        engineers.clear() //Removes current list
        engineers.addAll(sortedEngineers) //Adds engineers back to the list but they are  sorted
        adapter.notifyDataSetChanged()
        return true
    }

    fun refreshList() {
        adapter.notifyDataSetChanged()
    }
//When engineers profile is clicked it opens their about page
    private fun goToAbout(engineer: Engineer) {
        val bundle = Bundle().apply {
            putString("name", engineer.name)
        }
        findNavController().navigate(R.id.action_engineersFragment_to_aboutFragment, bundle)
    }
}