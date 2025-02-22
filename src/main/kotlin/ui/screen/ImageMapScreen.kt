package ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import component.ImageMapComponent
import viewmodel.ImageMapViewModel
import java.io.File

@Composable
fun ImageMapScreen(
    imageMapComponent: ImageMapComponent,
) {
    val htmlPath = File("src/main/resources/index.html").toURI().toString()
    val state = rememberWebViewState(url = htmlPath)
    state.webSettings.isJavaScriptEnabled = true
    val webViewNavigator = rememberWebViewNavigator()
    val imageMapViewModel: ImageMapViewModel = viewModel<ImageMapViewModel>()

    val markersJson by imageMapViewModel.imageGpsInfoGsonFlow.collectAsState()
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
                actions = {
                    Row(Modifier.wrapContentWidth()) {
                        IconButton(
                            onClick = {
                                webViewNavigator.evaluateJavaScript(
                                    script = "addMarkersAnimation('$markersJson')",
                                    callback = { state ->

                                    }
                                )
                            }) {
                            Text(text = "Show with animation")
                        }
                        Spacer(Modifier.width(20.dp))
                        IconButton(
                            onClick = {
                                webViewNavigator.evaluateJavaScript(
                                    script = "addMarkers('$markersJson')",
                                    callback = { state ->

                                    }
                                )
                            }
                        ) {
                            Text(text = "Show No animation")
                        }
                        Spacer(Modifier.width(20.dp))
                        IconButton(
                            onClick = {
                                webViewNavigator.evaluateJavaScript(
                                    script = "clearMarkers()",
                                    callback = { state ->

                                    }
                                )
                            }
                        ) {
                            Text(text = "clean all marker")
                        }
                        Spacer(Modifier.width(20.dp))
                        IconButton(
                            onClick = {
                                webViewNavigator.evaluateJavaScript(
                                    script = "pauseMarkers()",
                                    callback = { state ->

                                    }
                                )
                            }
                        ) {
                            Text(text = "pause marking")
                        }
                    }

                }
            )
        },
    ) {
        WebView(
            state = state,
            modifier = Modifier
                .fillMaxSize(),
            navigator = webViewNavigator,
            onCreated = {

            },
            onDispose = {

            },
        )

    }
}