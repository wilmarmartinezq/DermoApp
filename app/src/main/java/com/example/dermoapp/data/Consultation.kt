package com.example.dermoapp.data

import android.os.Parcel
import android.os.Parcelable


data class Consultation(
    val id: String,
    val shape: String,
    val numberOfInjuries: String,
    val distribution: String,
    val comment: String,
    val image: String,
    val creationDate: String,
    val typeOfInjury: String,
    val specialty: String,
    val diagnosis: String,
    val asigned: Boolean,
    val acceptDiagnosis: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(shape)
        parcel.writeString(numberOfInjuries)
        parcel.writeString(distribution)
        parcel.writeString(comment)
        parcel.writeString(image)
        parcel.writeString(creationDate)
        parcel.writeString(typeOfInjury)
        parcel.writeString(specialty)
        parcel.writeString(diagnosis)
        parcel.writeByte(if (asigned) 1 else 0)
        parcel.writeString(acceptDiagnosis)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Consultation> {
        override fun createFromParcel(parcel: Parcel): Consultation {
            return Consultation(parcel)
        }

        override fun newArray(size: Int): Array<Consultation?> {
            return arrayOfNulls(size)
        }
    }
}

