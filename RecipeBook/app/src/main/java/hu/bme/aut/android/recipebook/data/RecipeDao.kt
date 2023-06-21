package hu.bme.aut.android.recipebook.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRecipe(recipe: Recipe)

    @Update
    suspend fun updateRecipe(recipeItem: Recipe)

    @Query("DELETE FROM recipes")
    suspend fun deleteAllRecipes(): Integer

    @Query("SELECT * FROM recipes")
    fun readAllData(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE saved LIKE 1")
    fun getSavedRecipes(): LiveData<List<Recipe>>

}