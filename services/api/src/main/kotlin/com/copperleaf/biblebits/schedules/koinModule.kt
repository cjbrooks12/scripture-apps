package com.copperleaf.biblebits.schedules

import com.copperleaf.biblebits.schedules.main.MainServerSchedule
import com.copperleaf.biblebits.schedules.main.MainServerScheduleViewModel
import com.copperleaf.ballast.ktor.queue
import io.ktor.server.application.ApplicationCall
import org.koin.dsl.module
import org.koin.ktor.plugin.RequestScope

val schedulesKoinModule = module {
    scope<RequestScope> {
        scoped<MainServerScheduleViewModel> {
            get<ApplicationCall>().queue(MainServerSchedule)
        }
    }
}
