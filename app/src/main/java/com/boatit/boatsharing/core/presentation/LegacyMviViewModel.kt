package com.boatit.boatsharing.core.presentation

import androidx.lifecycle.ViewModel

/**
 * Transitional base for legacy ViewModels that still expose
 * feature-specific state fields while migrating to strict event-driven MVI.
 */
open class LegacyMviViewModel : ViewModel()
