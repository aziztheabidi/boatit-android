package com.boatit.boatsharing.ui.design

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit

/**
 * Design System for Business Dashboard
 * 
 * This object contains all the design tokens used throughout the Business Dashboard
 * to ensure consistency and maintainability. All magic numbers should be replaced
 * with references to these constants.
 */
object DesignSystem {
    
    // ==================== SPACING ====================
    object Spacing {
        val xs: Dp = 4.dp      // Minimal spacing (icon padding, small gaps)
        val sm: Dp = 8.dp      // Small spacing (horizontal arrangement, small gaps)
        val md: Dp = 12.dp     // Medium spacing (section spacing, vertical arrangement)
        val lg: Dp = 16.dp     // Large spacing (card padding, main container padding)
        val xl: Dp = 20.dp     // Extra large spacing (large gaps between elements)
        val xxl: Dp = 24.dp    // Extra extra large spacing (major section breaks)
        
        // Specific use cases
        val cardPadding: Dp = lg
        val sectionSpacing: Dp = md
        val elementSpacing: Dp = xl
        val minimalSpacing: Dp = xs
        val smallSpacing: Dp = sm
    }
    
    // ==================== SIZING ====================
    object Sizing {
        // Icon sizes
        val iconSmall: Dp = 16.dp      // Small icons
        val iconMedium: Dp = 24.dp     // Medium icons
        val iconLarge: Dp = 32.dp      // Large icons (warning, etc.)
        val iconXLarge: Dp = 48.dp     // Extra large icons (FAB)
        
        // Logo and image sizes
        val logoSize: Dp = 110.dp      // Business logo
        val logoSmall: Dp = 80.dp      // Wheel icon, small logos
        val galleryImageSize: Dp = 100.dp  // Gallery images
        
        // Button heights
        val buttonHeight: Dp = 35.dp   // Standard button height
        val buttonHeightSmall: Dp = 28.dp  // Small button height
        
        // Container heights
        val dropdownHeight: Dp = 300.dp    // Dropdown menu height
        val textFieldHeight: Dp = 100.dp   // Multi-line text field height
        
        // Layout weights
        val dayColumnWeight: Float = 0.5f  // Day column in hours modal
        val timeColumnWeight: Float = 1f   // Time column in hours modal
    }
    
    // ==================== TYPOGRAPHY ====================
    object Typography {
        val businessName: TextUnit = 22.sp     // Business name font size
        val businessType: TextUnit = 16.sp     // Business type font size
        val businessDescription: TextUnit = 14.sp  // Business description font size
        val buttonText: TextUnit = 12.sp       // Button text font size
        val smallText: TextUnit = 10.sp        // Small text, captions
        val largeText: TextUnit = 18.sp        // Large text, headings
    }
    
    // ==================== CORNER RADIUS ====================
    object CornerRadius {
        val small: Dp = 8.dp      // Small radius (text fields, small cards)
        val medium: Dp = 10.dp    // Medium radius (buttons)
        val large: Dp = 15.dp     // Large radius (logo cards, images)
        val xlarge: Dp = 20.dp     // Extra large radius (major containers)
        val modal: Dp = 16.dp     // Modal bottom sheet radius
    }
    
    // ==================== ELEVATION ====================
    object Elevation {
        val none: Dp = 0.dp       // Flat cards, no elevation
        val low: Dp = 2.dp        // Low elevation (toggle buttons)
        val medium: Dp = 4.dp     // Medium elevation
        val high: Dp = 6.dp       // High elevation (logo cards, prominent elements)
        val modal: Dp = 16.dp      // Modal elevation
    }
    
    // ==================== BORDERS ====================
    object Border {
        val width: Dp = 1.dp      // Standard border width
        val widthThick: Dp = 2.dp // Thick border width
    }
    
    // ==================== ALPHA VALUES ====================
    object Alpha {
        val disabled: Float = 0.1f    // Disabled state alpha
        val overlay: Float = 0.3f    // Overlay alpha
        val subtle: Float = 0.5f     // Subtle alpha
    }
    
    // ==================== DRAG THRESHOLDS ====================
    object Interaction {
        val dragThreshold: Int = 20    // Drag gesture threshold
    }
    
    // ==================== GRID LAYOUTS ====================
    object Grid {
        val columns: Int = 3           // Gallery grid columns
        val galleryItemSize: Dp = 90.dp // Gallery item size
        val gallerySpacing: Dp = 8.dp   // Gallery spacing
    }
    
    // ==================== COORDINATE PRECISION ====================
    object Precision {
        val coordinateDecimalPlaces: Int = 4  // Coordinate display precision
    }
}
