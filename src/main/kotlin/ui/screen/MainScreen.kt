package ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import component.MainComponent
import compose.basecomponent.*
import dev.chrisbanes.haze.*
import viewmodel.SharedViewModel

@Composable
fun MainScreen(
    mainComponent: MainComponent,
) {
    val sharedViewModel: SharedViewModel = viewModel<SharedViewModel>()
    val imageList by sharedViewModel.imageList.collectAsState()
    val hazeState = remember { HazeState() }

    val cardStyle = HazeStyle(
        backgroundColor = Color.Black,
        tints = listOf(HazeTint(Color.Yellow.copy(alpha = 0.4f))),
        blurRadius = 8.dp,
        noiseFactor = HazeDefaults.noiseFactor,
    )
    val isContainGPSInfo = remember { mutableStateOf(false) }

    MaterialTheme {
        val imageInfoModifier = Modifier
            .padding(TopEndPaddingValues8)
            .width(300.dp)
            .height(160.dp)
            .zIndex(1.0f)
            .background(color = Color.White.copy(alpha = 0.45f), shape = AllRoundedCornerShape8)
            .padding(PaddingValues8)
            .hazeEffect(state = hazeState, style = cardStyle)

        val imageIndexModifier = Modifier
            .padding(TopStartPaddingValues8)
            .wrapContentWidth()
            .height(40.dp)
            .zIndex(1.0f)
            .background(color = Color.White.copy(alpha = 0.45f), shape = AllRoundedCornerShape8)
            .padding(PaddingValues8)
            .hazeEffect(state = hazeState, style = cardStyle)
        val imageItemModifier = Modifier
            .padding(TopPaddingValues16)
            .wrapContentSize()
            .clip(AllRoundedCornerShape16)
        val asyncImageModifier = Modifier.width(800.dp)
            .height(600.dp)
            .clip(AllRoundedCornerShape8)
            .zIndex(0.5f)

        Column {
            Row(modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
                CheckboxWithLabel(
                    isContainGPSInfo.value,
                    text = "Is Contain GPS",
                    onCheckedChange = {
                        isContainGPSInfo.value = it
                        sharedViewModel.isContainGPS(it)
                    }
                )
            }
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues16,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                columns = GridCells.Adaptive(600.dp),
            ) {

                item {
                    Button(
                        onClick = {
                            mainComponent.onShowImageGPSScreenClicked()
                        }
                    ) {
                        Text(text = "Hello World")
                    }
                }
                itemsIndexed(items = imageList) { index, image ->
                    println("image.index.toString( image.index.toString( ${image.index}")
                    Box(
                        modifier = imageItemModifier
                    ) {
                        Box(
                            modifier = imageIndexModifier
                                .align(Alignment.TopStart)
                                .hazeEffect(state = hazeState, style = cardStyle)
                        ) {
                            Text(image.index.toString(), Modifier.align(Alignment.Center))
                        }
                        Box(
                            imageInfoModifier
                                .align(Alignment.TopEnd)
                                .hazeEffect(state = hazeState, style = cardStyle)
                        ) {
                            Column(
                                modifier = Modifier
                            ) {
                                LazyColumn(
                                    modifier = Modifier
                                ) {
                                    item {
                                        Text("经纬度信息")
                                        Text(text = "latitude = ${image.imageGPSInfo?.latitude}")
                                        Text(text = "longitude = ${image.imageGPSInfo?.longitude}")
                                        Button(
                                            onClick = {
                                                if (image.imageGPSInfo != null) {
                                                    val googleMapUrl =
                                                        "https://www.google.com/maps/@${image.imageGPSInfo.latitude},${image.imageGPSInfo.longitude},14z"
                                                    println("googleMapUrl googleMapUrl = $googleMapUrl ")
                                                }
                                            }) {
                                            Text(text = "Show GPS in Google Maps")
                                        }
                                        Text("Camera 信息")
                                        Text(text = "cameraMake = ${image.cameraBodyInfo?.cameraMake}")
                                        Text(text = "cameraModel = ${image.cameraBodyInfo?.cameraModel}")
                                        Text("Lens 信息")
                                        Text(text = "lensName = ${image.lensInfo?.name}")
                                        Text(text = "lensMake = ${image.lensInfo?.lensMake}")
                                        Text(text = "lensModel = ${image.lensInfo?.lensModel}")
                                    }
                                }

                            }
                        }
                        AsyncImage(
                            model = image.filePath,
                            modifier = asyncImageModifier.clickable { mainComponent.onShowSingleImageClick(image) },
                            contentScale = ContentScale.FillBounds,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}