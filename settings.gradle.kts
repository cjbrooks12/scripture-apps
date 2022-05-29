rootProject.name = "scripture-now"

include(":androidApp")
include(":shared")

include(":modules:common")
include(":modules:router")
include(":modules:sync")

include(":modules:votd:api")
include(":modules:votd:impl")

include(":modules:verses:api")
include(":modules:verses:impl")

include(":api")
include(":idle-beer")
