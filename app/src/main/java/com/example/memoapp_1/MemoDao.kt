package com.example.memoapp_1

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MemoDao{
    @Query("SELECT * FROM memoTable ORDER BY id DESC")
    fun getAll_live(): LiveData<List<Memo>>
    //fun getAll(): List<Memo>

    @Query("SELECT * FROM memoTable ORDER BY id DESC")
    fun getAll(): List<Memo>

    @Query("SELECT * FROM memoTable WHERE id =:target_id ")
    fun getByid(target_id:Int) : Memo?

    @Query("SELECT taginfo FROM memoTable WHERE id =:target_id")
    fun getTaginfo(target_id: Int) : String

    @Query("SELECT COUNT(id) FROM memoTable")
    fun count() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memo: Memo)

    @Update
    fun update(memo: Memo)

    @Delete
    fun delete(memo: Memo)

    @Query("DELETE FROM memoTable")
    fun deleteAll()
}