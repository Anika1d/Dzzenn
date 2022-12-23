package mir.anika1d.dzen.composable.layout.text

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit


@Composable
fun ResponsiveText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle,
    color: Color = style.color,
    textAlign: TextAlign = TextAlign.Start,
    textDecoration: TextDecoration = TextDecoration.None,
    targetTextSizeHeight: TextUnit = style.fontSize,
    maxLines: Int = 1,
    lineHeight: TextUnit = style.lineHeight
) {
    var textSize by remember { mutableStateOf(targetTextSizeHeight) }

    Text(
        modifier = modifier,
        text = text,
        color = color,
        textAlign = textAlign,
        fontSize = textSize,
        fontFamily = style.fontFamily,
        fontStyle = style.fontStyle,
        fontWeight = style.fontWeight,
        lineHeight = lineHeight,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

            if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex) ||
                textLayoutResult.didOverflowHeight || textLayoutResult.didOverflowWidth
            ) {
                textSize = textSize.times(0.95f)
            }
        },
        textDecoration = textDecoration
    )
}

