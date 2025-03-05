package project

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TimeManagerApplication

fun main(args: Array<String>){
    runApplication<TimeManagerApplication>()
}