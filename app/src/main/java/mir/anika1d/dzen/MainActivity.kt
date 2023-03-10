@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package mir.anika1d.dzen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import mir.anika1d.dzen.composable.layout.gridItem.GridItem
import mir.anika1d.dzen.composable.layout.shimmer.ShimmerBox
import mir.anika1d.dzen.composable.layout.text.ResponsiveText
import mir.anika1d.dzen.ui.theme.DzenTheme
import mir.anika1d.dzen.ui.theme.LocalSpacing
import mir.anika1d.dzen.ui.theme.spacing

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DzenTheme(true) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    MaterialTheme.colorScheme.background,
                                ), title =
                                {
                                    ResponsiveText(
                                        text = stringResource(id = R.string.hint_notification),
                                        style = TextStyle(
                                            fontWeight = FontWeight.W400,
                                            fontSize = 16.sp,
                                            lineHeight = 20.sp,
                                            color = Color.Gray
                                        ),
                                        maxLines = 3
                                    )
                                },
                                actions = {
                                    Button(
                                        onClick = { /*TODO*/ },
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.later),
                                            style = TextStyle(
                                                fontWeight = FontWeight.W400,
                                                fontSize = 16.sp,
                                                lineHeight = 20.sp,
                                                color = MaterialTheme.colorScheme.tertiary
                                            ),
                                            maxLines = 3
                                        )
                                    }
                                })
                        }
                    ) { padding ->
                        val listText = remember {
                            mutableStateListOf<Hobbies>()
                        }
                        var count by remember {
                            mutableStateOf(0)
                        }
                        var shimmer by remember {
                            mutableStateOf(true)
                        }
                        val lazyState = rememberLazyGridState()
                        LaunchedEffect(key1 = true, block = {
                            listText.addAll(getHobbies())
                            delay(500)
                            shimmer = false
                        })
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = MaterialTheme.spacing.small),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            LazyVerticalGrid(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(1f)
                                    .padding(
                                        vertical = padding.calculateTopPadding(),
                                        horizontal = MaterialTheme.spacing.medium
                                    ),
                                state = lazyState,
                                columns = GridCells.Fixed(2),
                                verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium),
                                horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium),
                            ) {
                                if (shimmer)
                                    items(10) {
                                        AnimatedVisibility(
                                            visible = listText.isEmpty(),
                                            exit = slideOutVertically(tween(500)) + fadeOut(
                                                tween(
                                                    500
                                                )
                                            )
                                        ) {
                                            ShimmerBox(
                                                modifier = Modifier
                                                    .width(80.dp)
                                                    .height(50.dp)
                                                    .clip(RoundedCornerShape(35.dp))
                                            )
                                        }
                                    }
                                items(listText.size, key = { listText[it].id }) { i ->
                                    AnimatedVisibility(
                                        visible = !shimmer,
                                        modifier = Modifier.animateItemPlacement(
                                            tween(1000)
                                        ),
                                        enter = fadeIn(tween(500)) + expandIn(tween(500)),
                                    ) {
                                        GridItem(
                                            text = listText[i].text,
                                            value = listText[i].selected,
                                            onValueChange = {
                                                listText[i].selected = it
                                                if (it) count += 1
                                                else count -= 1
                                                val tmp = listText[i]
                                                if (it) {
                                                    for (j in i - 1 downTo 0) {
                                                        listText[j + 1] = listText[j]
                                                    }
                                                    listText[0] = tmp
                                                } else {
                                                    var indexTmp = i
                                                    if (i != listText.lastIndex
                                                        && !(i == 0 && !listText[1].selected)
                                                    )
                                                        do {
                                                            listText[indexTmp] =
                                                                listText[indexTmp + 1]
                                                            listText[indexTmp + 1] = tmp
                                                            indexTmp += 1
                                                        } while (indexTmp + 1 <= listText.lastIndex &&
                                                            listText[indexTmp + 1].selected
                                                        )
                                                }
                                            },
                                            modifier = Modifier
                                                .width(80.dp)
                                                .height(50.dp)
                                                .clip(RoundedCornerShape(35)),
                                            selectedBackground = MaterialTheme.colorScheme.secondary,
                                            unSelectedBackground = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                            AnimatedVisibility(
                                visible = count != 0,
                                enter = scaleIn(tween(1000)),
                                exit = scaleOut(tween(1000))
                            ) {
                                Button(
                                    onClick = { /*TODO*/ },
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary)
                                ) {
                                    ResponsiveText(
                                        text = stringResource(id = R.string.next),
                                        style = TextStyle(
                                            fontWeight = FontWeight.W500,
                                            fontSize = 18.sp,
                                            lineHeight = 22.sp,
                                            color = Color.Black
                                        )
                                    )
                                }

                            }
                        }

                    }
                }
            }
        }
    }
}

data class Hobbies(
    val text: String,
    var selected: Boolean,
    var id: Long,
) {
    override fun toString(): String {
        return "$text || $selected  || $id"
    }
}

suspend fun getHobbies(): SnapshotStateList<Hobbies> {
    delay(2000)
    return mutableStateListOf(
        Hobbies("????????????", false, 1),
        Hobbies("????????????????", false, 2),
        Hobbies("??????????", false, 3),
        Hobbies("??????", false, 4),
        Hobbies("???????????????????? ????????", false, 5),
        Hobbies("????????", false, 6),
        Hobbies("????????", false, 7),
        Hobbies("????????????", false, 8),
        Hobbies("????????????????", false, 9),
        Hobbies("??????????", false, 10),
        Hobbies("??????", false, 11),
        Hobbies("???????????????????? ????????", false, 12),
        Hobbies("????????", false, 13),
        Hobbies("????????", false, 14),
        Hobbies("????????????", false, 15),
        Hobbies("????????????????", false, 16),
        Hobbies("??????????", false, 17),
        Hobbies("??????", false, 18),
        Hobbies("???????????????????? ????????", false, 19),
        Hobbies("????????", false, 20),
        Hobbies("????????", false, 21),
        Hobbies("????????????????", false, 22),
        Hobbies("??????????", false, 23),
        Hobbies("??????", false, 24),
        Hobbies("???????????????????? ????????", false, 25),
        Hobbies("????????", false, 26),
        Hobbies("????????", false, 27),
        Hobbies("????????????", false, 28),
        Hobbies("????????????????", false, 29),
        Hobbies("??????????", false, 30),
    )

}