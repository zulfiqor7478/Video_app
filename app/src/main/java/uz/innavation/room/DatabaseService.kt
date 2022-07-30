package uz.innavation.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import uz.innavation.models.Video

@Dao
interface DatabaseService {

    @Query("select * from Video where type = :type ")
    fun getAllVideo(type: Int): List<Video>

    @Insert
    fun addVideo(myClass: Video)


    @Delete
    fun deleteVideo(myClass: Video)


}
