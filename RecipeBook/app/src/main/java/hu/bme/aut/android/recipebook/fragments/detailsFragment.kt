package hu.bme.aut.android.recipebook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import hu.bme.aut.android.recipebook.R
import hu.bme.aut.android.recipebook.data.Recipe
import hu.bme.aut.android.recipebook.databinding.FragmentAddBinding
import hu.bme.aut.android.recipebook.databinding.FragmentDetailsBinding
import hu.bme.aut.android.recipebook.viewmodel.RecipeViewModel


class detailsFragment : Fragment() {

    private val args by navArgs<detailsFragmentArgs>()
    private lateinit var mRecipeViewModel: RecipeViewModel
    private lateinit var binding: FragmentDetailsBinding
    var saved: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater,container,false)

        mRecipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)

        binding.recipeName.setText(args.currentItem.name)
        binding.cousine.setText(Recipe.CousineType.getByOrdinal(args.currentItem.cousine).toString())
        binding.preptime.setText("${args.currentItem.timecost.toString()} min")
        binding.servings.setText(args.currentItem.servings.toString())
        var allergenString = args.currentItem.allergen
        var allergens:String = ""
        if(allergenString.contains("Soy")){
            allergens = "soy"
        }
        if(allergenString.contains("Gluten")){
            if(allergens.isEmpty())
                allergens = "gluten"
            else
                allergens = "${allergens}, gluten"
        }
        if(allergenString.contains("Nuts")){
            if(allergens.isEmpty())
                allergens = "nuts"
            else
                allergens = "${allergens}, nuts"
        }
        if(allergenString.contains("Eggs")){

            if(allergens.isEmpty())
                allergens = "eggs"
            else
                allergens = "${allergens}, eggs"
        }
        if(allergenString.contains("Milk")){
            if(allergens.isEmpty())
                allergens = "milk"
            else
                allergens = "${allergens}, milk"
        }

        if(allergens.isEmpty()){
            allergens = "none"
        }

        if(args.currentItem.saved == 1){
            saved = true;
            binding.bookmark.setImageResource(R.drawable.ic_baseline_bookmark_24)
        }
        else{
            binding.bookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
        }

        binding.allergens.setText(allergens)
        binding.ingredients.setText(args.currentItem.ingredients)
        binding.preparation.setText(args.currentItem.instructions)

        // Add menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookmark = binding.bookmark
        var savedInt:Int = 0
        bookmark.setOnClickListener{
            if(!saved){
                bookmark.setImageResource(R.drawable.ic_baseline_bookmark_24)
                saved = true;
                savedInt = 1
                Toast.makeText(requireContext(), "Saved successfully!", Toast.LENGTH_SHORT).show()
            }
            else{
                bookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
                saved = false;
                savedInt = 0
                Toast.makeText(requireContext(), "Successfully removed from the saved recipes!", Toast.LENGTH_SHORT).show()
            }
            // Create Recipe Object
            val updatedRecipe = Recipe(
                args.currentItem.id,
                args.currentItem.name,
                args.currentItem.timecost,
                args.currentItem.servings,
                args.currentItem.ingredients,
                args.currentItem.instructions,
                args.currentItem.cousine,
                args.currentItem.meatType,
                args.currentItem.allergen,
                savedInt)
            // Update Current Recipe
            mRecipeViewModel.updateRecipe(updatedRecipe)

        }

        binding.close.setOnClickListener{
            if(args.parentscreen){
                findNavController().navigate(R.id.action_detailsFragment_to_listFragment)
            }
            else{
                findNavController().navigate(R.id.action_detailsFragment_to_savedFragment)
            }

        }
    }

}