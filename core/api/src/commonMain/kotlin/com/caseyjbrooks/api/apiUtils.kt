package com.caseyjbrooks.api

import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.path
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

private fun replacePathParameters(
    path: String,
    pathParameters: Map<String, String>,
): List<String> {
    val pathSegments: List<String> = path.split('/')

    val dynamicSegmentNames = pathSegments
        .filter { it.startsWith(':') }
        .map { it.drop(1) }
        .sorted()
    val suppliedSegmentNames = pathParameters
        .keys
        .sorted()

    check(dynamicSegmentNames == suppliedSegmentNames) {
        "incorrect path parameters supplied: expected $dynamicSegmentNames, got $suppliedSegmentNames"
    }

    return pathSegments.map { segment ->
        if (segment.startsWith(':')) {
            pathParameters[segment.drop(1)]!!
        } else {
            segment
        }
    }
}

public suspend fun <NormalResponse, ErrorResponse> HttpClient.request(
    _method: HttpMethod,
    _baseUrl: String,
    _path: String,
    _pathParameters: Map<String, String> = emptyMap(),
    _queryParameters: Map<String, String> = emptyMap(),

    _headers: Map<String, String> = emptyMap(),
    _contentType: ContentType = ContentType.Application.Json,
    _accept: ContentType = ContentType.Application.Json,

    normalBodySerializer: KSerializer<NormalResponse>,
    errorBodySerializer: KSerializer<ErrorResponse>,
): Either<NormalResponse, ErrorResponse> {
    val httpClient = this
    val response: HttpResponse = httpClient.request(_baseUrl) {
        method = _method
        url {
            path(*replacePathParameters(_path, _pathParameters).toTypedArray())
            if (_queryParameters.isNotEmpty()) {
                _queryParameters.forEach { (key, value) ->
                    parameters.append(key, value)
                }
            }
        }
        if (_headers.isNotEmpty()) {
            headers.append(HttpHeaders.ContentType, _contentType.toString())
            headers.append(HttpHeaders.Accept, _accept.toString())
            _headers.forEach { (key, value) ->
                headers.append(key, value)
            }
            headers.append("x-some-extra-header", "my value")
        }
    }

    val responseContentType = response.contentType()?.withoutParameters()
    check(responseContentType == ContentType.Application.Json) {
        "response was not of type 'application/json', got $responseContentType"
    }

    if (response.status.isSuccess()) {
        val bodyString = response.bodyAsText()
        return Json.decodeFromString(normalBodySerializer, bodyString).asLeft()
    } else {
        val bodyString = response.bodyAsText()
        return Json.decodeFromString(errorBodySerializer, bodyString).asRight()
    }
}
