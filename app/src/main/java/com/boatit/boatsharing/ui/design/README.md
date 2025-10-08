# Design System Documentation

## Overview

The `DesignSystem` object provides a centralized, semantic approach to managing all design tokens used throughout the Business Dashboard. This eliminates magic numbers and ensures consistent spacing, sizing, typography, and styling across the entire application.

## Benefits

- **🎯 Single Source of Truth**: All design values are defined in one place
- **🔧 Easy Maintenance**: Change values once to update the entire app
- **📱 Consistent UI**: Ensures visual consistency across all components
- **🚀 Developer Experience**: Auto-completion and type safety
- **📏 Semantic Naming**: Clear, descriptive names instead of magic numbers

## Usage

### Import the Design System

```kotlin
import com.boatit.boatsharing.ui.design.DesignSystem
```

### Spacing

Use semantic spacing tokens for consistent layout:

```kotlin
// Instead of magic numbers
.padding(16.dp)
.verticalArrangement = Arrangement.spacedBy(12.dp)

// Use design system
.padding(DesignSystem.Spacing.cardPadding)
.verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing)
```

**Available Spacing Tokens:**
- `xs` (4.dp) - Minimal spacing (icon padding, small gaps)
- `sm` (8.dp) - Small spacing (horizontal arrangement, small gaps)
- `md` (12.dp) - Medium spacing (section spacing, vertical arrangement)
- `lg` (16.dp) - Large spacing (card padding, main container padding)
- `xl` (20.dp) - Extra large spacing (large gaps between elements)
- `xxl` (24.dp) - Extra extra large spacing (major section breaks)

**Specific Use Cases:**
- `cardPadding` - Standard card padding
- `sectionSpacing` - Spacing between sections
- `elementSpacing` - Spacing between major elements
- `minimalSpacing` - Minimal spacing for small elements
- `smallSpacing` - Small spacing for minor elements

### Sizing

Use semantic sizing tokens for consistent component dimensions:

```kotlin
// Instead of magic numbers
.size(80.dp)
.height(35.dp)

// Use design system
.size(DesignSystem.Sizing.logoSmall)
.height(DesignSystem.Sizing.buttonHeight)
```

**Available Sizing Tokens:**

**Icon Sizes:**
- `iconSmall` (16.dp) - Small icons
- `iconMedium` (24.dp) - Medium icons
- `iconLarge` (32.dp) - Large icons (warning, etc.)
- `iconXLarge` (48.dp) - Extra large icons (FAB)

**Logo and Image Sizes:**
- `logoSize` (110.dp) - Business logo
- `logoSmall` (80.dp) - Wheel icon, small logos
- `galleryImageSize` (100.dp) - Gallery images

**Button Heights:**
- `buttonHeight` (35.dp) - Standard button height
- `buttonHeightSmall` (28.dp) - Small button height

**Container Heights:**
- `dropdownHeight` (300.dp) - Dropdown menu height
- `textFieldHeight` (100.dp) - Multi-line text field height

**Layout Weights:**
- `dayColumnWeight` (0.5f) - Day column in hours modal
- `timeColumnWeight` (1f) - Time column in hours modal

### Typography

Use semantic typography tokens for consistent text styling:

```kotlin
// Instead of magic numbers
fontSize = 22.sp
fontSize = 16.sp

// Use design system
fontSize = DesignSystem.Typography.businessName
fontSize = DesignSystem.Typography.businessType
```

**Available Typography Tokens:**
- `businessName` (22.sp) - Business name font size
- `businessType` (16.sp) - Business type font size
- `businessDescription` (14.sp) - Business description font size
- `buttonText` (12.sp) - Button text font size
- `smallText` (10.sp) - Small text, captions
- `largeText` (18.sp) - Large text, headings

### Corner Radius

Use semantic corner radius tokens for consistent rounded corners:

```kotlin
// Instead of magic numbers
shape = RoundedCornerShape(8.dp)
shape = RoundedCornerShape(15.dp)

// Use design system
shape = RoundedCornerShape(DesignSystem.CornerRadius.small)
shape = RoundedCornerShape(DesignSystem.CornerRadius.large)
```

**Available Corner Radius Tokens:**
- `small` (8.dp) - Small radius (text fields, small cards)
- `medium` (10.dp) - Medium radius (buttons)
- `large` (15.dp) - Large radius (logo cards, images)
- `xlarge` (20.dp) - Extra large radius (major containers)
- `modal` (16.dp) - Modal bottom sheet radius

### Elevation

Use semantic elevation tokens for consistent depth:

```kotlin
// Instead of magic numbers
elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)

// Use design system
elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.none)
elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.high)
```

