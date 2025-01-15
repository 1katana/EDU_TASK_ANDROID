package com.example.grouptasker.data.models

import com.example.grouptasker.data.roomDb.Entity.UserEntity


data class User(
    val id: Long? = null,
    val name: String,
    val lastName: String,
    val email: String,



    val password: String? = null,


    val passwordConfirmations: String? = null

) {
    fun isValidForCreate(): Boolean {
        return name.isNotBlank() && name.length <= 255 &&
                lastName.length <= 255 &&
                email.isNotBlank() && email.length <= 255 &&
                password?.isNotBlank() ?: false &&
                passwordConfirmations != null && passwordConfirmations.isNotBlank()
    }

    fun isValidForUpdate(): Boolean {
        return id != null &&
                name.isNotBlank() && name.length <= 255 &&
                lastName.length <= 255 &&
                email.isNotBlank() && email.length <= 255 &&
                password?.isNotBlank() ?: false
    }


    fun toEntity(): UserEntity {
        return UserEntity(id = this.id!!, name = this.name, email = this.email, lastName = this.lastName)
    }


}
