package com.evince.useroperations.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.evince.useroperations.models.UserModel

@Database(entities = [UserModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usersDao(): UserDao
}