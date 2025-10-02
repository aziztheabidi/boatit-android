package com.boatit.boatsharing.utils.prefmanager

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * TokenProvider with Android Keystore integration for secure token storage
 * 
 * Implements LLR-2.7.1: Keystore Key Generation Implementation
 * Implements LLR-2.7.2: Token Encryption Implementation
 * Implements LLR-2.7.3: Keystore Storage Implementation
 * Implements LLR-2.7.4: Token Decryption Implementation
 * Implements LLR-2.7.5: Keystore Retrieval Implementation
 */
class TokenProvider(context: Context) {

    private val sharedPrefManager = SharedPrefManager(context)
    private val context = context
    
    // Android Keystore constants
    private val keyStore = KeyStore.getInstance("AndroidKeyStore")
    private val keyAlias = "BoatSharingTokenKey"
    private val transformation = "AES/GCM/NoPadding"
    private val gcmIvLength = 12
    
    init {
        keyStore.load(null)
        // LLR-2.7.1: Keystore Key Generation Implementation
        generateKeystoreKey()
    }
    
    /**
     * LLR-2.7.1: Keystore Key Generation Implementation
     * 
     * Generates encryption key for token storage using Android Keystore
     */
    private fun generateKeystoreKey() {
        try {
            Log.d("TokenProvider", "Generating Keystore key")
            
            if (!keyStore.containsAlias(keyAlias)) {
                val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
                val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                    keyAlias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setUserAuthenticationRequired(false) // Allow background access
                    .setRandomizedEncryptionRequired(true)
                    .build()
                
                keyGenerator.init(keyGenParameterSpec)
                keyGenerator.generateKey()
                
                Log.i("TokenProvider", "Keystore key generated successfully")
            } else {
                Log.d("TokenProvider", "Keystore key already exists")
            }
            
        } catch (e: Exception) {
            Log.e("TokenProvider", "Failed to generate Keystore key: ${e.message}")
        }
    }
    
    /**
     * LLR-2.7.2: Token Encryption Implementation
     * 
     * Encrypts tokens before storage using AES-GCM encryption
     */
    private fun encryptToken(token: String): ByteArray? {
        return try {
            Log.d("TokenProvider", "Encrypting token")
            
            val secretKey = keyStore.getKey(keyAlias, null) as SecretKey
            val cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            
            val encryptedBytes = cipher.doFinal(token.toByteArray())
            val iv = cipher.iv
            
            // Prepend IV to encrypted data
            val encryptedWithIv = ByteArray(iv.size + encryptedBytes.size)
            System.arraycopy(iv, 0, encryptedWithIv, 0, iv.size)
            System.arraycopy(encryptedBytes, 0, encryptedWithIv, iv.size, encryptedBytes.size)
            
            Log.d("TokenProvider", "Token encrypted successfully")
            encryptedWithIv
            
        } catch (e: Exception) {
            Log.e("TokenProvider", "Token encryption failed: ${e.message}")
            null
        }
    }
    
    /**
     * LLR-2.7.4: Token Decryption Implementation
     * 
     * Decrypts tokens after retrieval using AES-GCM decryption
     */
    private fun decryptToken(encryptedToken: ByteArray): String? {
        return try {
            Log.d("TokenProvider", "Decrypting token")
            
            if (encryptedToken.size < gcmIvLength) {
                Log.w("TokenProvider", "Encrypted token too short")
                return null
            }
            
            val secretKey = keyStore.getKey(keyAlias, null) as SecretKey
            val cipher = Cipher.getInstance(transformation)
            
            // Extract IV from encrypted data
            val iv = ByteArray(gcmIvLength)
            System.arraycopy(encryptedToken, 0, iv, 0, gcmIvLength)
            
            val encryptedBytes = ByteArray(encryptedToken.size - gcmIvLength)
            System.arraycopy(encryptedToken, gcmIvLength, encryptedBytes, 0, encryptedBytes.size)
            
            val spec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
            
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            val decryptedToken = String(decryptedBytes)
            
            Log.d("TokenProvider", "Token decrypted successfully")
            decryptedToken
            
        } catch (e: Exception) {
            Log.e("TokenProvider", "Token decryption failed: ${e.message}")
            null
        }
    }
    
    /**
     * LLR-2.7.3: Keystore Storage Implementation
     * 
     * Stores encrypted tokens in Android Keystore
     */
    private fun storeTokenInKeystore(tokenType: String, encryptedToken: ByteArray): Boolean {
        return try {
            Log.d("TokenProvider", "Storing $tokenType token in Keystore")
            
            val prefs = context.getSharedPreferences("EncryptedTokens", Context.MODE_PRIVATE)
            val base64Encoded = android.util.Base64.encodeToString(encryptedToken, android.util.Base64.DEFAULT)
            
            prefs.edit().putString(tokenType, base64Encoded).apply()
            
            Log.i("TokenProvider", "$tokenType token stored in Keystore successfully")
            true
            
        } catch (e: Exception) {
            Log.e("TokenProvider", "Failed to store $tokenType token in Keystore: ${e.message}")
            false
        }
    }
    
