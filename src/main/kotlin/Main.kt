import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import compose.basecomponent.*
import kotlinx.coroutines.DelicateCoroutinesApi
import org.koin.core.KoinApplication
import viewmodel.SharedViewModel

@OptIn(DelicateCoroutinesApi::class)
@Composable
@Preview
fun App() {
    val sharedViewModel: SharedViewModel = viewModel<SharedViewModel>()
    val imageList by sharedViewModel.imageList.collectAsState()

    MaterialTheme {
        val imageInfoModifier = Modifier
            .padding(TopEndPaddingValues8)
            .width(300.dp)
            .height(160.dp)
            .zIndex(1.0f)
            .background(color = Color.White.copy(alpha = 0.2f), shape = AllRoundedCornerShape8)
            .padding(PaddingValues8)
        val imageItemModifier = Modifier
            .padding(TopPaddingValues16)
            .wrapContentSize()
            .clip(AllRoundedCornerShape16)
        val asyncImageModifier = Modifier.width(800.dp)
            .height(600.dp)
            .clip(AllRoundedCornerShape8)
            .zIndex(0.5f)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues16,
        ) {
            item {
                Button(onClick = {
                    sharedViewModel.refreshImages()
                }) {
                    Text(text = "Hello World")
                }
            }
            itemsIndexed(imageList) { index, image ->
                Box(
                    modifier = imageItemModifier
                ) {
                    Column(
                        modifier = imageInfoModifier
                            .align(Alignment.TopEnd)
                    ) {
                        LazyColumn {
                            item {
                                Text("经纬度信息")
                                Text(text = "latitude = ${image.imageGPSInfo?.latitude}")
                                Text(text = "longitude = ${image.imageGPSInfo?.longitude}")
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
                    AsyncImage(
                        model = image.filePath,
                        modifier = asyncImageModifier,
                        contentScale = ContentScale.FillBounds,
                        contentDescription = null,
                    )
                }
            }


        }
    }
}

fun main() = application {
    KoinApplication.init().modules(allModules)
    Window(title = "ImageShow", onCloseRequest = ::exitApplication) {
        App()
    }
}


