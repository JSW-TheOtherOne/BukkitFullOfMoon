# BukkitFullOfMoon
Custom generator example for Bukkit by Dinnerbone

Updated to 1.13.2 - 8th June 2019 by JSW-TheOtherOne

Updating Dinnerbone's 0.0.1-SNAPSHOT to Spigot 1.13.2 API
pullFork branch added to look at other peoples forks of this project -
with a view to incorporating some of their ideas and code.

Incorporating Pocketkid's to 1.12 update as base build to start from.
Incorporating androgorr's rebuild - "Add moon gravity, organize plugin files", adds "/earth command and moon gravity by Javipepe."

Commands:
/moon - Will generate (if world isn't already there) a moon and teleport you to it (if you aren't already). Also you will get a cool jump boost effect!
/earth - Gbye to moon! Will teleport you to the world you were at when you executed `/moon` and you'll lose them cool jump effects. It was a cool trip, though!

14thJune 2019
Changed the way terrain blocks are generated within a chunk
Added config settings to play around with these
Added crater rims to larger craters

ToDo: get NoiseGenerator working to generate a more interesting moonscape
add gravity for players and mobs, objects
return to earth command
limit types of mobs
