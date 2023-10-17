package com.monjoy.feedapp.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.net.PasswordAuthentication

@Entity(tableName = "UserProfileDetails", indices = [Index(value = ["UserName"], unique = true)])
data class UserProfile (
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "UserId") val id: Long?,
        @ColumnInfo(name = "UserName") var userName: String,
        @ColumnInfo(name = "Password") var password: String
        )