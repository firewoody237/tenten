package com.example.tenten.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan


@SpringBootApplication(scanBasePackages = ["com.example.tenten"])
@ServletComponentScan(basePackages = ["com.example.tenten"])
class ApiApplication {

}

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}