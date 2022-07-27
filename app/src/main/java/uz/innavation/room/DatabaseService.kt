package uz.innavation.room

import androidx.room.*
import io.reactivex.Completable
import uz.innavation.models.Video

@Dao
interface DatabaseService {

    @Query("select * from Video")
    fun getAllVideo(): List<Video>

    @Insert
    fun addVideo(myClass: Video)


    @Query("select * from Video")
    fun getAllTwoMinuteVideo(): List<Video>

    @Insert
    fun addTwoMinuteVideo(myClass: Video)

    @Update
    fun update(myClass: Video): Completable

    @Delete
    fun deleteVideo(myClass: Video)



}
