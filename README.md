# skyproc-starter

Environment variables:

All run configurations require at least two environment variables:

LOCALAPPDATA, which defines the path to 'plugins.txt', which should be managed by your mod manager (i.e., MO2)

SP_GLOBAL_PATH_TO_INI, which defines the path to Skyrim.ini

and SP_GLOBAL_PATH_TO_DATA, which defines the path to the SSE Data folder. 

Examples (from my Linux host using MO2 w/default profile):

LOCALAPPDATA=/home/me/Games/mod-organizer-2-skyrimspecialedition/modorganizer2/profiles/Default

SP_GLOBAL_PATH_TO_INI=/home/me/.steam/debian-installation/steamapps/compatdata/489830/pfx/drive_c/users/steamuser/Documents/My Games/Skyrim Special Edition/Skyrim.ini

SP_GLOBAL_PATH_TO_DATA=/home/me/.steam/debian-installation/steamapps/common/Skyrim Special Edition/Data/

Patch properties (application.properties): 

The following patch properties are required: 

sp.local.patch.name The name of this patch 

sp.local.patch.author The name of the author of this patch 

sp.local.patch.version The current version of this patch 

sp.local.patch.welcome A welcome message to display in the UI; should provide a brief description of this patch functionality 

sp.local.patch.sum.description A more verbose message to display in the UI to descripbe this patcher 

sp.local.patch.font.name The name of the font to use for everything 

sp.local.patch.import.requests The record types SkyProc should import to construct this patch 

sp.local.patch.is.string.tabled 'true' if this patch is string tabled 

sp.local.patch.needs.patching // TODO: this value should be determined by a predicate function, not a property 

sp.local.patch.required.mods Any mods that you REQUIRE to be present in order to patch 
