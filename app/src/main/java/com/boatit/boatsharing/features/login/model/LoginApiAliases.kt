package com.boatit.boatsharing.features.login.model

import com.boatit.boatsharing.features.login.data.dto.LoginResponseDto
import com.boatit.boatsharing.features.login.data.dto.UserDataDto

/** API wire types live in `data.dto`; these aliases preserve existing imports and Gson/Kotlinx usage. */
typealias LoginResponse = LoginResponseDto
typealias UserData = UserDataDto
