package hu.bme.aut.android.recipebook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.recipebook.R
import hu.bme.aut.android.recipebook.data.Recipe
import hu.bme.aut.android.recipebook.fragments.ListFragmentDirections
import hu.bme.aut.android.recipebook.fragments.SavedFragmentDirections
import hu.bme.aut.android.recipebook.other.MyFilter

class RecipeAdapter(private var homescreen:Boolean, private var mycontext: Context): RecyclerView.Adapter<RecipeAdapter.MyViewHolder>(), Filterable {

    private var recipeList = emptyList<Recipe>()
    private var filteredrecipeList:ArrayList<Recipe> = arrayListOf()


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val recipeName: TextView
        val recipeImage: ImageView
        val listedItem: RelativeLayout

        init {
            recipeName = itemView.findViewById(R.id.recipeName)
            recipeImage = itemView.findViewById(R.id.recipeImage)
            listedItem = itemView.findViewById(R.id.listedItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.listed_item, parent, false))
    }

    override fun getItemCount(): Int {
        return filteredrecipeList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = filteredrecipeList[position]
        holder.recipeName.text =currentItem.name
        holder.recipeImage.setImageResource(R.drawable.food)

        holder.listedItem.setOnClickListener {
            if(homescreen){
                var action = ListFragmentDirections.actionListFragmentToDetailsFragment(currentItem,true)
                holder.itemView.findNavController().navigate(action)
            }
           else{
                var action = SavedFragmentDirections.actionSavedFragmentToDetailsFragment(currentItem,false)
                holder.itemView.findNavController().navigate(action)
            }

        }

    }
    fun filterRecipes(filter: MyFilter){
        if(filter.isEmpty()){
            filteredrecipeList.clear()
            filteredrecipeList = ArrayList<Recipe>(recipeList)
            notifyDataSetChanged()
            return
        }

        var filteredList = ArrayList<Recipe>()
        var filteredList2 = ArrayList<Recipe>()
        var filteredList3 = ArrayList<Recipe>()

        if(filter.isCousineFiltered()){
            for(item in recipeList){
                if(item.cousine == filter.getCousineFilters()){
                    filteredList.add(item)
                }
            }
            if(filter.isMeatFiltered()){
                for(item in filteredList){
                    if(item.meatType == filter.getMeatFilters()){
                        filteredList2.add(item)
                    }
                }
                if(filter.isAllergenFiltered()){
                    for(item in filteredList2){
                        if(!item.allergen.contains(filter.getAllergenFilters())){
                            filteredList3.add(item)
                        }
                    }
                }
            }
            else if(filter.isAllergenFiltered()){
                for(item in filteredList){
                    if(!item.allergen.contains(filter.getAllergenFilters())){
                        filteredList2.add(item)
                    }
                }
            }
        }
        //NO COUSINE FILTER
        else if(filter.isMeatFiltered()){
            for(item in recipeList){
                if(item.meatType == filter.getMeatFilters()){
                    filteredList.add(item)
                }
            }
            if(filter.isAllergenFiltered()){
                for(item in filteredList){
                    if(!item.allergen.contains(filter.getAllergenFilters())){
                        filteredList2.add(item)
                    }
                }
            }
        }
        //NO COUSINE FILTER, NO MEAT FILTER
        else if(filter.isAllergenFiltered()){
            for(item in recipeList){
                if(!item.allergen.contains(filter.getAllergenFilters())){
                    filteredList.add(item)
                }
            }
        }

        filteredrecipeList.clear()
        if(!filteredList3.isEmpty()){
            filteredrecipeList.addAll(filteredList3)
        }
        else if(!filteredList2.isEmpty()){
            filteredrecipeList.addAll(filteredList2)
        }
        else{
            filteredrecipeList.addAll(filteredList)
        }
        if(filteredList.isEmpty()){
            Toast.makeText(mycontext, "No recipes fit these filters!", Toast.LENGTH_LONG).show()
        }

        notifyDataSetChanged()

    }

    fun setData(recipes: List<Recipe>){
        this.filteredrecipeList = recipes as ArrayList<Recipe>
        this.recipeList = ArrayList<Recipe>(recipes)
        notifyDataSetChanged()
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                var filteredList = ArrayList<Recipe>()

                if(p0 == null || p0.length == 0){
                    filteredList.addAll(recipeList)
                }
                else{
                    var filterPattern = p0.toString().lowercase().trim()
                    for(item in recipeList){
                        if(item.name.lowercase().contains(filterPattern)){
                            filteredList.add(item)
                        }
                    }
                }
                var results = FilterResults()
                results.values = filteredList

                return results
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                if(p1?.values == null){
                    filteredrecipeList = ArrayList()
                }
                else{
                    filteredrecipeList = p1?.values as ArrayList<Recipe>
                }
                notifyDataSetChanged()
            }
        }
    }
    fun listIsEmpty():Boolean{
        return filteredrecipeList.isEmpty()
    }
}


