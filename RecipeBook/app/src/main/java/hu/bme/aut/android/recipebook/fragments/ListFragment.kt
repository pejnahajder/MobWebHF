package hu.bme.aut.android.recipebook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.recipebook.R
import hu.bme.aut.android.recipebook.adapter.RecipeAdapter
import hu.bme.aut.android.recipebook.data.Recipe
import hu.bme.aut.android.recipebook.databinding.FragmentListBinding
import hu.bme.aut.android.recipebook.other.MyFilter
import hu.bme.aut.android.recipebook.viewmodel.RecipeViewModel
import java.util.*

class ListFragment : Fragment() {

    private lateinit var mRecipeViewModel: RecipeViewModel
    private lateinit var binding: FragmentListBinding
    private lateinit var myadapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = FragmentListBinding.inflate(inflater,container,false)

        mRecipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)

        // Recyclerview

        myadapter = RecipeAdapter(true,requireContext())

        binding.bottomnavbar.selectedItemId=R.id.homescreen

        binding.recyclerview.adapter = myadapter

        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // RecipeViewModel

        mRecipeViewModel.readAllData.observe(viewLifecycleOwner, Observer { recipes ->
            myadapter.setData(recipes)
        })


        // Add menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searcdhView.clearFocus()
        binding.searcdhView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val searchText = p0!!.lowercase(Locale.getDefault())
                myadapter.filter.filter(searchText)
                if(myadapter.listIsEmpty()){
                    Toast.makeText(requireContext(), "No recipes has this name!", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                val searchText = p0!!.lowercase(Locale.getDefault())
                myadapter.filter.filter(searchText)
                return false
            }
        })
        binding.clearFilters.setOnClickListener{
            myadapter.filterRecipes(MyFilter())
        }

        binding.filterButton.setOnClickListener{
            val showFilters = FilterFragment(myadapter)
            showFilters.show((activity as AppCompatActivity).supportFragmentManager, "showFilters")
        }

        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        binding.bottomnavbar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.savedscreen -> findNavController().navigate(R.id.action_listFragment_to_savedFragment)
                else -> {}
            }
            true
        }
    }


}