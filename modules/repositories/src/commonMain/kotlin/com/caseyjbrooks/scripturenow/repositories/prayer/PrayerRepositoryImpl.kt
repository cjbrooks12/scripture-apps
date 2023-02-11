package com.caseyjbrooks.scripturenow.repositories.prayer

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import com.copperleaf.ballast.*
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class PrayerRepositoryImpl(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: PrayerRepositoryInputHandler,
) : BasicViewModel<
        PrayerRepositoryContract.Inputs,
        PrayerRepositoryContract.Events,
        PrayerRepositoryContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .apply {
            this += BootstrapInterceptor {
                PrayerRepositoryContract.Inputs.Initialize
            }
            inputStrategy = FifoInputStrategy()
        }
        .withViewModel(
            inputHandler = inputHandler,
            initialState = PrayerRepositoryContract.State(),
            name = "Prayer Repository",
        )
        .build(),
    eventHandler = eventHandler { },
), PrayerRepository {

    override fun getAllPrayers(refreshCache: Boolean): Flow<Cached<List<Prayer>>> {
        trySend(PrayerRepositoryContract.Inputs.RefreshPrayerList(refreshCache))
        return observeStates()
            .map { it.prayerList }
    }

    override fun getPrayer(uuid: Uuid, refreshCache: Boolean): Flow<Cached<Prayer>> {
        trySend(PrayerRepositoryContract.Inputs.RefreshPrayerList(refreshCache))
        return observeStates()
            .map {
                it
                    .prayerList
                    .map { prayers ->
                        prayers.single { prayer -> prayer.uuid == uuid }
                    }
            }
    }

    override suspend fun createOrUpdatePrayer(prayer: Prayer) {
        send(PrayerRepositoryContract.Inputs.CreateOrUpdatePrayer(prayer))
    }

    override suspend fun deletePrayer(prayer: Prayer) {
        send(PrayerRepositoryContract.Inputs.DeletePrayer(prayer))
    }
}
