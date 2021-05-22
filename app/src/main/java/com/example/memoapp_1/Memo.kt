package com.example.memoapp_1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "memoTable")
data class Memo(
        @PrimaryKey(autoGenerate = true)
        var id: Int?,
        @ColumnInfo(name = "title")
        var title: String?,
        @ColumnInfo(name = "contents")
        var contents: String?,
        @ColumnInfo(name = "taginfo")
        var taginfo: String

)