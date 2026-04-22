package com.boatit.boatsharing.features.login.domain.model

import com.boatit.boatsharing.features.login.model.LoginResponse
import com.boatit.boatsharing.features.login.model.UserData

data class LoginDomainModel(
    val status: Int,
    val message: String,
    val user: AuthenticatedUser?,
)

data class AuthenticatedUser(
    val email: String,
    val password: String,
    val userId: String,
    val username: String,
    val role: String,
    val missingStep: Int,
    val accessToken: String,
    val refreshToken: String,
)

fun LoginResponse.toDomainModel(): LoginDomainModel {
    return LoginDomainModel(
        status = Status,
        message = Message,
        user = obj?.toDomainModel(),
    )
}

fun UserData.toDomainModel(): AuthenticatedUser {
    return AuthenticatedUser(
        email = Email,
        password = Password,
        userId = UserId,
        username = Username,
        role = Role,
        missingStep = MissingStep,
        accessToken = Accesstoken,
        refreshToken = Refreshtoken,
    )
}

fun AuthenticatedUser.toUserData(): UserData {
    return UserData(
        Email = email,
        Password = password,
        UserId = userId,
        Username = username,
        Role = role,
        MissingStep = missingStep,
        Accesstoken = accessToken,
        Refreshtoken = refreshToken,
    )
}
