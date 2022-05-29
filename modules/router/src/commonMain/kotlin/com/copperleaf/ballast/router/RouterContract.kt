package com.copperleaf.ballast.router

public object RouterContract {
    public data class State(
        val backstack: List<NavToken> = emptyList(),
    )

    public sealed class Inputs {
        /**
         * Navigate to the target [destination]. This destination may optionally be given a [tag], which will be pushed
         * into the backstack before the [destination], which may layer be used to pop several screens back until this
         * tag.
         */
        public data class GoToDestination(
            val destination: Destination,
            val tag: Tag? = null,
        ) : Inputs()

        /**
         * Navigate 1 destination backward in the backstack. The destination that was removed will be emitted as a
         * [Events.DestinationPopped] event.
         */
        public object GoBack : Inputs()
    }

    public sealed class Events {

        /**
         * A new destination was pushed into the backstack.
         */
        public data class DestinationPushed(val newDestination: Destination) : Events()

        /**
         * The router navigated backward 1 destination in the backstack. If this was the start destination,
         * [previousDestination] will be null, otherwise it will be non-null referring to the previous destination in
         * the backstack that is now resumed as the current destination.
         */
        public data class DestinationPopped(val removedDestination: Destination, val previousDestination: Destination?) : Events()

        /**
         * A new tag was pushed into the backstack.
         */
        public data class TagPushed(val newTag: Tag) : Events()

        /**
         * The router navigated backward in the backstack, and popped a tag in the process. If this was the only tag in
         * the backstack, [previousTag] will be null, otherwise it will be non-null referring to the previous tag in the
         * backstack that is now considered the current tag.
         */
        public data class TagPopped(val removedTag: Tag, val previousTag: Tag?) : Events()

        /**
         * The router attempted to navigate backward, but the backstack was already empty. Typically, this would be a
         * request to either exit the app, to navigate back to the start destination.
         */
        public object OnBackstackEmptied : Events()
    }
}
