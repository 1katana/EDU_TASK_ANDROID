package com.example.grouptasker.data.models

import com.fasterxml.jackson.annotation.JsonProperty

data class User(
    val id: Long? = null,
    val name: String,
    val lastName: String,
    val email: String,


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val password: String,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val passwordConfirmations: String? = null

) {
    fun isValidForCreate(): Boolean {
        return name.isNotBlank() && name.length <= 255 &&
                lastName.length <= 255 &&
                email.isNotBlank() && email.length <= 255 &&
                password.isNotBlank() &&
                passwordConfirmations != null && passwordConfirmations.isNotBlank()
    }

    fun isValidForUpdate(): Boolean {
        return id != null &&
                name.isNotBlank() && name.length <= 255 &&
                lastName.length <= 255 &&
                email.isNotBlank() && email.length <= 255 &&
                password.isNotBlank()
    }
}
