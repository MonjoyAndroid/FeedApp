package com.monjoy.feedapp.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LoginUserDetails")
data class LoginUser(
    @PrimaryKey
    @ColumnInfo(name = "UserId") val id: Long,
    @ColumnInfo(name = "UserName") var userName: String,
    @ColumnInfo(name = "Password") var password: String,
    @ColumnInfo(name = "IsCheckedRemember") var isCheckedRemember: Boolean

)
