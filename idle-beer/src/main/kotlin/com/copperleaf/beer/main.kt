package com.copperleaf.beer

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.singleWindowApplication
import com.copperleaf.beer.main.MainContract
import com.copperleaf.beer.main.MainViewModel
import com.copperleaf.beer.models.minus
import com.copperleaf.beer.models.plus

fun main() = singleWindowApplication {
    val coroutineScope = rememberCoroutineScope()
    val mainViewModel = remember(coroutineScope) { MainViewModel(coroutineScope) }

    val currentState by mainViewModel.observeStates().collectAsState()

    MaterialTheme {
        Column {
            val sendUpdate: ((MainContract.State) -> MainContract.State) -> Unit = { updateStateFn ->
                println("sending update to VM")
                mainViewModel.trySend(MainContract.Inputs.UpdateState(updateStateFn))
            }

            GrainButton(currentState, sendUpdate)
            MaltButton(currentState, sendUpdate)
            WortButton(currentState, sendUpdate)
            BeerButton(currentState, sendUpdate)
            BottlesButton(currentState, sendUpdate)
            MoneyButton(currentState, sendUpdate)
        }
    }
}

private fun MainContract.State.canBuyGrain(): Boolean {
    return money.value > 10
}

private fun buyGrain(): (MainContract.State.() -> MainContract.State) {
    return {
        copy(
            money = money - 10,
            grain = grain + 1,
        )
    }
}

@Composable
private fun GrainButton(state: MainContract.State, postInput: ((MainContract.State) -> MainContract.State) -> Unit) {
    Button(
        onClick = {
            println("clicking grain button")
            postInput(buyGrain())
        },
//        enabled = state.canBuyGrain()
    ) {
        Text("grain: ${state.grain} lbs")
    }
}

@Composable
private fun MaltButton(state: MainContract.State, postInput: ((MainContract.State) -> MainContract.State) -> Unit) {
    Button(onClick = {
        postInput {
            it.copy()
        }
    }) {
        Text("malt: ${state.malt}")
    }
}

@Composable
private fun WortButton(state: MainContract.State, postInput: ((MainContract.State) -> MainContract.State) -> Unit) {
    Button(onClick = {
        postInput {
            it.copy()
        }
    }) {
        Text("wort: ${state.wort}")
    }
}

@Composable
private fun BeerButton(state: MainContract.State, postInput: ((MainContract.State) -> MainContract.State) -> Unit) {
    Button(onClick = {
        postInput {
            it.copy()
        }
    }) {
        Text("beer: ${state.beer}")
    }
}

@Composable
private fun BottlesButton(state: MainContract.State, postInput: ((MainContract.State) -> MainContract.State) -> Unit) {
    Button(onClick = {
        postInput {
            it.copy()
        }
    }) {
        Text("bottles: ${state.bottles}")
    }
}

@Composable
private fun MoneyButton(state: MainContract.State, postInput: ((MainContract.State) -> MainContract.State) -> Unit) {
    Button(onClick = {
        postInput {
            it.copy()
        }
    }) {
        Text("money: ${state.money}")
    }
}
