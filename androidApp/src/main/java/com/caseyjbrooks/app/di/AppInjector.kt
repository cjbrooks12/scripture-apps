package com.caseyjbrooks.app.di

import android.app.Application
import com.caseyjbrooks.scripturenow.repositories.RepositoriesInjector
import kotlinx.coroutines.CoroutineScope

public class AppInjector(
    public val applicationContext: Application,
    public val appCoroutineScope: CoroutineScope,
) {
    val dataSourcesInjector: DataSourcesInjectorImpl by lazy {
        DataSourcesInjectorImpl(this@AppInjector)
    }
    val repositoriesInjector: RepositoriesInjector by lazy {
        RepositoriesInjectorImpl(
            this@AppInjector,
            dataSourcesInjector,
        )
    }
}
