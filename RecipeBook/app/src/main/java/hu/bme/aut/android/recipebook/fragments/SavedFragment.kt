package hu.bme.aut.android.recipebook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.recipebook.R
import androidx.lifecycle.Observer
import hu.bme.aut.android.recipebook.adapter.RecipeAdapter
import hu.bme.aut.android.recipebook.databinding.FragmentSavedBinding
import hu.bme.aut.android.recipebook.viewmodel.RecipeViewModel


class SavedFragment : Fragment() {

    private lateinit var mRecipeViewModel: RecipeViewModel
    private lateinit var binding: FragmentSavedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(inflater,container,false)

        // Recyclerview
        val adapter = RecipeAdapter(false,requireContext())
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // RecipeViewModel
        mRecipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)
        mRecipeViewModel.readSavedRecipes.observe(viewLifecycleOwner, Observer { recipe ->
            adapter.setData(recipe)
        })
        binding.bottomnavbar.selectedItemId=R.id.savedscreen

        // Add menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomnavbar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homescreen -> findNavController().navigate(R.id.action_savedFragment_to_listFragment)
                else -> {}
            }
            true
        }
    }
}