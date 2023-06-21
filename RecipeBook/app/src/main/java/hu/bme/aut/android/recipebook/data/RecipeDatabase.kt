package hu.bme.aut.android.recipebook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
@TypeConverters(value = [Recipe.MeatType::class,Recipe.CousineType::class])
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
            val tempIsntance = INSTANCE
            if(tempIsntance!=null){
                return tempIsntance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipes"
                ).createFromAsset("database/recipes3.db").build() //.createFromAsset()
                INSTANCE = instance

                return instance

            }
        }
    }
}