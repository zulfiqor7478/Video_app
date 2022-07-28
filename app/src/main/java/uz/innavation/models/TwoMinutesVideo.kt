package uz.innavation.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class TwoMinutesVideo(

    @PrimaryKey
    val uri: String,
    @ColumnInfo(name = "author")
    val lat: Double,
    @ColumnInfo(name = "image")
    val longitude: Double,
    @ColumnInfo(name = "title")
    val time: String,
    @ColumnInfo(name = "description")
    val date: String,

    ) : Serializable