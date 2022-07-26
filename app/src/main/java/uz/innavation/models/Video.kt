package uz.innavation.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Video(

    @PrimaryKey
    val uri: String,
    @ColumnInfo(name = "author")
    val lat: Long,
    @ColumnInfo(name = "image")
    val longitude: Long,
    @ColumnInfo(name = "title")
    val time: String,
    @ColumnInfo(name = "description")
    val date: String,

    ) : Serializable