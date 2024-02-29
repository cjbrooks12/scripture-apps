package com.caseyjbrooks.notifications

public sealed interface PermissionsDomainEvents {
    public enum class PermissionType {
        Notifications,
        None,
    }

    public enum class Result {
        Granted,
        Denied,
    }

    public data class PermissionResult(val type: PermissionType, val result: Result) : PermissionsDomainEvents
}
