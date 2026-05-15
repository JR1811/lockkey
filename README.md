# Lock and Key

Lock and Key is a Fabric mod that adds simple security mechanics for blocks and entities.

## Features

- Lock and key system with items to control access to blocks and entities
    - Add locks (with grooves data) to blocks and entities
    - use the `lockkey:all_blocks_lockable` Gamerule to enable every block being lockable, not just the ones in the `lcokkey:lockable` block tag
    - use keys with matching groove data to enable interaction / attacking / breaking with that block or entity
    - for now (WIP) use the `/lock` commands to manage anything lock related, like setting grooves and managing lock states of entities and blocks manually

# Requirements

- Fabric Loader
- Fabric API

# Usage

Craft locks and keys to secure blocks and entities. This mod is still work in progress! Currently, you can only set 
lock and key grooves by using the `/lock` commands!

# Planned Features and Content

- Better locking of multi-blocks, like doors and double chests (potentially providing an easy-to-use Mod API for other mods / datapacks)

Anything else? If you have any ideas, please let me know and open an [Issue on GitHub](https://github.com/JR1811/lockkey/issues)

# Credits

The textures of the lock and the key items have been cleaned up by [Aethyus](https://aethyus.carrd.co/).
Check them out!

<div style="text-align: center;">
  <a href="https://github.com/JR1811/Boatism/issues">
    <img src="https://github.com/fabricated-atelier/.github/blob/main/assets/badges/old/work_in_progress.png?raw=true" alt="Work in Progress" width="200">
  </a>
</div>