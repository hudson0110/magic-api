package com.hudson.magicapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MagicapiApplication

fun main(args: Array<String>) {
	runApplication<MagicapiApplication>(*args)
}
