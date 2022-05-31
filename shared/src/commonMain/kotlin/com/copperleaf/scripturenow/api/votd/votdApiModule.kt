package com.copperleaf.scripturenow.api.votd

import com.copperleaf.scripturenow.api.votd.ourmanna.OurMannaApi
import com.copperleaf.scripturenow.api.votd.ourmanna.OurMannaApiConverterImpl
import com.copperleaf.scripturenow.api.votd.ourmanna.OurMannaApiImpl
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.create
import io.github.copper_leaf.shared.BASE_URL_OURMANNA
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun votdApiModule() = DI.Module(name = "API > VOTD") {
    bind<VerseOfTheDayApi> {
        singleton {
            OurMannaApiImpl(
                api = instance<String, Ktorfit>(arg = BASE_URL_OURMANNA).create<OurMannaApi>(),
                converter = OurMannaApiConverterImpl(),
            )
        }
    }
}
