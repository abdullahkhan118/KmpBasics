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

import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Singleton

/**
 * Repository module for handling data operations.
 *
 * Collecting from the Flows in [PlantDao] is main-safe.  Room supports Coroutines and moves the
 * query execution off of the main thread.
 */

@Singleton
class PlantRepository (
//    private val plantDao: PlantDao
) {

    fun getPlants() = flow { emit(emptyList<Plant>()) }
//        plantDao.getPlants()

    fun getPlant(plantId: String) = flow { emit(Plant(plantId, "asdasd", "asda", 4)) }
//        plantDao.getPlant(plantId)

    fun getPlantsWithGrowZoneNumber(growZoneNumber: Int) = flow { emit(emptyList<Plant>()) }
//        plantDao.getPlantsWithGrowZoneNumber(growZoneNumber)

    companion object {

        // For Singleton instantiation
        val instance: PlantRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            PlantRepository(
//                plantDao
            )
        }
    }
}
