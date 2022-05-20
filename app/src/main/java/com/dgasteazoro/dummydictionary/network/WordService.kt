package com.dgasteazoro.dummydictionary.network

import com.dgasteazoro.dummydictionary.network.dto.LoginRequest
import com.dgasteazoro.dummydictionary.network.dto.LoginResponse
import com.dgasteazoro.dummydictionary.network.dto.WordsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WordService {

        @GET("/words")
        suspend fun getAllWord(): WordsResponse

        @POST("/login")
        suspend fun login(@Body credentials: LoginRequest): LoginResponse


}