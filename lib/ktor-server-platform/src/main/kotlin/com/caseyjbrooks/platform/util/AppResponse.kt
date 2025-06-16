package com.caseyjbrooks.platform.util

import com.caseyjbrooks.platform.util.routing.Extractor
import com.caseyjbrooks.platform.util.routing.ExtractorRegistry
import com.caseyjbrooks.platform.util.routing.ExtractorScope
import com.caseyjbrooks.platform.util.routing.getExtractor
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import org.koin.ktor.ext.getKoin

fun interface AppResponse {
    abstract suspend fun send(routingContext: RoutingContext)

    class Json<T : Any>(
        private val value: T?,
        private val typeInfo: TypeInfo?,
        private val status: HttpStatusCode = HttpStatusCode.OK
    ) : AppResponse {
        override suspend fun send(routingContext: RoutingContext) = with(routingContext) {
            call.response.status(status)
            if (value != null) {
                call.respond(value, typeInfo)
            }
        }
    }
}

inline fun <reified T : Any> ok(value: T): AppResponse.Json<T> {
    return AppResponse.Json(value, runCatching { typeInfo<T>() }.getOrNull(), HttpStatusCode.OK)
}

inline fun <reified T : Any> notFound(): AppResponse.Json<T> {
    return AppResponse.Json(null, runCatching { typeInfo<T>() }.getOrNull(), HttpStatusCode.NotFound)
}

suspend inline fun RoutingContext.returningResponse(crossinline block: suspend () -> AppResponse) {
    block().send(this)
}

inline fun Route.GET(path: String, crossinline block: suspend RoutingContext.() -> AppResponse) {
    get(path) {
        returningResponse {
            block()
        }
    }
}

inline fun Route.PUT(path: String, crossinline block: suspend RoutingContext.() -> AppResponse) {
    put(path) {
        returningResponse {
            block()
        }
    }
}

inline fun Route.POST(path: String, crossinline block: suspend RoutingContext.() -> AppResponse) {
    post(path) {
        returningResponse {
            block()
        }
    }
}

inline fun Route.DELETE(path: String, crossinline block: suspend RoutingContext.() -> AppResponse) {
    delete(path) {
        returningResponse {
            block()
        }
    }
}

inline fun <reified P1> Route.POST(
    path: String,
    crossinline block: suspend RoutingContext.(P1) -> AppResponse,
) {
    post(path) {
        val scope = ExtractorScope(call)
        val extractorRegistry = call.getKoin().get<ExtractorRegistry>()
        val e1: Extractor<P1> = getExtractor<P1>(extractorRegistry)

        returningResponse {
            block(e1.invoke(scope))
        }
    }
}
