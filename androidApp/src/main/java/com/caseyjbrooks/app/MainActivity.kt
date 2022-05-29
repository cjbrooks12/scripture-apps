package com.caseyjbrooks.app

import androidx.activity.compose.BackHandler
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.caseyjbrooks.app.ui.RouterContent
import com.caseyjbrooks.app.utils.ComposeActivity
import com.copperleaf.ballast.router.RouterContract
import com.copperleaf.scripturenow.ScriptureNowDatabase
import com.copperleaf.scripturenow.di.Injector
import com.copperleaf.scripturenow.routing.Destinations
import com.copperleaf.scripturenow.routing.currentDestination
import com.copperleaf.scripturenow.routing.currentRoute
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ktor.client.engine.okhttp.OkHttp

class MainActivity : ComposeActivity() {

    private val injector: Injector = Injector(
        OkHttp,
        AndroidSqliteDriver(ScriptureNowDatabase.Schema, this, "scripture_now.db"),
        onBackstackEmptied = {
            this@MainActivity.finish()
        }
    )

    @Composable
    override fun ScreenContent() {
        val routerViewModel = injector.mainRouter

        val routerState by routerViewModel.observeStates().collectAsState()

        BackHandler {
            routerViewModel.trySend(
                RouterContract.Inputs.GoBack
            )
        }

        Scaffold(
            bottomBar = {
                val items = listOf(
                    Destinations.App.Home,
                    Destinations.App.VerseOfTheDay,
                    Destinations.App.Verses,
                )
                BottomNavigation {
                    items.forEach { screen ->
                        BottomNavigationItem(
                            icon = {
                                when (screen) {
                                    is Destinations.App.Home -> {
                                        Icon(Icons.Default.Home, contentDescription = "Home")
                                    }
                                    is Destinations.App.VerseOfTheDay -> {
                                        Icon(Icons.Default.Home, contentDescription = "VOTD")
                                    }
                                    is Destinations.App.Verses -> {
                                        Icon(Icons.Default.Home, contentDescription = "Verses")
                                    }
                                    else -> {
                                        Text("Error, not a valid top-level route")
                                    }
                                }
                            },
                            label = {
                                when (screen) {
                                    is Destinations.App.Home -> {
                                        Text("Home")
                                    }
                                    is Destinations.App.VerseOfTheDay -> {
                                        Text("VOTD")
                                    }
                                    is Destinations.App.Verses -> {
                                        Text("Verses")
                                    }
                                    else -> {
                                        Text("Error, not a valid top-level route")
                                    }
                                }
                            },
                            selected = screen.destination() == routerState.currentDestination,
                            onClick = {
                                routerViewModel.trySend(
                                    RouterContract.Inputs.GoToDestination(screen.destination())
                                )
                            }
                        )
                    }
                }
            },
            content = {
                routerState.currentDestination?.currentRoute?.let {
                    RouterContent(it, injector)
                }
            }
        )
    }
}




/*


702.2, Deathtouch,
702.3, Defender,
702.4, Double Strike,
702.5, Enchant,
702.6, Equip,
702.7, First Strike,
702.8, Flash,
702.9, Flying,
702.10, Haste,
702.11, Hexproof,
702.12, Indestructible,
702.13, Intimidate,
702.14, Landwalk,
702.15, Lifelink,
702.16, Protection,
702.17, Reach,
702.18, Shroud,
702.19, Trample,
702.20, Vigilance,
702.21, Ward,
702.22, Banding,
702.23, Rampage,
702.24, Cumulative Upkeep,
702.25, Flanking,
702.26, Phasing,
702.27, Buyback,
702.28, Shadow,
702.29, Cycling,
702.30, Echo,
702.31, Horsemanship,
702.32, Fading,
702.33, Kicker,
702.34, Flashback,
702.35, Madness,
702.36, Fear,
702.37, Morph,
702.38, Amplify,
702.39, Provoke,
702.40, Storm,
702.41, Affinity,
702.42, Entwine,
702.43, Modular,
702.44, Sunburst,
702.45, Bushido,
702.46, Soulshift,
702.47, Splice,
702.48, Offering,
702.49, Ninjutsu,
702.50, Epic,
702.51, Convoke,
702.52, Dredge,
702.53, Transmute,
702.54, Bloodthirst,
702.55, Haunt,
702.56, Replicate,
702.57, Forecast,
702.58, Graft,
702.59, Recover,
702.60, Ripple,
702.61, Split Second,
702.62, Suspend,
702.63, Vanishing,
702.64, Absorb,
702.65, Aura Swap,
702.66, Delve,
702.67, Fortify,
702.68, Frenzy,
702.69, Gravestorm,
702.70, Poisonous,
702.71, Transfigure,
702.72, Champion,
702.73, Changeling,
702.74, Evoke,
702.75, Hideaway,
702.76, Prowl,
702.77, Reinforce,
702.78, Conspire,
702.79, Persist,
702.80, Wither,
702.81, Retrace,
702.82, Devour,
702.83, Exalted,
702.84, Unearth,
702.85, Cascade,
702.86, Annihilator,
702.87, Level Up,
702.88, Rebound,
702.89, Totem Armor,
702.90, Infect,
702.91, Battle Cry,
702.92, Living Weapon,
702.93, Undying,
702.94, Miracle,
702.95, Soulbond,
702.96, Overload,
702.97, Scavenge,
702.98, Unleash,
702.99, Cipher,
702.100, Evolve,
702.101, Extort,
702.102, Fuse,
702.103, Bestow,
702.104, Tribute,
702.105, Dethrone,
702.106, Hidden Agenda,
702.107, Outlast,
702.108, Prowess,
702.109, Dash,
702.110, Exploit,
702.111, Menace,
702.112, Renown,
702.113, Awaken,
702.114, Devoid,
702.115, Ingest,
702.116, Myriad,
702.117, Surge,
702.118, Skulk,
702.119, Emerge,
702.120, Escalate,
702.121, Melee,
702.122, Crew,
702.123, Fabricate,
702.124, Partner,
702.125, Undaunted,
702.126, Improvise,
702.127, Aftermath,
702.128, Embalm,
702.129, Eternalize,
702.130, Afflict,
702.131, Ascend,
702.132, Assist,
702.133, Jump-Start,
702.134, Mentor,
702.135, Afterlife,
702.136, Riot,
702.137, Spectacle,
702.138, Escape,
702.139, Companion,
702.140, Mutate,
702.141, Encore,
702.142, Boast,
702.143, Foretell,
702.144, Demonstrate,
702.145, Daybound and Nightbound,
702.146, Disturb,
702.147, Decayed,
702.148, Cleave,
702.149, Training,
702.150, Compleated,
702.151, Reconfigure,
702.152, Blitz,
702.153, Casualty,


 */
