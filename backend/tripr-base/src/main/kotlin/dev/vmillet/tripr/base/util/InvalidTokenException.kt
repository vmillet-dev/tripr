package dev.vmillet.tripr.base.util

import java.lang.RuntimeException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.UNAUTHORIZED)
class InvalidTokenException : RuntimeException()
