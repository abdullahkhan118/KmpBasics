/*
 * Copyright 2020 Google LLC
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

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Class that captures dimens used in Compose code.
 *
 * Disclaimer:
 * This approach doesn't consider multiple configurations. For that, an Ambient should be created.
 */
object Dimens {

    // Per Material Design specs:
    // https://material.io/design/components/buttons-floating-action-button.html#specs
    val FabMargin: Dp = 16.dp

    // Padding on the bottom of a screen so that the FAB won't overlap scrolled text.
    val FabBottomPadding: Dp = 72.dp

    val PlantDetailAppBarHeight: Dp = 278.dp

    val MarginNormal: Dp = 16.dp
    val MarginSmall: Dp = 8.dp
    val MarginExtraSmall: Dp = 4.dp

    val PaddingLarge: Dp = 48.dp

    val PlantItemImageHeight: Dp = 95.dp

    val CardSideMargin: Dp = 12.dp
    val CardBottomMargin: Dp = 26.dp
    val CardElevation: Dp = 2.dp
    val CardCornerRadius: Dp = 12.dp

    // Elevation for Plant Detail toolbar - used to convert to pixels when hiding/showing
    val ToolbarElevation: Dp = 5.dp

    // Plant List Header margin
    val HeaderMargin: Dp = 24.dp

    val GalleryHeaderMargin: Dp = 72.dp

    // Minimum height of plant detail page so that the collapsing toolbar can be shown on every page
    val PlantDescriptionMinHeight: Dp = 555.dp

    val PaddingSmall: Dp = MarginSmall

    val PaddingNormal: Dp = MarginNormal

    val ToolbarIconPadding: Dp = 12.dp

    val ToolbarIconSize: Dp = 32.dp
}
