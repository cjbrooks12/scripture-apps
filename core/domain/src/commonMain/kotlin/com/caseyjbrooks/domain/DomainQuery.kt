package com.caseyjbrooks.domain

import kotlinx.coroutines.flow.Flow

public typealias DomainQuery0<Result> /*                      */ = () -> Flow<Result>
public typealias DomainQuery1<P1, Result> /*                  */ = (P1) -> Flow<Result>
public typealias DomainQuery2<P1, P2, Result> /*              */ = (P1, P2) -> Flow<Result>
public typealias DomainQuery3<P1, P2, P3, Result> /*          */ = (P1, P2, P3) -> Flow<Result>
public typealias DomainQuery4<P1, P2, P3, P4, Result> /*      */ = (P1, P2, P3, P4) -> Flow<Result>
public typealias DomainQuery5<P1, P2, P3, P4, P5, Result> /*  */ = (P1, P2, P3, P4, P5) -> Flow<Result>

public interface DomainQueryWrapper<Result> {
    public fun executeQuery(block: () -> Flow<Result>): Flow<Result> {
        return block()
    }
}

public class CombinedDomainQueryWrapper<Result : Any>(
    private val wrappers: List<DomainQueryWrapper<Result>>,
) : DomainQueryWrapper<Result> {
    override fun executeQuery(block: () -> Flow<Result>): Flow<Result> {
        return wrappers
            .fold(
                { block() },
            ) { next, wrapper ->
                { wrapper.executeQuery(next) }
            }
            .invoke()
    }
}

public fun <Result> wrapDomainQuery(
    kernel: DomainQuery0<Result>,
    wrapper: DomainQueryWrapper<Result>,
): DomainQuery0<Result> {
    return { wrapper.executeQuery { kernel() } }
}

public fun <P1, Result> wrapDomainQuery(
    kernel: DomainQuery1<P1, Result>,
    wrapper: DomainQueryWrapper<Result>,
): DomainQuery1<P1, Result> {
    return { p1: P1 -> wrapper.executeQuery { kernel(p1) } }
}

public fun <P1, P2, Result> wrapDomainQuery(
    kernel: DomainQuery2<P1, P2, Result>,
    wrapper: DomainQueryWrapper<Result>,
): DomainQuery2<P1, P2, Result> {
    return { p1: P1, p2: P2 -> wrapper.executeQuery { kernel(p1, p2) } }
}

public fun <P1, P2, P3, Result> wrapDomainQuery(
    kernel: DomainQuery3<P1, P2, P3, Result>,
    wrapper: DomainQueryWrapper<Result>,
): DomainQuery3<P1, P2, P3, Result> {
    return { p1: P1, p2: P2, p3: P3 -> wrapper.executeQuery { kernel(p1, p2, p3) } }
}

public fun <P1, P2, P3, P4, Result> wrapDomainQuery(
    kernel: DomainQuery4<P1, P2, P3, P4, Result>,
    wrapper: DomainQueryWrapper<Result>,
): DomainQuery4<P1, P2, P3, P4, Result> {
    return { p1: P1, p2: P2, p3: P3, p4: P4 -> wrapper.executeQuery { kernel(p1, p2, p3, p4) } }
}

public fun <P1, P2, P3, P4, P5, Result> wrapDomainQuery(
    kernel: DomainQuery5<P1, P2, P3, P4, P5, Result>,
    wrapper: DomainQueryWrapper<Result>,
): DomainQuery5<P1, P2, P3, P4, P5, Result> {
    return { p1: P1, p2: P2, p3: P3, p4: P4, p5: P5 -> wrapper.executeQuery { kernel(p1, p2, p3, p4, p5) } }
}
