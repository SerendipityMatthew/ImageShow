package compose.basecomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp


@Composable
fun CheckboxWithLabel(isChecked: Boolean, text: String, onCheckedChange: (Boolean) -> Unit) {
    Row(
        Modifier.wrapContentWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(4.dp))
            .background(color = MaterialTheme.colors.secondary, shape = RoundedCornerShape(4.dp))
            .padding(start = 8.dp, end = 8.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            isChecked,
            onCheckedChange = onCheckedChange
        )
        Spacer(Modifier.width(8.dp))
        Text(text = text, minLines = 1, overflow = TextOverflow.Ellipsis)
    }
}