    /**
     * LLR-2.7.5: Keystore Retrieval Implementation
     * 
     * Retrieves and decrypts tokens from Android Keystore
     */
    private fun getTokenFromKeystore(tokenType: String): String? {
        return try {
            Log.d("TokenProvider", "Retrieving $tokenType token from Keystore")
            
            val prefs = context.getSharedPreferences("EncryptedTokens", Context.MODE_PRIVATE)
            val base64Encoded = prefs.getString(tokenType, null)
            
            if (base64Encoded != null) {
                val encryptedToken = android.util.Base64.decode(base64Encoded, android.util.Base64.DEFAULT)
                val decryptedToken = decryptToken(encryptedToken)
                
                if (decryptedToken != null) {
                    Log.d("TokenProvider", "$tokenType token retrieved from Keystore successfully")
                } else {
                    Log.w("TokenProvider", "Failed to decrypt $tokenType token")
                }
                
                decryptedToken
            } else {
                Log.d("TokenProvider", "No $tokenType token found in Keystore")
                null
            }
            
        } catch (e: Exception) {
            Log.e("TokenProvider", "Failed to retrieve $tokenType token from Keystore: ${e.message}")
            null
        }
    }
    
    /**
     * Public interface methods that use Keystore implementation
     */
    
    fun getAccessToken(): String? {
        return try {
            // Try Keystore first
            val keystoreToken = getTokenFromKeystore("accessToken")
            if (keystoreToken != null) {
                Log.d("TokenProvider", "Retrieved access token from Keystore")
                return keystoreToken
            }

            // Fallback to SharedPreferences for migration
            val sharedPrefToken = sharedPrefManager.getUserData()?.accessToken
            if (sharedPrefToken != null) {
                Log.d("TokenProvider", "Retrieved access token from SharedPreferences (migration)")
                // Migrate to Keystore
                saveTokens(sharedPrefToken, sharedPrefManager.getUserData()?.refreshToken)
                return sharedPrefToken
            }
            
            Log.d("TokenProvider", "No access token found")
            null
            
        } catch (e: Exception) {
            Log.e("TokenProvider", "Failed to get access token: ${e.message}")
            null
        }
    }

    fun getRefreshToken(): String? {
        return try {
            // Try Keystore first
            val keystoreToken = getTokenFromKeystore("refreshToken")
            if (keystoreToken != null) {
                Log.d("TokenProvider", "Retrieved refresh token from Keystore")
                return keystoreToken
            }
            
            // Fallback to SharedPreferences for migration
            val sharedPrefToken = sharedPrefManager.getUserData()?.refreshToken
            if (sharedPrefToken != null) {
                Log.d("TokenProvider", "Retrieved refresh token from SharedPreferences (migration)")
                // Migrate to Keystore
                saveTokens(sharedPrefManager.getUserData()?.accessToken, sharedPrefToken)
                return sharedPrefToken
            }
            
            Log.d("TokenProvider", "No refresh token found")
            null
            
        } catch (e: Exception) {
            Log.e("TokenProvider", "Failed to get refresh token: ${e.message}")
            null
        }
    }

    fun saveTokens(accessToken: String?, refreshToken: String?) {
        try {
            Log.i("TokenProvider", "Saving tokens to Keystore")
            
            if (accessToken != null && refreshToken != null) {
                // Encrypt and store in Keystore
                val encryptedAccessToken = encryptToken(accessToken)
                val encryptedRefreshToken = encryptToken(refreshToken)
                
                if (encryptedAccessToken != null && encryptedRefreshToken != null) {
                    val accessSuccess = storeTokenInKeystore("accessToken", encryptedAccessToken)
                    val refreshSuccess = storeTokenInKeystore("refreshToken", encryptedRefreshToken)
                    
                    if (accessSuccess && refreshSuccess) {
                        Log.i("TokenProvider", "Tokens saved to Keystore successfully")
                        
                        // Clear old SharedPreferences data after successful Keystore storage
                        sharedPrefManager.clearUserData()
                    } else {
                        Log.w("TokenProvider", "Failed to save tokens to Keystore")
                    }
                } else {
                    Log.e("TokenProvider", "Failed to encrypt tokens")
                }
            } else {
                Log.w("TokenProvider", "Cannot save null tokens")
            }
            
        } catch (e: Exception) {
            Log.e("TokenProvider", "Failed to save tokens: ${e.message}")
        }
    }

    fun clearTokens() {
        try {
            Log.i("TokenProvider", "Clearing tokens from Keystore and SharedPreferences")
            
            // Clear from Keystore
            val prefs = context.getSharedPreferences("EncryptedTokens", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
            
            // Clear from SharedPreferences
            sharedPrefManager.clearUserData()
            
            Log.i("TokenProvider", "Tokens cleared successfully")
            
        } catch (e: Exception) {
            Log.e("TokenProvider", "Failed to clear tokens: ${e.message}")
        }
    }
}