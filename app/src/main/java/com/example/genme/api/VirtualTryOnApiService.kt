package com.example.genme.api

import com.example.genme.api.models.TryOnStartResponse
import com.example.genme.api.models.TryOnStatusResponse
import com.example.genme.api.models.HairstyleStartResponse
import com.example.genme.api.models.FigurineStartResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit API service interface for Virtual Try-On API
 * Base URL: http://35.232.52.16
 */
interface VirtualTryOnApiService {
    
    /**
     * Start a virtual try-on job by uploading person and clothing images
     * POST /try-on/
     */
    @Multipart
    @POST("try-on/")
    suspend fun startTryOn(
        @Part personImage: MultipartBody.Part,
        @Part clothingImage: MultipartBody.Part
    ): Response<TryOnStartResponse>
    
    /**
     * Check the status of a try-on job
     * GET /status/{task_id}
     */
    @GET("status/{task_id}")
    suspend fun getStatus(
        @Path("task_id") taskId: String
    ): Response<TryOnStatusResponse>

    // Fallback figurine-specific status route if backend namespaces it under /figurine/
    @GET("figurine/status/{task_id}")
    suspend fun getFigurineStatus(
        @Path("task_id") taskId: String
    ): Response<TryOnStatusResponse>
    
    /**
     * Download the result image for a completed try-on job
     * GET /result/{task_id}
     */
    @GET("result/{task_id}")
    suspend fun getResult(
        @Path("task_id") taskId: String
    ): Response<ResponseBody>

    // Fallback figurine-specific result route
    @GET("figurine/result/{task_id}")
    suspend fun getFigurineResult(
        @Path("task_id") taskId: String
    ): Response<ResponseBody>
    
    /**
     * Start a hairstyle change job with correct endpoint from API docs
     * POST /hair-style/
     */
    @Multipart
    @POST("hair-style/")
    suspend fun startHairstyleChange(
        @Part personImage: MultipartBody.Part,
        @Part("hair_description") hairstyleText: okhttp3.RequestBody
    ): Response<HairstyleStartResponse>

    /**
     * Start a 3D figurine generation job with predefined styles only
     * POST /figurine/
     * - character_image: MultipartBody.Part
     * - style: text (one of predefined keys)
     */
    @Multipart
    @POST("figurine/")
    suspend fun startFigurine(
        @Part characterImage: MultipartBody.Part,
        @Part("style") styleKey: okhttp3.RequestBody
    ): Response<FigurineStartResponse>

    /**
     * Get available figurine styles
     * GET /figurine/styles
     */
    @GET("figurine/styles")
    suspend fun getFigurineStyles(): Response<List<String>>
    
    /**
     * Health check endpoint
     * GET /
     */
    @GET("/")
    suspend fun healthCheck(): Response<Map<String, String>>
}
