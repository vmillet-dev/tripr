package dev.vmillet.tripr.base

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan


@SpringBootApplication
@ComponentScan("dev.vmillet.tripr")
class TriprApplication

fun main(args: Array<String>) {
    runApplication<TriprApplication>(*args)
}
