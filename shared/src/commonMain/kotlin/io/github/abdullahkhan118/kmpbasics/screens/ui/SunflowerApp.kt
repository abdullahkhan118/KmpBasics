/*
 * Copyright 2023 Google LLC
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

package io.github.abdullahkhan118.kmpbasics.screens.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import io.github.abdullahkhan118.kmpbasics.BackStack
import io.github.abdullahkhan118.kmpbasics.Screen
import io.github.abdullahkhan118.kmpbasics.SinglePaneSceneStrategy
import io.github.abdullahkhan118.kmpbasics.screens.ui.gallery.GalleryScreen
import io.github.abdullahkhan118.kmpbasics.screens.ui.home.HomeScreen
import io.github.abdullahkhan118.kmpbasics.screens.ui.plantdetail.PlantDetailsScreen

@Composable
fun SunflowerApp(backStack: BackStack<Screen>) {
    SunFlowerNavHost(backStack)
}

@Composable
fun SunFlowerNavHost(backStack: BackStack<Screen>) {
    val entries = remember { backStack.entries }
    NavDisplay(
        backStack = entries,
        onBack = { backStack.pop() },
        sceneStrategies = listOf(
//                rememberDoublePaneSceneStrategy(),
            SinglePaneSceneStrategy(),
        ),
        entryProvider = entryProvider {
            entry<Screen.Home> {
                HomeScreen(
                    onPlantClick = {
                        backStack.push(
                            Screen.PlantDetail(
                                it.plantId
                            )
                        )
                    }
                )
            }
            entry<Screen.PlantDetail> { screen ->
                PlantDetailsScreen(
                    onBackClick = { backStack.pop() },
                    onShareClick = {
//                        createShareIntent(activity, it)
                    },
                    onGalleryClick = {
                        backStack.push(
                            Screen.Gallery(
                                plantName = it.name
                            )
                        )
                    }
                )
            }
            entry<Screen.Gallery> { screen ->
                GalleryScreen(
                    onPhotoClick = {
//                        val uri = Uri.parse(it.user.attributionUrl)
//                        val intent = Intent(Intent.ACTION_VIEW, uri)
//                        activity.startActivity(intent)
                    },
                    onUpClick = {
                        backStack.pop()
                    })
            }
        })
}

// Helper function for calling a share functionality.
// Should be used when user presses a share button/menu item.
//private fun createShareIntent(activity: Activity, plantName: String) {
//    val shareText = activity.getString(Res.string.share_text_plant, plantName)
//    val shareIntent = ShareCompat.IntentBuilder(activity)
//        .setText(shareText)
//        .setType("text/plain")
//        .createChooserIntent()
//        .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
//    activity.startActivity(shareIntent)
//}