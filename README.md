# OasisWarps
A simple Spigot plugin for warp management, supporting versions 1.8 to 1.14. You can download the plugin from spigotmc.org by clicking [here](https://www.spigotmc.org/resources/oasiswarps-1-8-1-14.71587/).

If you are experiencing and bugs or other issues, or you would simply like to request a new feature, please open a new issue on the [issue page](https://github.com/InfinityMage/OasisWarps/issues).

### Features
OasisWarps has a number of features that make the plugin very useful for servers.

- Full customisability of all messages
- Optional per-warp permissions
- Limit number of setwarps using permissions
- Highlight the player's own warps in the warp list
- In-depth config

### Commands

| Command | Description | Permissions |
|:---|:---|:---|
| `/oasiswarps [help\|reload]` | Display a list of commands or reload the config | `oasiswarps.reload`
| `/setwarp <warp name>` | Set a warp at your current location | `oasiswarps.setwarp`
| `/delwarp <warp name>` | Delete a warp | `oasiswarps.delwarp`
| `/warp <warp name> [player]` | Teleport to warp | `oasiswarps.warp`
| `/warps` `/listwarps` | List all the warps on the server | `oasiswarps.listwarps`

### Permissions

| Permission | Description | 
|:---|:---|
| `oasiswarps.reload` | Allow access to reload the config
| `oasiswarps.setwarp` | Allow access to the `/setwarp` command
| `oasiswarps.limit.[limit name]` | Limit the number of `/setwarps` a player can do. Limits are customisable in the config.
| `oasiswarps.bypass.limit` | Bypass the setwarp limit
| `oasiswarps.delwarp` | Allow access to the `/delwarp` command
| `oasiswarps.delwarp.others` | Allow the deletion of another person's warp
| `oasiswarps.warp` | Allow access to the `/warp` command
| `oasiswarps.warp.[warp name]` | Allow access to teleporting to a specific warp. You can enable per-warp permissions in the config.
| `oasiswarps.warp.others` | Allow access to forcefully warping another player (this bypasses delay and cooldown)
| `oasiswarps.bypass.warp` | Bypass per-warp permissions
| `oasiswarps.bypass.cooldown` | Bypass the `/warp` cooldown
| `oasiswarps.bypass.delay` | Bypass the `/warp` delay
| `oasiswarps.listwarps` | Allow access to the `/warps` and `/listwarps` commands

### Config
```yaml
 # Should warps only be able to be used if the warp location is considered safe (air, no lava etc)?
safe-warp: true
 # Maxmium length of warp name (Setting this higher than 32 is not recommended)
max-length: 20
 # Minimum length of warp name (Do not set lower than 1)
min-length: 3
 # How many warps should be displayed on each page in /warps
warp-list-amount: 25
 # Should there be a delay before teleporting to a warp?
warp-delay: true
 # Time in seconds to delay warp teleporting (give the permission oasiswarps.bypass.delay to bypass)
 # warp-delay must be set to true
warp-delay-time: 5
 # Cooldown length in seconds (give the permission oasiswarps.bypass.cooldown to bypass)
warp-cooldown: 20
 # Should attempted teleports with a delay be cancelled if the player moves?
 # This requires there to be an active warp delay
warp-cancel-on-move: true
 # Should you require a permission for each individual warp you can teleport to? (give the permission oasiswarps.warp.[warp name], or oasiswarps.bypass.restrict to bypass)
per-warp-permission: false

 # Custom Setwarp Limits
 # To limit the number of warps a user can create, give them the permission oasiswarps.limit.[name]
 # To bypass the limit, use the permission oasiswarps.bypass.limit
setwarp-limits:
  default: 1
  vip: 3
  epic: 5

 # Messages
messages:
  warp-created: "&3Warp &b{WARP} &3created."
  warp-deleted: "&3Warp &b{WARP} &3deleted."
  warp-tp: "&3Teleporting to &b{WARP}&3..."
  warp-tp-delay: "&3Teleporting in &b{DELAY} &3seconds. Don't move."
  warp-tp-other: "&3Teleporting &f{USER} &3to &b{WARP}&3..."
  warp-color: "&f"
  warp-owned-color: "&a"
  warp-list-none: "&fNo warps exist"
  warp-list-info: "&3There are &b{WARPS} &3warps. Viewing page &b{PAGE} &3of &b{MAX_PAGE}&3."
  plugin-reload: "&aOasisWarps config reloaded."
   # Error messages
  incorrect-usage: "&cIncorrect Usage! {USAGE}"
  no-permission: "&cYou do not have permission for that command!"
  no-permission-action: "&cYou do not have the required permissions to {ACTION}!"
  warp-already-exists: "&cSorry, but that warp already exists."
  warp-no-exist: "&cSorry, but that warp does not exist."
  warp-cooldown: "&cYou cannot warp for another {COOLDOWN} seconds."
  warp-cancelled: "&cTeleport cancelled, you moved!"
  warp-no-perm: "&cYou do not have permission to teleport to &6{WARP}&c."
  long-name: "&cSorry, but the maximum length of a warp name is {MAX} characters."
  short-name: "&cSorry, but the minimum length of a warp name is {MIN} characters."
  illegal-char: "&cSorry, but warps can only use alphanumeric characters and underscores."
  limit-reached: "&cSorry, but you are not able to set any more warps."
  not-online: "&cThat player is not online."
  warp-list-not-number: "&cThe page selection must be a number!"
  warp-list-no-exist: "&cSorry, but that page does not exist."
  warp-unsafe: "&cThat warp is unsafe and cannot be teleported to."
```