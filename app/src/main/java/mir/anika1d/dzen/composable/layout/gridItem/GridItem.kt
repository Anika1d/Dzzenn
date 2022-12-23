package mir.anika1d.dzen.composable.layout.gridItem

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mir.anika1d.dzen.R
import mir.anika1d.dzen.composable.layout.text.ResponsiveText
import mir.anika1d.dzen.ui.theme.spacing

@Composable
fun GridItem(
    modifier: Modifier = Modifier,
    text: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    contentColor: Color = MaterialTheme.colorScheme.tertiary,
    selectedBackground: Color,
    unSelectedBackground: Color
) {
    var valueState by remember {
        mutableStateOf(value)
    }
    var prevValueState = remember {
        mutableStateOf<Int>(0)
    }
    var textState by remember {
        mutableStateOf(false)
    }
    val rotation by remember {
        mutableStateOf(androidx.compose.animation.core.Animatable(if (value) 0f else 360f))
    }
    val color by remember {
        mutableStateOf(Animatable(if (value) selectedBackground else unSelectedBackground))
    }
    LaunchedEffect(key1 = valueState, block = {
        if (prevValueState.value == 1) {
            rotation.animateTo(
                targetValue = if (valueState) 0f else 360f,
                animationSpec = tween(durationMillis = 1000),
            )
            color.animateTo(if (valueState) selectedBackground else unSelectedBackground)
            prevValueState.value += 1
        }
    })
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .graphicsLayer {
                rotationZ = rotation.value
            }
            .background(color.value)
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
        ) {
            Column(
                modifier = Modifier.animateContentSize(
                    animationSpec = tween(durationMillis = 1000),
                    finishedListener = { _, _ ->
                        textState = false
                    }),
            ) {
                ResponsiveText(
                    text = if (text.length > 10) text.substring(0..1) + " ... " + text.substring(
                        text.lastIndex - 1..text.lastIndex
                    )
                    else text,
                    style = TextStyle(
                        color = contentColor,
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight(590),
                        textAlign = TextAlign.Center
                    ),

                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.small)
                        .clickable {
                            if (text.length > 10)
                                textState = true
                        }
                )
                AnimatedVisibility(
                    visible = textState,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkHorizontally(),
                ) {
                    ResponsiveText(
                        text = text,
                        style = TextStyle(
                            color = contentColor,
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight(590)
                        ),
                        modifier = Modifier
                            .padding(horizontal = MaterialTheme.spacing.small),
                        maxLines = 1
                    )
                }

            }

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Divider(
                        modifier = Modifier
                            .height((rotation.value / 20f).dp)
                            .width((rotation.value / 360f).dp)
                            .animateContentSize(), color = Color(0x45FFFFFF)
                    )
                    IconButton(
                        onClick = {
                            prevValueState.value = 1
                            valueState = !valueState
                            onValueChange.invoke(valueState)
                        },
                        modifier = Modifier.wrapContentSize()

                    ) {
                        Icon(
                            painter = painterResource(id = if (valueState) R.drawable.ic_check_mark else R.drawable.ic_plus),
                            contentDescription = null,
                            tint = contentColor
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0L)
@Composable
fun PreviewGridItem() {
    GridItem(
        modifier = Modifier
            .height(40.dp)
            .clip(RoundedCornerShape(34))
            .background(MaterialTheme.colorScheme.primary),
        text = "WORK",
        value = true,
        onValueChange = {},
        selectedBackground = MaterialTheme.colorScheme.secondary,
        unSelectedBackground = MaterialTheme.colorScheme.primary
    )
}