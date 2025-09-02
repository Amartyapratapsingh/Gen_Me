package com.example.genme.api.models

import com.google.gson.annotations.SerializedName

/**
 * Response model for starting a virtual try-on job
 */
data class TryOnStartResponse(
    @SerializedName("task_id")
    val taskId: String,
    @SerializedName("status")
    val status: String = "processing"
)

/**
 * Response model for checking job status
 */
data class TryOnStatusResponse(
    @SerializedName("task_id")
    val taskId: String,
    @SerializedName("status")
    val status: TryOnStatus,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("error")
    val error: String? = null
)

/**
 * Enum representing the status of a try-on job
 */
enum class TryOnStatus {
    @SerializedName("processing")
    PROCESSING,
    
    @SerializedName("completed")
    COMPLETED,
    
    @SerializedName("failed")
    FAILED
}

/**
 * Response model for API errors
 */
data class ApiErrorResponse(
    @SerializedName("detail")
    val detail: String? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("error")
    val error: String? = null
)
