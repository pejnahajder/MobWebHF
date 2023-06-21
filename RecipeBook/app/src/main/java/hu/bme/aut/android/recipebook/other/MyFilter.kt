package hu.bme.aut.android.recipebook.other

import android.widget.Filter
import hu.bme.aut.android.recipebook.data.Recipe

class MyFilter {
    private var cousineFilters: Int
    private var meatFilters: Int
    private var allergenFilters: String
    init {
        cousineFilters = -1
        meatFilters = -1
        allergenFilters = ""
    }
    fun isEmpty():Boolean{
        if(!isCousineFiltered() && !isMeatFiltered() && !isAllergenFiltered()) return true

        return false
    }
    fun add_cFilter(type: Int){
        cousineFilters = type
    }
    fun add_mFilter(type: Int){
        meatFilters = type
    }
    fun add_aFilter(allergen: String){
        allergenFilters = allergen
    }
    fun isCousineFiltered():Boolean{
        if(cousineFilters>-1) return true
        return false
    }
    fun isMeatFiltered():Boolean{
        if(meatFilters>-1) return true
        return false
    }
    fun isAllergenFiltered():Boolean{
        if(!allergenFilters.isEmpty()) return true
        return false
    }
    fun getCousineFilters():Int{
        return cousineFilters
    }
    fun getMeatFilters(): Int{
        return meatFilters
    }
    fun getAllergenFilters():String{
        return allergenFilters
    }
    fun toText():String{
        var output:String = ""
        if(isCousineFiltered()){
            output = "Cousine: ${Recipe.CousineType.getByOrdinal(cousineFilters)}"
        }
        if(isMeatFiltered()){
            if(output.isEmpty()){
                output = "Meat: ${Recipe.MeatType.getByOrdinal(meatFilters)}"
            }
            else{
                output = "${output}, Meat: ${Recipe.MeatType.getByOrdinal(meatFilters)}"
            }
        }
        if(isAllergenFiltered()){
            if(output.isEmpty()){
                output = "Free from: ${allergenFilters}"
            }
            else{
                output = "${output}, Free from: ${allergenFilters}"
            }
        }
        return output
    }
}