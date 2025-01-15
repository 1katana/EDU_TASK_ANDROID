package com.example.grouptasker.data.models



data class ResponseEntity<T>(
    val message: String,
    val body: T
)