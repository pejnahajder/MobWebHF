package hu.bme.aut.android.recipebook.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.recipebook.R
import hu.bme.aut.android.recipebook.data.Recipe
import hu.bme.aut.android.recipebook.databinding.FragmentAddBinding
import hu.bme.aut.android.recipebook.viewmodel.RecipeViewModel


class addFragment : Fragment() {

    private lateinit var mRecipeViewModel: RecipeViewModel
    private lateinit var binding: FragmentAddBinding
    private var cousineType: Int = -1
    private var meatType: Int = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater,container,false)

        mRecipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addRecipe.setOnClickListener{
            insertDataToDatabase()
        }

        val cousines = listOf("Hungarian","Italian","Europian","American","Asian")
        val adapter = activity?.let { ArrayAdapter(it,R.layout.dropdown_item,cousines) }
        binding.cousineDropdown.setAdapter(adapter)
        binding.cousineDropdown.setOnItemClickListener(OnItemClickListener { adapterView, view, position, id ->
            cousineType = position
        })

        val meats = listOf("Chicken","Turkey","Lamb","Pork","Beef","Fish","Other","None")
        val adapter2 = activity?.let { ArrayAdapter(it,R.layout.dropdown_item,meats) }
        binding.meatDropdown.setAdapter(adapter2)
        binding.meatDropdown.setOnItemClickListener(OnItemClickListener { adapterView, view, position, id ->
            meatType = position
        })

        binding.uploadImageView.setOnClickListener{
            pickImageFromGallery()
        }

        binding.addButton.setOnClickListener{
            if(binding.allIngredients.text.isEmpty()){
                binding.allIngredients.setText("${binding.ingredient.text} ${binding.quantity.text}")
            }
            else{
                binding.allIngredients.setText("${binding.allIngredients.text}\n${binding.ingredient.text} ${binding.quantity.text}")
            }
            binding.ingredient.text.clear()
            binding.quantity.text.clear()
            Toast.makeText(requireContext(), binding.allIngredients.text,Toast.LENGTH_SHORT).show()
        }
        binding.cancel.setOnClickListener{
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
        binding.allergenSelector.setOnClickListener {
            val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
            builder?.setTitle("Select Allergens")

            val allergenArray = arrayOf("Milk","Eggs","Nuts","Gluten","Soy")
            val checkItem = booleanArrayOf(false,false,false,false,false)

            builder?.setMultiChoiceItems(allergenArray,checkItem){dialog,id,isChecked->
                checkItem[id] = isChecked
            }

            builder?.setPositiveButton("Ok"){dialog,id->
                var text = String()
                for(i in allergenArray.indices){
                    if(checkItem[i])
                        text ="${text} ${allergenArray[i]}"
                }
                binding.allergenSelector.setText(text)
            }

            builder?.setNegativeButton("Cancel"){dialog,id->
                dialog.cancel()
            }
            val dialog: AlertDialog? = builder?.create()
            dialog?.show()
        }
    }

    private fun insertDataToDatabase() {
        val recipename = binding.recipeName.text.toString()
        val ingredients = binding.allIngredients.text.toString()
        val allergens = binding.allergenSelector.text.toString()
        val instructions = binding.instructions.text.toString()
        val prepTime = binding.preptime.text.toString()
        val servings = binding.servings.text.toString()

        var selectorsOK = false
        if (cousineType>=0 && meatType >=0) selectorsOK = true

        if(inputCheck(recipename,ingredients,instructions,prepTime,servings) && selectorsOK ){
            var allergen = "";
            if(!allergens.isEmpty()){
                if(allergens.contains("Milk")) allergen = "Milk"
                if(allergens.contains("Eggs")) allergen = "${allergen}Eggs"
                if(allergens.contains("Nuts")) allergen = "${allergen}Nuts"
                if(allergens.contains("Gluten")) allergen = "${allergen}Gluten"
                if(allergens.contains("Soy")) allergen = "${allergen}Soy"
            }

            // Create Recipe Object
            val recipe = Recipe(
                name = recipename,
                timecost = Integer.parseInt(prepTime),
                servings = Integer.parseInt(servings),
                ingredients = ingredients,
                instructions = instructions,
                cousine = cousineType,
                meatType = meatType,
                allergen = allergen,
                saved = 0
            )
            // Add Data to Database
            mRecipeViewModel.addRecipe(recipe)


            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
            // Navigate Back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(name: String, ingred: String,inst: String,time: String,servs: String ): Boolean{
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(ingred)&& TextUtils.isEmpty(inst)&& TextUtils.isEmpty(time)&& TextUtils.isEmpty(servs))
    }
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            binding.uploadImageView.setImageURI(data?.data)
        }
    }

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }
}