package uz.innavation.room

import androidx.room.*
import io.reactivex.Completable
import uz.innavation.models.Video

@Dao
interface DatabaseService {

    @Query("select * from Video where type = :type ")
    fun getAllVideo(type:Int): List<Video>

    @Insert
    fun addVideo(myClass: Video)


    @Delete
    fun deleteVideo(myClass: Video)



}
