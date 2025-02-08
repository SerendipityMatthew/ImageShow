import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
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
        Column(modifier = Modifier.fillMaxSize()) {
            Button(onClick = {}) {
                Text(text = "Hello World")
            }
            imageList.fastForEachIndexed { index, image ->
                Box(
                    modifier = Modifier.width(600.dp).height(800.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 8.dp, end = 8.dp)
                            .zIndex(1.0f)
                            .align(Alignment.TopEnd)
                            .background(color = Color.White.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Text("经纬度信息")
                        Text(text = "Latitude = ${image.imageGPSInfo?.latitude}")
                        Text(text = "longitude = ${image.imageGPSInfo?.longitude}")
                    }
                    AsyncImage(
                        model = image.filePath,
                        modifier = Modifier.fillMaxSize().background(
                            color = if (index == 0) {
                                Color.Blue
                            } else {
                                Color.Yellow
                            }
                        ).zIndex(0.5f),
                        contentDescription = null,
                    )
                }
            }

        }
    }
}

fun main() = application {
    KoinApplication.init().modules(allModules)
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}


