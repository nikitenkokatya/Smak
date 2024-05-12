package com.example.smak.data

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

@SuppressLint("ParcelCreator")
data class RecetaAPIDetails(
    val vegetarian: Boolean,
    val vegan: Boolean,
    val glutenFree: Boolean,
    val dairyFree: Boolean,
    val veryHealthy: Boolean,
    val cheap: Boolean,
    val veryPopular: Boolean,
    val sustainable: Boolean,
    val lowFodmap: Boolean,
    val weightWatcherSmartPoints: Int,
    val gaps: String,
    val preparationMinutes: Int,
    val cookingMinutes: Int,
    val aggregateLikes: Int,
    val healthScore: Int,
    val creditsText: String,
    val sourceName: String,
    val pricePerServing: Double,
    val extendedIngredients: List<ExtendedIngredient>,
    val id: Int,
    val title: String,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceUrl: String,
    val image: String,
    val imageType: String,
    val taste: Taste,
    val summary: String,
    val cuisines: List<String>,
    val dishTypes: List<String>,
    val diets: List<String>,
    val occasions: List<String>,
    val winePairing: WinePairing,
    val instructions: String,
    val analyzedInstructions: List<AnalyzedInstruction>,
    val spoonacularScore: Double,
    val spoonacularSourceUrl: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.createTypedArrayList(ExtendedIngredient)!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(Taste::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.readParcelable(WinePairing::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(AnalyzedInstruction)!!,
        parcel.readDouble(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (vegetarian) 1 else 0)
        parcel.writeByte(if (vegan) 1 else 0)
        parcel.writeByte(if (glutenFree) 1 else 0)
        parcel.writeByte(if (dairyFree) 1 else 0)
        parcel.writeByte(if (veryHealthy) 1 else 0)
        parcel.writeByte(if (cheap) 1 else 0)
        parcel.writeByte(if (veryPopular) 1 else 0)
        parcel.writeByte(if (sustainable) 1 else 0)
        parcel.writeByte(if (lowFodmap) 1 else 0)
        parcel.writeInt(weightWatcherSmartPoints)
        parcel.writeString(gaps)
        parcel.writeInt(preparationMinutes)
        parcel.writeInt(cookingMinutes)
        parcel.writeInt(aggregateLikes)
        parcel.writeInt(healthScore)
        parcel.writeString(creditsText)
        parcel.writeString(sourceName)
        parcel.writeDouble(pricePerServing)
        parcel.writeTypedList(extendedIngredients)
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeInt(readyInMinutes)
        parcel.writeInt(servings)
        parcel.writeString(sourceUrl)
        parcel.writeString(image)
        parcel.writeString(imageType)
        parcel.writeParcelable(taste, flags)
        parcel.writeString(summary)
        parcel.writeStringList(cuisines)
        parcel.writeStringList(dishTypes)
        parcel.writeStringList(diets)
        parcel.writeStringList(occasions)
        parcel.writeParcelable(winePairing, flags)
        parcel.writeString(instructions)
        parcel.writeTypedList(analyzedInstructions)
        parcel.writeDouble(spoonacularScore)
        parcel.writeString(spoonacularSourceUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecetaAPIDetails> {
        val TAG = "RecetaAPIDetails"
        override fun createFromParcel(parcel: Parcel): RecetaAPIDetails {
            return RecetaAPIDetails(parcel)
        }

        override fun newArray(size: Int): Array<RecetaAPIDetails?> {
            return arrayOfNulls(size)
        }
    }
}

data class ExtendedIngredient(
    val id: Int,
    val aisle: String,
    val image: String,
    val consistency: String,
    val name: String,
    val nameClean: String,
    val original: String,
    val originalName: String,
    val amount: Double,
    val unit: String,
    val meta: List<String>,
    val measures: Measures
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readParcelable(Measures::class.java.classLoader)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(aisle)
        parcel.writeString(image)
        parcel.writeString(consistency)
        parcel.writeString(name)
        parcel.writeString(nameClean)
        parcel.writeString(original)
        parcel.writeString(originalName)
        parcel.writeDouble(amount)
        parcel.writeString(unit)
        parcel.writeStringList(meta)
        parcel.writeParcelable(measures, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExtendedIngredient> {
        override fun createFromParcel(parcel: Parcel): ExtendedIngredient {
            return ExtendedIngredient(parcel)
        }

        override fun newArray(size: Int): Array<ExtendedIngredient?> {
            return arrayOfNulls(size)
        }
    }
}

data class Measures(
    val us: Measure,
    val metric: Measure
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Measure::class.java.classLoader)!!,
        parcel.readParcelable(Measure::class.java.classLoader)!!
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Measures> {
        override fun createFromParcel(parcel: Parcel): Measures {
            return Measures(parcel)
        }

        override fun newArray(size: Int): Array<Measures?> {
            return arrayOfNulls(size)
        }
    }
}

data class Measure(
    val amount: Double,
    val unitShort: String,
    val unitLong: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(amount)
        parcel.writeString(unitShort)
        parcel.writeString(unitLong)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Measure> {
        override fun createFromParcel(parcel: Parcel): Measure {
            return Measure(parcel)
        }

        override fun newArray(size: Int): Array<Measure?> {
            return arrayOfNulls(size)
        }
    }
}

data class Taste(
    val sweetness: Double,
    val saltiness: Double,
    val sourness: Double,
    val bitterness: Double,
    val savoriness: Double,
    val fattiness: Double,
    val spiciness: Double
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(sweetness)
        parcel.writeDouble(saltiness)
        parcel.writeDouble(sourness)
        parcel.writeDouble(bitterness)
        parcel.writeDouble(savoriness)
        parcel.writeDouble(fattiness)
        parcel.writeDouble(spiciness)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Taste> {
        override fun createFromParcel(parcel: Parcel): Taste {
            return Taste(parcel)
        }

        override fun newArray(size: Int): Array<Taste?> {
            return arrayOfNulls(size)
        }
    }
}

data class WinePairing(
    val pairingText: String,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(pairingText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WinePairing> {
        override fun createFromParcel(parcel: Parcel): WinePairing {
            return WinePairing(parcel)
        }

        override fun newArray(size: Int): Array<WinePairing?> {
            return arrayOfNulls(size)
        }
    }
}

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createTypedArrayList(Step)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeTypedList(steps)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AnalyzedInstruction> {
        override fun createFromParcel(parcel: Parcel): AnalyzedInstruction {
            return AnalyzedInstruction(parcel)
        }

        override fun newArray(size: Int): Array<AnalyzedInstruction?> {
            return arrayOfNulls(size)
        }
    }
}

data class Step(
    val number: Int,
    val step: String,
    val ingredients: List<IngredientAPI>,
    val equipment: List<Equipment>,
    val length: Length?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.createTypedArrayList(IngredientAPI)!!,
        parcel.createTypedArrayList(Equipment)!!,
        parcel.readParcelable(Length::class.java.classLoader)
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Step> {
        override fun createFromParcel(parcel: Parcel): Step {
            return Step(parcel)
        }

        override fun newArray(size: Int): Array<Step?> {
            return arrayOfNulls(size)
        }
    }
}

data class IngredientAPI(
    val id: Int,
    val name: String,
    val localizedName: String,
    val image: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(localizedName)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IngredientAPI> {
        override fun createFromParcel(parcel: Parcel): IngredientAPI {
            return IngredientAPI(parcel)
        }

        override fun newArray(size: Int): Array<IngredientAPI?> {
            return arrayOfNulls(size)
        }
    }
}

data class Equipment(
    val id: Int,
    val name: String,
    val localizedName: String,
    val image: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(localizedName)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Equipment> {
        override fun createFromParcel(parcel: Parcel): Equipment {
            return Equipment(parcel)
        }

        override fun newArray(size: Int): Array<Equipment?> {
            return arrayOfNulls(size)
        }
    }
}

data class Length(
    val number: Int,
    val unit: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Length> {
        override fun createFromParcel(parcel: Parcel): Length {
            return Length(parcel)
        }

        override fun newArray(size: Int): Array<Length?> {
            return arrayOfNulls(size)
        }
    }

}
