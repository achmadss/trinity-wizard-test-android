package com.achmadss.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.achmadss.domain.entity.Contacts

@Dao
interface AppDataDao {

    @Query("SELECT * FROM tb_app_data")
    fun getAllData(): List<Contacts>

    @Query("SELECT * FROM tb_app_data WHERE id=:id")
    fun getDataById(id: String): Contacts

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(listAppData: List<Contacts>)

    @Update
    fun updateData(contacts: Contacts): Int

}