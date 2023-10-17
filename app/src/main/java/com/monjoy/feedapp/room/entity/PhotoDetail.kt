package com.monjoy.feedapp.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PhotoDetail")
data class PhotoDetail(
    @PrimaryKey
    @ColumnInfo(name = "Id") val id: Long,
    @ColumnInfo(name = "AlbumId") var albumId: String,
    @ColumnInfo(name = "Title") var title: String,
    @ColumnInfo(name = "Url") var url: String,
    @ColumnInfo(name = "ThumbnailUrl") var thumbnailUrl: String,
    @ColumnInfo(name = "IsLiked") var isLiked: Boolean
)
