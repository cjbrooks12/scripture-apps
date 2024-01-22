package com.caseyjbrooks.votd.schedules

import com.copperleaf.ballast.BallastViewModel

internal typealias VotdSchedulesViewModel = BallastViewModel<
        VotdSchedulesContract.Inputs,
        VotdSchedulesContract.Events,
        VotdSchedulesContract.State,
        >
