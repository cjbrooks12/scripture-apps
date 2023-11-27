package com.caseyjbrooks.domain

public typealias DomainAction0<Result> /*                     */ = suspend () -> Result
public typealias DomainAction1<P1, Result> /*                 */ = suspend (P1) -> Result
public typealias DomainAction2<P1, P2, Result> /*             */ = suspend (P1, P2) -> Result
public typealias DomainAction3<P1, P2, P3, Result> /*         */ = suspend (P1, P2, P3) -> Result
public typealias DomainAction4<P1, P2, P3, P4, Result> /*     */ = suspend (P1, P2, P3, P4) -> Result
public typealias DomainAction5<P1, P2, P3, P4, P5, Result> /* */ = suspend (P1, P2, P3, P4, P5) -> Result

public interface DomainActionWrapper<Result> {
    public suspend fun executeAction(block: suspend () -> Result): Result {
        return block()
    }
}

public class CombinedDomainActionWrapper<Result : Any>(
    private val wrappers: List<DomainActionWrapper<Result>>,
) : DomainActionWrapper<Result> {
    override suspend fun executeAction(block: suspend () -> Result): Result {
        return wrappers
            .fold(
                suspend { block() },
            ) { next, wrapper ->
                suspend { wrapper.executeAction(next) }
            }
            .invoke()
    }
}

public fun <Result> wrapDomainAction(
    kernel: DomainAction0<Result>,
    wrapper: DomainActionWrapper<Result>,
): DomainAction0<Result> {
    return { wrapper.executeAction { kernel() } }
}

public fun <P1, Result> wrapDomainAction(
    kernel: DomainAction1<P1, Result>,
    wrapper: DomainActionWrapper<Result>,
): DomainAction1<P1, Result> {
    return { p1: P1 -> wrapper.executeAction { kernel(p1) } }
}

public fun <P1, P2, Result> wrapDomainAction(
    kernel: DomainAction2<P1, P2, Result>,
    wrapper: DomainActionWrapper<Result>,
): DomainAction2<P1, P2, Result> {
    return { p1: P1, p2: P2 -> wrapper.executeAction { kernel(p1, p2) } }
}

public fun <P1, P2, P3, Result> wrapDomainAction(
    kernel: DomainAction3<P1, P2, P3, Result>,
    wrapper: DomainActionWrapper<Result>,
): DomainAction3<P1, P2, P3, Result> {
    return { p1: P1, p2: P2, p3: P3 -> wrapper.executeAction { kernel(p1, p2, p3) } }
}

public fun <P1, P2, P3, P4, Result> wrapDomainAction(
    kernel: DomainAction4<P1, P2, P3, P4, Result>,
    wrapper: DomainActionWrapper<Result>,
): DomainAction4<P1, P2, P3, P4, Result> {
    return { p1: P1, p2: P2, p3: P3, p4: P4 -> wrapper.executeAction { kernel(p1, p2, p3, p4) } }
}

public fun <P1, P2, P3, P4, P5, Result> wrapDomainAction(
    kernel: DomainAction5<P1, P2, P3, P4, P5, Result>,
    wrapper: DomainActionWrapper<Result>,
): DomainAction5<P1, P2, P3, P4, P5, Result> {
    return { p1: P1, p2: P2, p3: P3, p4: P4, p5: P5 -> wrapper.executeAction { kernel(p1, p2, p3, p4, p5) } }
}
