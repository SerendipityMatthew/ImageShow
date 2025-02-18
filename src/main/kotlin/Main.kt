import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import compose.basecomponent.AllRoundedCornerShape16
import compose.basecomponent.AllRoundedCornerShape8
import compose.basecomponent.PaddingValues16
import compose.basecomponent.PaddingValues8
import compose.basecomponent.TopEndPaddingValues8
import compose.basecomponent.TopPaddingValues16
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinApplication
import viewmodel.SharedViewModel
import java.io.File

@Composable
@Preview
fun App(isShowWebview: MutableState<Boolean>) {
    val sharedViewModel: SharedViewModel = viewModel<SharedViewModel>()
    val imageList by sharedViewModel.imageList.collectAsState()
    val hazeState = remember { HazeState() }

    val cardStyle = HazeStyle(
        backgroundColor = Color.Black,
        tints = listOf(HazeTint(Color.Yellow.copy(alpha = 0.4f))),
        blurRadius = 8.dp,
        noiseFactor = HazeDefaults.noiseFactor,
    )
    val latitude = 35.8155031
    val longitude = 139.7097407
    val googleMapUrl = "https://www.google.com/maps/@$longitude,$latitude,14z"

    val htmlPath = File("src/main/resources/index.html").toURI().toString()
    val state = rememberWebViewState(url = htmlPath)
    state.webSettings.isJavaScriptEnabled = true
    val webViewNavigator = rememberWebViewNavigator()
    val coroutineScope = rememberCoroutineScope()
    MaterialTheme {
        val imageInfoModifier = Modifier
            .padding(TopEndPaddingValues8)
            .width(300.dp)
            .height(160.dp)
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

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues16,
        ) {
            item {

                if (isShowWebview.value) {
                    WebView(
                        state = state,
                        modifier =
                        Modifier
                            .width(800.dp).height(600.dp),
                        navigator = webViewNavigator,
                        onCreated = {

                            coroutineScope.launch {
                                delay(5000)
                                webViewNavigator.evaluateJavaScript(
                                    script = "addMarker($latitude,$longitude, 'New York');",
                                    callback = { state ->
                                        println("WebView WebView WebView state $state")
                                    })
                            }
                        }, onDispose = {

                        },
                        webViewJsBridge = null
                    )
                }

            }
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
    val isShowWebview = remember { mutableStateOf(false) }
    KoinApplication.init().modules(allModules)
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            KCEF.init(builder = {
                installDir(File("kcef-bundle"))
                progress {
                    onDownloading {
                    }
                    onInitialized {
                        isShowWebview.value = true
                    }
                }
                download {
                    github {
                        release("jbr-release-17.0.12b1207.37")
                    }
                }

                settings {
                    cachePath = File("cache").absolutePath
                }
            }, onError = {
                it?.printStackTrace()
            }, onRestartRequired = {
            })
        }
    }
    Window(title = "ImageShow", onCloseRequest = ::exitApplication) {
        App(isShowWebview)
    }


    DisposableEffect(Unit) {
        onDispose {
            KCEF.disposeBlocking()
        }
    }
}

