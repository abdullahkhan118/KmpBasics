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

package io.github.abdullahkhan118.kmpbasics.screens.ui.gallery

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import io.github.abdullahkhan118.kmpbasics.screens.ui.Dimens
import io.github.abdullahkhan118.kmpbasics.screens.ui.plantlist.PhotoListItem
import io.github.abdullahkhan118.kmpbasics.data.UnsplashPhoto
import io.github.abdullahkhan118.kmpbasics.data.UnsplashPhotoUrls
import io.github.abdullahkhan118.kmpbasics.data.UnsplashUser
import io.github.abdullahkhan118.kmpbasics.viewmodels.GalleryViewModel
import kmpbasics.shared.generated.resources.Res
import kmpbasics.shared.generated.resources.gallery_title
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel = koinViewModel(),
    onPhotoClick: (UnsplashPhoto) -> Unit,
    onUpClick: () -> Unit,
) {
    GalleryScreen(
        plantPictures = viewModel.plantPictures,
        onPhotoClick = onPhotoClick,
        onUpClick = onUpClick,
        onPullToRefresh = viewModel::refreshData,
    ){}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GalleryScreen(
    plantPictures: Flow<PagingData<UnsplashPhoto>>,
    onPhotoClick: (UnsplashPhoto) -> Unit = {},
    onUpClick: () -> Unit = {},
    onPullToRefresh: () -> Unit,
    endRefresh: PullToRefreshState.() -> Unit,
) {
    Scaffold(
        topBar = {
            GalleryTopBar(onUpClick = onUpClick)
        },
    ) { padding ->

        val pullToRefreshState = rememberPullToRefreshState()

        if (pullToRefreshState.isAnimating) {
            onPullToRefresh()
        }

        val pagingItems: LazyPagingItems<UnsplashPhoto> =
            plantPictures.collectAsLazyPagingItems()

        LaunchedEffect(pagingItems.loadState) {
            when (pagingItems.loadState.refresh) {
                is LoadState.Loading -> Unit
                is LoadState.Error, is LoadState.NotLoading -> {
                    pullToRefreshState.endRefresh()
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(padding)
//                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(all = Dimens.CardSideMargin)
            ) {
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.id }
                ) { index ->
                    val photo = pagingItems[index] ?: return@items
                    PhotoListItem(photo = photo) {
                        onPhotoClick(photo)
                    }
                }
            }

//            PullToRefreshContainer(
//                modifier = Modifier.align(Alignment.TopCenter),
//                state = pullToRefreshState
//            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GalleryTopBar(
    onUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(stringResource(Res.string.gallery_title))
        },
        modifier = modifier.statusBarsPadding(),
        navigationIcon = {
            IconButton(onClick = onUpClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
    )
}

@Preview
@Composable
private fun GalleryScreenPreview(
    @PreviewParameter(GalleryScreenPreviewParamProvider::class) plantPictures: Flow<PagingData<UnsplashPhoto>>
) {
    GalleryScreen(
        plantPictures = plantPictures,
        onPhotoClick = {},
        onUpClick = {},
        onPullToRefresh = {},
        endRefresh = {})
}

private class GalleryScreenPreviewParamProvider :
    PreviewParameterProvider<Flow<PagingData<UnsplashPhoto>>> {

    override val values: Sequence<Flow<PagingData<UnsplashPhoto>>> =
        sequenceOf(
            flowOf(
                PagingData.from(
                    listOf(
                        UnsplashPhoto(
                            id = "1",
                            urls = UnsplashPhotoUrls("https://images.unsplash.com/photo-1417325384643-aac51acc9e5d?q=75&fm=jpg&w=400&fit=max"),
                            user = UnsplashUser("John Smith", "johnsmith")
                        ),
                        UnsplashPhoto(
                            id = "2",
                            urls = UnsplashPhotoUrls("https://images.unsplash.com/photo-1417325384643-aac51acc9e5d?q=75&fm=jpg&w=400&fit=max"),
                            user = UnsplashUser("Sally Smith", "sallysmith")
                        )
                    )
                )
            ),
        )
}
