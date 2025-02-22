package ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import component.SingleImageComponent

@Composable
fun SingleImageScreen(singleImageComponent: SingleImageComponent) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Image Map Screen") },
                navigationIcon = {
                    IconButton(
                        onClick = singleImageComponent::onBackClicked
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back button",
                        )
                    }
                },
            )
        },
        content = {
            AsyncImage(
                model = singleImageComponent.imageMeta.filePath,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
        }
    )
}