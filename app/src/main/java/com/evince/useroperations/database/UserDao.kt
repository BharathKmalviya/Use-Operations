package com.evince.useroperations.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.evince.useroperations.models.UserModel

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAllUsers(): List<UserModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(channel: UserModel)

    @Update
    fun updateUser(channel: UserModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUserAll(channel: List<UserModel>)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Int):UserModel?

    @Delete
    fun deleteUser(userModel: UserModel)
}