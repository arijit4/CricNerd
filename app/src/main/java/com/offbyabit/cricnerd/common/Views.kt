package com.offbyabit.cricnerd.common

//import androidx.compose.material3.icons
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.offbyabit.cricnerd.cricbuzz.Match
import com.offbyabit.cricnerd.screens.NavigationRoot
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchCard(
    navController: NavHostController,
    match: Match,
    screenFraction: Float = 1f
) {
    ElevatedCard(
        modifier = Modifier
            .width(
                LocalConfiguration.current.screenWidthDp.dp.times(
                    screenFraction
                )
            ),
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        onClick = {
            navController.navigate(NavigationRoot.MatchDetailScreen.passId(match.match_id))
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = match.heading,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(12.dp))

            match.participants.forEach { p ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CoilImage(
                        modifier = Modifier.size(18.dp),
                        loading = { CircularProgressIndicator() },
                        imageModel = p.flag_url, // loading a network image or local resource using an URL.
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                        ),
                        failure = {
                            /*Image(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.secondary,
                                        CircleShape
                                    ),
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = ""
                            )*/
                            Icon(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.secondary,
                                        CircleShape
                                    ),
                                imageVector = Icons.Rounded.Flag,
                                contentDescription = ""
                            )
                        }
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        text = p.full_name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = p.current_score,
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            val h = 18.dp
            when (match.match_type) {
                "recent" -> {
                    Text(
                        modifier = Modifier.height(h),
                        text = match.status,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                "upcoming" -> {
                    Text(
                        modifier = Modifier.height(h),
                        text = match.starts_at,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Cyan,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                else -> {
                    Text(
                        modifier = Modifier.height(h),
                        text = match.starts_at,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Green,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController, selected: Int) {
    val context = LocalContext.current
    NavigationBar {
        NavigationBarItem(
            selected = (selected == 0),
            onClick = { navController.navigate(NavigationRoot.MainScreen.route) },
            icon = { Icon(Icons.Rounded.Home, contentDescription = null) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = (selected == 1),
            onClick = { navController.navigate(NavigationRoot.MatchesScreen.route) },
            icon = {
                Icon(Icons.Rounded.Timeline, "")
            },
            label = { Text("Recent") }
        )

        NavigationBarItem(
            selected = (selected == 2),
            onClick = { navController.navigate(NavigationRoot.SettingScreen.route) },
            icon = {
                Icon(Icons.Rounded.Settings, "")
            },
            label = { Text("Settings") }
        )
    }
}