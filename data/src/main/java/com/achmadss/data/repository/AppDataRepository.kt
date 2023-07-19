package com.achmadss.data.repository

import android.util.Log
import com.achmadss.data.local.room.AppDataDao
import com.achmadss.domain.entity.Contacts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AppDataRepository(
    private val appDataDao: AppDataDao
) {

    suspend fun insertNewData(listAppData: List<Contacts>): Flow<Unit> = flow {
        appDataDao.insertData(listAppData)
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    suspend fun updateData(contacts: Contacts): Flow<Boolean> = flow {
        val result = appDataDao.updateData(contacts)
        Log.e("UPDATE", result.toString())
        if (result > 0) emit(true)
        else emit(false)
    }.flowOn(Dispatchers.IO)

    suspend fun getAllData(): Flow<List<Contacts>> = flow {
        emit(appDataDao.getAllData())
    }.flowOn(Dispatchers.IO)

    fun getDataById(id: String): Flow<Contacts> = flow<Contacts> {
        emit(appDataDao.getDataById(id))
    }.flowOn(Dispatchers.IO)

}