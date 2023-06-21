package hu.bme.aut.android.recipebook.data


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.parcelize.Parcelize
import java.io.Serializable
@Parcelize
@Entity(tableName = "recipes")
data class Recipe(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long?=null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "time") var timecost: Int,
    @ColumnInfo(name = "servings") var servings: Int,
    @ColumnInfo(name = "ingredients") var ingredients: String,
    @ColumnInfo(name = "instructions") var instructions: String,
    @ColumnInfo(name = "cousine") var cousine: Int,
    @ColumnInfo(name = "meatType") var meatType: Int,
    @ColumnInfo(name = "allergen") var allergen: String,
    //MILK, EGGS, NUTS, GLUTEN, SOY
    @ColumnInfo(name = "saved") var saved: Int, //1 if saved, 0 else

):Parcelable,Serializable {
    enum class MeatType {
        CHICKEN, //ordinal 0
        TURKEY,  //ordinal 1
        LAMB,   //ordinal 2
        PORK,   //ordinal 3
        BEEF,   //ordinal 4
        FISH,   //ordinal 5
        OTHER,  //ordinal 6
        NONE;   //ordinal 7

        companion object {
            @JvmStatic
            @TypeConverter
            fun getByOrdinal(ordinal: Int): MeatType? {
                var ret: MeatType? = null
                for (cat in values()) {
                    if (cat.ordinal == ordinal) {
                        ret = cat
                        break
                    }
                }
                return ret
            }

            @JvmStatic
            @TypeConverter
            fun toInt(category: MeatType): Int {
                return category.ordinal
            }
        }
    }

    enum class CousineType {
        HUNGARIAN,  //ordinal 0
        ITALIAN,    //ordinal 1
        EUROPIAN,   //ordinal 2
        AMERICAN,   //ordinal 3
        ASIAN;      //ordinal 4


        companion object {
            @JvmStatic
            @TypeConverter
            fun getByOrdinal(ordinal: Int): CousineType? {
                var ret: CousineType? = null
                for (cat in values()) {
                    if (cat.ordinal == ordinal) {
                        ret = cat
                        break
                    }
                }
                return ret
            }

            @JvmStatic
            @TypeConverter
            fun toInt(category: CousineType): Int {
                return category.ordinal
            }
        }
    }
}