**Available Elevation Tokens:**
- `none` (0.dp) - Flat cards, no elevation
- `low` (2.dp) - Low elevation (toggle buttons)
- `medium` (4.dp) - Medium elevation
- `high` (6.dp) - High elevation (logo cards, prominent elements)
- `modal` (16.dp) - Modal elevation

### Borders

Use semantic border tokens for consistent borders:

```kotlin
// Instead of magic numbers
border = BorderStroke(1.dp, color = Color.Black)

// Use design system
border = BorderStroke(DesignSystem.Border.width, color = Color.Black)
```

**Available Border Tokens:**
- `width` (1.dp) - Standard border width
- `widthThick` (2.dp) - Thick border width

### Alpha Values

Use semantic alpha tokens for consistent transparency:

```kotlin
// Instead of magic numbers
.background(Color.Gray.copy(alpha = 0.1f))

// Use design system
.background(Color.Gray.copy(alpha = DesignSystem.Alpha.disabled))
```

**Available Alpha Tokens:**
- `disabled` (0.1f) - Disabled state alpha
- `overlay` (0.3f) - Overlay alpha
- `subtle` (0.5f) - Subtle alpha

### Interaction

Use semantic interaction tokens for consistent user interactions:

```kotlin
// Instead of magic numbers
if (dragAmount > 20) { ... }

// Use design system
if (dragAmount > DesignSystem.Interaction.dragThreshold) { ... }
```

**Available Interaction Tokens:**
- `dragThreshold` (20) - Drag gesture threshold

### Grid Layouts

Use semantic grid tokens for consistent layouts:

```kotlin
// Instead of magic numbers
val columns = 3
val itemSize = 90.dp
val spacing = 8.dp

// Use design system
val columns = DesignSystem.Grid.columns
val itemSize = DesignSystem.Grid.galleryItemSize
val spacing = DesignSystem.Grid.gallerySpacing
```

**Available Grid Tokens:**
- `columns` (3) - Gallery grid columns
- `galleryItemSize` (90.dp) - Gallery item size
- `gallerySpacing` (8.dp) - Gallery spacing

### Precision

Use semantic precision tokens for consistent formatting:

```kotlin
// Instead of magic numbers
String.format("%.4f", latitude)

// Use design system
String.format("%.${DesignSystem.Precision.coordinateDecimalPlaces}f", latitude)
```

**Available Precision Tokens:**
- `coordinateDecimalPlaces` (4) - Coordinate display precision

## Migration Guide

### Before (Magic Numbers)
```kotlin
Card(
    modifier = Modifier.padding(16.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Business Name",
            fontSize = 22.sp
        )
        Button(
            modifier = Modifier.height(35.dp)
        ) {
            Text("Save")
        }
    }
}
```

### After (Design System)
```kotlin
Card(
    modifier = Modifier.padding(DesignSystem.Spacing.cardPadding),
    elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.none)
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing)
    ) {
        Text(
            text = "Business Name",
            fontSize = DesignSystem.Typography.businessName
        )
        Button(
            modifier = Modifier.height(DesignSystem.Sizing.buttonHeight)
        ) {
            Text("Save")
        }
    }
}
```

## Best Practices

1. **Always use design system tokens** instead of magic numbers
2. **Choose the most specific token** for your use case (e.g., `cardPadding` instead of `lg`)
3. **Use semantic names** that describe the purpose, not the value
4. **Group related tokens** in the same category
5. **Document new tokens** when adding them to the design system

## Adding New Tokens

When adding new design tokens:

1. **Choose the appropriate category** (Spacing, Sizing, Typography, etc.)
2. **Use semantic naming** that describes the purpose
3. **Add comments** explaining the use case
4. **Update this documentation** with the new token
5. **Test the changes** across all affected components

## Examples

### Complete Component Example

```kotlin
@Composable
fun ExampleCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DesignSystem.Spacing.cardPadding),
        elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.none),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(DesignSystem.CornerRadius.small)
    ) {
        Column(
            modifier = Modifier.padding(DesignSystem.Spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing)
        ) {
            Text(
                text = "Card Title",
                fontSize = DesignSystem.Typography.businessName,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Card description text",
                fontSize = DesignSystem.Typography.businessDescription,
                color = Color.Gray
            )
            
            Button(
                onClick = { },
                modifier = Modifier.height(DesignSystem.Sizing.buttonHeight),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.button_normal)
                )
            ) {
                Text(
                    text = "Action Button",
                    fontSize = DesignSystem.Typography.buttonText
                )
            }
        }
    }
}
```

This design system ensures consistent, maintainable, and scalable UI development across the entire Business Dashboard.
