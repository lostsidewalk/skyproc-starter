# skyproc-starter

Environment variables:

All run configurations require at least two environment variables:

LOCALAPPDATA, which defines the path to 'plugins.txt', which should be managed by your mod manager (i.e., MO2)

and SP_GLOBAL_PATH_TO_INI, which defines the path to Skyrim.ini.

Examples (from my Linux host using MO2 w/default profile):

LOCALAPPDATA=/home/me/Games/mod-organizer-2-skyrimspecialedition/modorganizer2/profiles/Default

SP_GLOBAL_PATH_TO_INI=/home/me/.steam/debian-installation/steamapps/compatdata/489830/pfx/drive_c/users/steamuser/Documents/My Games/Skyrim Special Edition/Skyrim.ini

Configuration properties (application.properties):

The following configuration properties are required:

sp.global.path.to.data, which defines the path to the SSE 'Data' folder.

Example:

sp.global.path.to.data=/home/me/.steam/debian-installation/steamapps/common/Skyrim Special Edition/Data/

Patch properties (application.properties): 

The following patch properties are required: 

sp.local.patch.name The name of this patch 

sp.local.patch.author The name of the author of this patch 

sp.local.patch.version The current version of this patch 

sp.local.patch.welcome A welcome message to display in the UI; should provide a brief description of this patch functionality 

sp.local.patch.sum.description A more verbose message to display in the UI to descripbe this patcher 

sp.local.patch.font.name The name of the font to use for everything 

