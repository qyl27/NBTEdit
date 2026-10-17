# In-Game NBTEdit

A Minecraft mod with an in-game editor for [NBT](https://minecraft.wiki/w/NBT_format).
For map creating, debugging, or anything else.

## Usage

### Shortcuts

Press `N` (by default) to edit your target BlockEntity, Entity or ItemStack in main hand.

- `Ctrl` + `C` to Copy a node.
- `Ctrl` + `V` to Paste a node.
- `Ctrl` + `X` to Cut a node.
- `Ctrl` + `D` to Delete a node.

### Commands

- `/nbtedit me`
  Edit player themselves.

- `/nbtedit hand`
  Edit ItemStack in player's main hand.

- `/nbtedit <x> <y> <z>`
  Edit BlockEntity at x y z.

- `/nbtedit <entity selector>`  
  Edit Entity with entity selector.

### Permissions

| Name           | Default Permission Level | Comment                                                                    |
|----------------|--------------------------|----------------------------------------------------------------------------|
| use            | 2                        | Open the editor to edit the NBT.                                           |
| read_only      | 1                        | Open the editor with view-only permission, saving is not available.        |
| edit_on_player | 4                        | Use the editor on player, some issues may be caused. USE AT YOUR OWN RISK! |

You may override the default permission levels in config file, or with a permission manager like `LuckPerms` to grant
permission node `nbtedit.<Name>` to any player.

### Configurations

#### Forge/NeoForge

Path: `.minecraft/config/nbtedit.toml`

```toml
# General settings. 
[general]
# Enable debug logs. Necessary if you are reporting bugs.
debug = false

# Override the default permission levels. Similar to vanilla, in the range of 0 ~ 4.
[general.permission]
use = 2
read_only = 1
edit_on_player = 4
```

#### Fabric

Path: `.minecraft/config/nbtedit.json`

```json5
{
  "debug": false,
  // Enable debug logs. Necessary if you are reporting bugs.
  "permissionsLevels": {
    // Override the default permission levels. Similar to vanilla, in the range of 0 ~ 4.
    "read_only": 1,
    "edit_on_player": 4,
    "use": 2
  }
}
```

## Origin

http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1286750-in-game-nbtedit-edit-mob-spawners-attributes-in

## Screenshots

![Screenshot #1](https://github.com/qyl27/NBTEdit/raw/1.21/img/2.png)  
![Screenshot #2](https://github.com/qyl27/NBTEdit/raw/1.21/img/3.png)

## Troubleshooting

- I was kicked while pressing save button, got `Payload may not be larger than 32767 bytes`:
  Minecraft have a maximum packet size limit, use
  with [Packet Fixer](https://www.curseforge.com/minecraft/mc-mods/packet-fixer)
  by [TonimatasDEV](https://github.com/TonimatasDEV)

## Bug report / Feature request

Go to the [issues page](https://github.com/qyl27/NBTEdit/issues).
