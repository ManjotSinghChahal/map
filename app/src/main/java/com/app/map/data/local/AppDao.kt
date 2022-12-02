package com.app.map.data.local

import androidx.room.*

@Dao
interface AppDao {
    @Query("select * from AppEntities where id=:bookId")
    fun fetchSpecificLoc(bookId: String?): AppEntities

    @Query("select * from AppEntities")
    fun fetchAllLocations(): List<AppEntities>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: AppEntities?)

    @Update
    fun update(record: AppEntities?)

    @Query("delete from AppEntities where id=:id  ")
    fun delete(id: String?)

}