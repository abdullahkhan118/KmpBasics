/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.abdullahkhan118.kmpbasics.data

import androidx.room3.ColumnTypeConverters
import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

/**
 * The Room database for this app
 */

// shared/src/commonMain/kotlin/Database.kt

@Database(entities = [GardenPlanting::class, Plant::class], version = 1, exportSchema = false)
@ColumnTypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gardenPlantingDao(): GardenPlantingDao
    abstract fun plantDao(): PlantDao
}

// The Room compiler generates the `actual` implementations.

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

// shared/src/commonMain/kotlin/Database.kt

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}


//
//
//
//abstract class AppDatabase : RoomDatabase() {
//
//    companion object {
//
//        // For Singleton instantiation
//        @Volatile private var instance: AppDatabase? = null
//
//        fun getInstance(context: Context): AppDatabase {
//            return instance ?: synchronized(this) {
//                instance ?: buildDatabase(context).also { instance = it }
//            }
//        }
//
//        // Create and pre-populate the database. See this article for more details:
//        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
//        private fun buildDatabase(context: Context): AppDatabase {
//            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
//                .addCallback(
//                    object : RoomDatabase.Callback() {
//                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            super.onCreate(db)
//                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
//                                    .setInputData(workDataOf(KEY_FILENAME to PLANT_DATA_FILENAME))
//                                    .build()
//                            WorkManager.getInstance(context).enqueue(request)
//                        }
//                    }
//                )
//                .build()
//        }
//    }
//}
