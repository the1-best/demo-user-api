package com.example.demo.user.api.controller

import com.example.demo.user.api.entity.Response
import com.example.demo.user.api.entity.User
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    val users: List<User> = listOf(
        User().also {
            it.id = "U0001"
            it.name = "John Rambo"
            it.age = 60
        },
        User().also
        {
            it.id = "U0002"
            it.name = "Steve Roger"
            it.age = 39
        }
        ,
        User().also
        {
            it.id = "U0003"
            it.name = "Stephen Strange"
            it.age = 40
        })

    @GetMapping(
        value = ["/user"]
    )
    fun getUser(): Response {
        var response = Response()
        response.respCode = "success"
        response.respMessage = jacksonObjectMapper().writeValueAsString(users)
        return response
    }

    @GetMapping(
        value = ["/user/{id}"]
    )
    fun getUserById(
        @PathVariable("id") id: String
    ): User {
        return users.find { it.id == id }!!
    }
}
