package com.example.memoapp_1


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = arrayOf(Memo::class), version = 1, exportSchema = false)
public abstract class MemoDatabase: RoomDatabase(){
    abstract fun memoDao(): MemoDao

    companion object{

        @Volatile
        private var INSTANCE: MemoDatabase? = null
        fun getInstance(context:Context): MemoDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    MemoDatabase::class.java,
                    "memo_database"
                )   .enableMultiInstanceInvalidation()
                    .build()
            }
            return INSTANCE!! // as MemoDatabase
        }
    }
}