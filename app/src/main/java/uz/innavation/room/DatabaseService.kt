package uz.innavation.room

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import uz.innavation.models.Video

@Dao
interface DatabaseService {

    @Query("select * from Video")
    fun getAll(): List<Video>

    @Insert
    fun add(myClass: Video)

    @Update
    fun update(myClass: Video): Completable

}
