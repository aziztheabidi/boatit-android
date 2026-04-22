package com.boatit.boatsharing.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object StringOrIntAsIntSerializer : KSerializer<Int?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("StringOrIntAsInt", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Int? {
        return try {
            // Try decoding as Int
            decoder.decodeInt()
        } catch (_: SerializationException) {
            try {
                // Try decoding as String then convert to Int
                decoder.decodeString().toIntOrNull()
            } catch (_: SerializationException) {
                null
            }
        }
    }

    override fun serialize(
        encoder: Encoder,
        value: Int?,
    ) {
        if (value != null) {
            encoder.encodeInt(value)
        } else {
            encoder.encodeNull()
        }
    }
}
