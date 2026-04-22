package com.boatit.boatsharing.data.network.networkresponse

/**
 * Generic mapper interface for converting DTO objects to domain models
 * Ensures type-safe and testable data mapping
 *
 * Best practices:
 * - One mapper per DTO or response model
 * - Implement mapping logic with null safety
 * - Handle transformation errors gracefully
 * - Use sealed results for mapping outcomes
 *
 * Example implementation:
 * ```
 * class UserDtoMapper : NetworkMapper<UserResponseDto, User> {
 *     override fun mapToDomain(response: UserResponseDto): User =
 *         User(
 *             id = response.id,
 *             name = response.name,
 *             email = response.email.lowercase()
 *         )
 *
 *     override fun mapToDto(domain: User): UserResponseDto =
 *         UserResponseDto(
 *             id = domain.id,
 *             name = domain.name,
 *             email = domain.email
 *         )
 * }
 *
 * // Usage
 * val mapper = UserDtoMapper()
 * val user = mapper.mapToDomain(userDto)
 * ```
 */
interface NetworkMapper<ResponseDto : ResponseModel<ResponseDto>, DomainModel> {
    /**
     * Convert API response DTO to domain model
     * Called when receiving data from backend
     *
     * @param response The DTO from network response
     * @return Domain model suitable for business logic
     * @throws IllegalArgumentException if mapping cannot be performed
     */
    fun mapToDomain(response: ResponseDto): DomainModel

    /**
     * Convert domain model to API request DTO
     * Called when sending data to backend
     *
     * @param domain The domain model to convert
     * @return DTO suitable for network transmission
     */
    fun mapToDto(domain: DomainModel): ResponseDto
}

/**
 * Base implementation of NetworkMapper with optional logging
 * Provides template method for common mapping tasks
 */
abstract class BaseNetworkMapper<ResponseDto : ResponseModel<ResponseDto>, DomainModel> :
    NetworkMapper<ResponseDto, DomainModel> {
    /**
     * Safe mapping with error handling
     *
     * @param response DTO to map
     * @param mapperFn Mapping function
     * @return DomainModel or throws exception with context
     */
    protected inline fun <T> safeMap(
        response: ResponseDto,
        mapperFn: (ResponseDto) -> T,
    ): T {
        return try {
            mapperFn(response)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to map response: ${e.message}", e)
        }
    }
}
