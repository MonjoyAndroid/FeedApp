package com.monjoy.feedapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.monjoy.feedapp.room.dao.LoginDetailsDao
import com.monjoy.feedapp.room.dao.PhotoDetailDao
import com.monjoy.feedapp.room.dao.UserProfileDao
import com.monjoy.feedapp.room.entity.LoginUser
import com.monjoy.feedapp.room.entity.PhotoDetail
import com.monjoy.feedapp.room.entity.UserProfile
import com.monjoy.feedapp.utils.Constant

@Database(entities = [UserProfile::class, LoginUser::class, PhotoDetail::class], version = Constant.DATABASE_VERSION, exportSchema = false)
abstract class FeedDatabase : RoomDatabase() {
    abstract fun mUserProfileDao(): UserProfileDao?
    abstract fun mLoginUserProfileDao(): LoginDetailsDao?
    abstract fun mPhotoDetailDao(): PhotoDetailDao

    companion object {
        @Volatile
        private var database: FeedDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): FeedDatabase? {
            if (database == null) {
                synchronized(FeedDatabase::class.java) {
                    if (database == null) {
                        database = Room.databaseBuilder(context.applicationContext, FeedDatabase::class.java, "Feed_DB")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return database
        }
    }
}