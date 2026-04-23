package com.boatit.boatsharing.ui.navigation

import android.net.Uri
import androidx.navigation.NavBackStackEntry

fun NavBackStackEntry.optStringArg(name: String): String? = arguments?.getString(name)

fun NavBackStackEntry.optDecodedStringArg(name: String): String? =
    arguments?.getString(name)?.let(Uri::decode)

fun NavBackStackEntry.optBooleanArg(
    name: String,
    default: Boolean = false,
): Boolean = arguments?.getBoolean(name, default) ?: default
