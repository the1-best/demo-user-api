package com.example.demo.user.api.controller

import com.example.demo.user.api.entity.Response
import com.example.demo.user.api.entity.User
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@WebMvcTest(
    controllers = [UserController::class],
    properties = ["server.port=0", "management.server.port=0"]
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

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

    @Test
    fun `should return claim reward offer reward success test`() {
        val response = Response().apply {
            respCode = "success"
            respMessage = jacksonObjectMapper().writeValueAsString(users)
        }
        val mvcResult = mockMvc.perform(
            MockMvcRequestBuilders.get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn()

        val actualResponseBody = mvcResult.response.contentAsString
        JSONAssert.assertEquals(jacksonObjectMapper().writeValueAsString(response), actualResponseBody, true)
    }
}
