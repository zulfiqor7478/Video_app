package uz.innavation.room

import androidx.room.*
import io.reactivex.Completable
import uz.innavation.models.TwoMinutesVideo
import uz.innavation.models.Video

@Dao
interface DatabaseService {

    @Query("select * from Video")
    fun getAllVideo(): List<Video>

    @Insert
    fun addVideo(myClass: Video)


    @Query("select * from TwoMinutesVideo")
    fun getAllTwoMinuteVideo(): List<TwoMinutesVideo>

    @Insert
    fun addTwoMinuteVideo(myClass: TwoMinutesVideo)

    @Update
    fun update(myClass: Video): Completable

    @Delete
    fun deleteVideo(myClass: Video)
    @Delete
    fun deleteTwoMinutesVideo(myClass: TwoMinutesVideo)



}
