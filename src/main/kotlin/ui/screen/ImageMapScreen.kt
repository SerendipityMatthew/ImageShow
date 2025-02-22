package ui.screen

import ImageMapComponent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import utils.Utils
import java.io.File

@Composable
fun ImageMapScreen(
    imageMapComponent: ImageMapComponent,
) {
    val htmlPath = File("src/main/resources/index.html").toURI().toString()
    val state = rememberWebViewState(url = htmlPath)
    state.webSettings.isJavaScriptEnabled = true
    val webViewNavigator = rememberWebViewNavigator()
    val coroutineScope = rememberCoroutineScope()

    val imageMapMarkerInfoList by Utils.imageGpsInfoFlow.collectAsState()

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Image Map Screen") },
                navigationIcon = {
                    IconButton(onClick = {
                        imageMapComponent.onShowMainScreenClicked()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back button",
                        )
                    }
                },
            )
        },
    ) {
        WebView(
            state = state,
            modifier = Modifier
                .fillMaxSize(),
            navigator = webViewNavigator,
            onCreated = {

                coroutineScope.launch {
                    delay(5000)

                    val markersJson = Json.encodeToString(imageMapMarkerInfoList)
                    webViewNavigator.evaluateJavaScript(
                        script = "addMarkers('$markersJson')",
                        callback = { state ->

                        }
                    )

                }
            },
            onDispose = {

            },
        )

    }
}