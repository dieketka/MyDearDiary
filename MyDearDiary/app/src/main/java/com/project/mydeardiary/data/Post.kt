package com.project.mydeardiary.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize
import java.text.DateFormat

@Entity(tableName = "post_table")
@VersionedParcelize
data class Post(
    val name: String?,
    val created: Long = System.currentTimeMillis(), //time when a post was created
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
    :Parcelable
{
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created) //formatting the code into date/hours/minutes

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeLong(created)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readLong(),
        parcel.readInt()
    )
    }
