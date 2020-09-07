package com.example.moviecatalog.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviecatalog.model.Element

@Dao
interface ElementDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(element: Element)

    @Query("SELECT * FROM WATCH_LIST ORDER BY name")
    fun getWatchList(): LiveData<List<Element>>

    @Query("SELECT * FROM WATCH_LIST WHERE id = :id")
    fun getElementById(id: Long): Element?

    @Delete
    fun delete(element: Element)
}
