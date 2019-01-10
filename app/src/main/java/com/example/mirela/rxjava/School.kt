package com.example.mirela.rxjava

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.reactivex.internal.operators.flowable.FlowableDistinct

data class School(
    @SerializedName("settings_code") val settingsCode: String,
    @SerializedName("district") val district: String,
    @SerializedName("state") val state: String,
    @SerializedName("logo_url") val logo: String,
    @SerializedName("android_enabled") val enable: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(settingsCode)
        parcel.writeString(district)
        parcel.writeString(state)
        parcel.writeString(logo)
        parcel.writeByte(if (enable) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<School> {
        override fun createFromParcel(parcel: Parcel): School {
            return School(parcel)
        }

        override fun newArray(size: Int): Array<School?> {
            return arrayOfNulls(size)
        }
    }

}