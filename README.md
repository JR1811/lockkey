# Lock and Key

Lock and Key is a Fabric mod that adds simple security mechanics for blocks and entities.

## Features

- Lock and key system with items to control access to blocks and entities
    - Add locks (with grooves data) to blocks and entities
    - use the `lockkey:all_blocks_lockable` Gamerule to enable every block being lockable, not just the ones in the `lcokkey:lockable` block tag
    - use keys with matching groove data to enable interaction / attacking / breaking with that block or entity
    - for now (WIP) use the `/lock` commands to manage anything lock related, like setting grooves and managing lock states of entities and blocks manually

# Requirements

- Minecraft (26.1.1)
- Fabric Loader
- Fabric API

# Installation

1. Install Fabric Loader for your Minecraft version
2. Download and install Fabric API
3. Place the mod .jar file into your mods folder
4. Launch the game using the Fabric profile

# Usage

Craft locks and keys to secure blocks and entities.
Place locker blocks and choose a variant depending on the intended function.

This mod is still work in progress! Currently, you can only set lock and key grooves by using the `/lock` commands!

# Planned Features and Content

- Better locking of multi-blocks, like doors and double chests (potentially providing an easy-to-use Mod API for others to use)
- Better Item Textures (yeah they are only Programmer art...)

Anything else? If you have any ideas, please let me know and open an [Issue on GitHub](https://github.com/JR1811/lockkey/issues)

<div style="text-align: center;">
  <a href="https://github.com/JR1811/Boatism/issues">
    <img src="https://github.com/fabricated-atelier/.github/blob/main/assets/badges/old/work_in_progress.png?raw=true" alt="Work in Progress" width="200">
  </a>
</div>