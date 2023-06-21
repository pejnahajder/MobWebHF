package hu.bme.aut.android.recipebook.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Query
import hu.bme.aut.android.recipebook.data.Recipe
import hu.bme.aut.android.recipebook.data.RecipeDao

class RecipeRepository(private val recipeDao: RecipeDao) {

    val readAllData: LiveData<List<Recipe>> = recipeDao.readAllData()

    val readSavedRecipes : LiveData<List<Recipe>> = recipeDao.getSavedRecipes()


    suspend fun addRecipe(recipe: Recipe){
        recipeDao.addRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: Recipe){
        recipeDao.updateRecipe(recipe)
    }

    suspend fun deleteAllRecipes(){
        recipeDao.deleteAllRecipes()
    }


}