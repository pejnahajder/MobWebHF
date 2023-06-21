package hu.bme.aut.android.recipebook.viewmodel

import android.app.Application
import androidx.lifecycle.*
import hu.bme.aut.android.recipebook.data.Recipe
import hu.bme.aut.android.recipebook.data.RecipeDatabase
import hu.bme.aut.android.recipebook.other.MyFilter
import hu.bme.aut.android.recipebook.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RecipeViewModel (application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Recipe>>
    val readSavedRecipes: LiveData<List<Recipe>>

    private val repository: RecipeRepository

    init {
        val recipeDao = RecipeDatabase.getDatabase(
            application
        ).recipeDao()
        repository = RecipeRepository(recipeDao)
        readAllData = repository.readAllData

        readSavedRecipes = repository.readSavedRecipes
    }


    fun addRecipe(recipe: Recipe){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRecipe(recipe)
        }
    }

    fun updateRecipe(recipe: Recipe){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRecipe(recipe)
        }
    }


    fun deleteAllRecipes(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllRecipes()
        }
    }

}