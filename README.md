# m4lib

![Banner](src/main/resources/assets/m4lib/banner.png)

Library mod for m4ssive's mods - Provides common utilities for nametag rendering and entity interactions.

## Description

m4lib is a shared library mod that provides common functionality for m4ssive's Minecraft mods. It includes utilities for nametag rendering, entity interactions, and other shared features.

## Features

- **Nametag Rendering**: Custom nametag rendering system for mods
- **PotionHelper**: Utility for potion type detection and categorization
- **ItemEntityTracker**: Generic ItemEntity tracking with duplicate prevention
- **PlayerTracker**: Player data tracking and management utility
- **ColorHelper**: Color manipulation and blending utilities
- **Entity Interactions**: Mixins for player and entity interactions

## Requirements

- Minecraft 1.21.x
- Fabric Loader >= 0.15.11
- Fabric API
- Java 21+

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21
2. Download and install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download m4lib from [Releases](https://github.com/m4ssivee/m4lib/releases)
4. Place the mod in your `mods` folder

## Usage

This is a library mod - it provides functionality for other mods. Install it if you're using mods that depend on m4lib.

## Mods Using m4lib

- TotemCounterV2
- PotCounter
- (Add other mods that use m4lib here)

## API Usage

### PotionHelper
```java
import com.m4ssive.m4lib.util.PotionHelper;

// Check if item is a potion
if (PotionHelper.isPotionItem(stack)) {
    PotionHelper.PotionType type = PotionHelper.getPotionType(stack);
    String displayName = PotionHelper.getPotionTypeDisplayName(type);
}
```

### ItemEntityTracker
```java
import com.m4ssive.m4lib.util.ItemEntityTracker;

ItemEntityTracker tracker = new ItemEntityTracker(
    100, // cooldown in ms
    itemEntity -> /* filter condition */,
    itemEntity -> /* callback */
);
tracker.processItemEntity(itemEntity);
```

### PlayerTracker
```java
import com.m4ssive.m4lib.util.PlayerTracker;

PlayerTracker tracker = M4Lib.getInstance().getPlayerTracker();
PlayerTracker.PlayerData data = tracker.getOrCreatePlayerData(uuid);
data.setCustomData("key", value);
```

### ColorHelper
```java
import com.m4ssive.m4lib.util.ColorHelper;

int blended = ColorHelper.blendColors(color1, color2, 0.5f);
int withAlpha = ColorHelper.withAlpha(color, 128);
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Links

- [GitHub](https://github.com/m4ssivee/m4lib)
- [Issues](https://github.com/m4ssivee/m4lib/issues)

## Author

**m4ssive**

- GitHub: [@m4ssivee](https://github.com/m4ssivee)

