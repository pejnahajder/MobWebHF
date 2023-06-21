package hu.bme.aut.android.recipebook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.android.recipebook.R
import hu.bme.aut.android.recipebook.adapter.RecipeAdapter
import hu.bme.aut.android.recipebook.databinding.FragmentFilterBinding
import hu.bme.aut.android.recipebook.other.MyFilter
import hu.bme.aut.android.recipebook.viewmodel.RecipeViewModel


class FilterFragment(private var myadapter:RecipeAdapter) : DialogFragment() {

    private lateinit var binding: FragmentFilterBinding
    private lateinit var mRecipeViewModel: RecipeViewModel
    private var filter: MyFilter = MyFilter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBinding.inflate(layoutInflater)
        mRecipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cousines = listOf("Hungarian","Italian","Europian","American","Asian")
        val adapter = activity?.let { ArrayAdapter(it, R.layout.dropdown_item,cousines) }
        binding.cousineDropdown.setAdapter(adapter)
        binding.cousineDropdown.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, position, id ->
            filter.add_cFilter(position)
        })

        val meats = listOf("Chicken","Turkey","Lamb","Pork","Beef","Fish","Other","None")
        val adapter2 = activity?.let { ArrayAdapter(it, R.layout.dropdown_item,meats) }
        binding.meatDropdown.setAdapter(adapter2)
        binding.meatDropdown.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, position, id ->
            filter.add_mFilter(position)
        })

        val allergens = listOf("Milk","Eggs","Nuts","Gluten","Soy")
        val adapter3 = activity?.let { ArrayAdapter(it, R.layout.dropdown_item,allergens) }
        binding.allergenDropdown.setAdapter(adapter3)
        binding.allergenDropdown.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, position, id ->
            val selectedValue: String = adapterView.adapter.getItem(position).toString()
            filter.add_aFilter(selectedValue)
        })


        binding.cancel.setOnClickListener{
            this.dismiss()
        }

        binding.ok.setOnClickListener{
            myadapter.filterRecipes(filter)
            if(!filter.isEmpty()){
                Toast.makeText(requireContext(), "Filtering for: ${filter.toText()}", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(requireContext(), "Filter was empty!", Toast.LENGTH_LONG).show()
            }

            this.dismiss()
        }

    }

}

