## Phase 1: Foundation
- [✓] Create package structure
    ### Overview: Package Structure
    Followed the following structure.
    ```
    hytale-lands-plugin/
    ├── src/
    │   └── main/
    │       ├── java/
    │       │   └── org/
    │       │       └── almond/
    │       │           └── lands/
    │       │               ├── LandsPlugin.java         # Main plugin entry point
    │       │               ├── command/
    │       │               │   ├── LandCommand.java     # /land command handler
    │       │               │   └── LandAdminCommand.java # /landadmin for operators
    │       │               ├── model/
    │       │               │   ├── Land.java            # Land data structure
    │       │               │   ├── Region.java          # Cuboid region (two Vector3i)
    │       │               │   ├── LandRole.java        # Role with permission flags
    │       │               │   └── LandPermission.java  # Permission enum
    │       │               ├── manager/
    │       │               │   ├── LandManager.java     # Land CRUD & position lookup
    │       │               │   └── SelectionManager.java # Player corner selections
    │       │               ├── listener/
    │       │               │   └── ProtectionListener.java # Block/interact event handlers
    │       │               └── storage/
    │       │                   └── LandStorage.java     # JSON file persistence
    │       └── resources/
    │           ├── manifest.json
    │           ├── Common/                              # Assets (models, textures)
    │           └── Server/                              # Server-side data
    ├── build.gradle
    ├── settings.gradle
    ├── gradle.properties
    ├── README.md
    └── run/
    ```
    lands: The main plugin folder.
    command: The folder containing the command handlers, for the main land commands and the land admin commands. Any new group of commands will have a handler here.
    model: The folder containing the data structures used by the land plugin. The Land data structure, Region data structure, LandRole data structure, and LandPermission data structure. Any new data structures for the land plugin will be added here.
    manager: The folder containing managing resources used by the land plugin, such us looking up position and creating cuboid selections.
    storage: The folder containing the lands plugin storage, currently a json, but can be configured as a database in the future.

- [✓] Implement `Region` class with containment/adjacency logic
    ### Overview: Region Class Implementation
    Region: A region is a cuboid region defined by two corners in 3D space. In game the player will select two blocks, these two blocks will be the two corners used to form the cuboid region. The main functions we will need are: whether a block is contained in a region, whether a region is adjacent to another, the volume of a region.

        Concerns: Since a Land claim is created by these regions, what happens when these regions overlap. The current implementation is that the Land is a list of Region objects. Now overlaping regions wont cause a problem but are space inefficient. Maybe in the future we could make it so that overlaping regions are adjusted so that the stored region are not overlaping, maximizing space efficiency. But again a region is just two Vector3i objects, so they do not take much space, so we dont need to go crazy about the space efficiency.

        Constructor: given two Vector3i corner objects, representing the selected corners of the cuboid region by the player. The constructor will set the region to the maximum and minimum corner, not the exact corner given by the player. To do this the constructor creates two new Vector3i objects: corner1 where each coordinate is the max from the two given values. corner2 where each coordinate is the min from the two given corners. This ensures that the assumption that corner1 is the max corner in the region and corner2 is the min corner of the region is upheld for the other functions. 
        
        Contains: The Region's class function to check whether a block is in a region or not. Given is a Vector3i object, that represent the position in terms of a block in a Hytale world. Outputs a boolean value, of whether that block is in the region or not. Implementation: A Vector3i is an integer object; val blockPos = Vector3i(x, y, z). Taking 3 integers representing the x, y, and z coordinates of the block position in the world. Therefore we can create three ineqaulities, checking that the given Vector3i is within the bounds of the Region's two corner Vector3i objects.

            Concerns: The player can give any combination of corner pairs from the 8 corners in a cuboid region, to make the contains implementation easier/general we should implement that when a region is created the max and min corners are chosen. so the max corner will have greater than or equal values for its coordinates compared to the min corner. This will be irrespective of the corners that the player chooses when making the selection. This means when the player makes the selection, we use the selected corners to get the min and max corners of the cuboid region, then save those corners as the Region object.

        Adjacent: The Region's class function to check whether another region is adjacent to this region or not. Input, another region. Output, boolean values indicating adjacency or no. Implementation: corner1 is the max corner, corner2 is the min corner. Case 1, adjacent along x axis: Use an inequality to check that the y and z coordinates are within the bounds. For the case of exactly adjacent, the x coordinate of the max of one region will be 1 offset from the min of the other region.
        Repeat the above algoirthm for each coordinate.

            Concerns: There is an edge case for overlapping corners that we will need to cover. To cover this edge case, we just need to consider whether the coordinate is within the inequality of the other region. So for the x coordinate for instance, not just checking that its exactly offset by 1, but also checking if the x coordinate is within the min and max x coordinate of the other region. Repeat this for every coordinate.

        getVolume: The function to return the volume of blocks within the region, no input, the output is a long representing the number of blocks in the region. Implementation: Take the difference + 1 of each coordinate from the two corners. The plus 1 is because Hytale coordinates start at 0. Then multiple the difference for all 3 coordinates, this will be the volume of blocks in the region.

            Concern, if we use this to calculate the volume of blocks in a land, then overlapping regions will cause blocks to be count multiple times. The solution will  have to be implemented in the lands file, when encorporating new regions into the land, if a region overlaps another then it would have to be split into 1-3 cuboid regions which do not overlap any regions in the land, the number will depend on how many axis are fully overlapped, so if two axis are fully overlapped then only 1 new region needs to be created, while if zero axis are fully overlapped then 3 cuboid regions will need to be created.

- [✓] Implement `LandPermission` enum
    ### Overview: LandPermission Implementation:
    LandPermission: An enum object representing the different permissions you can set for a land claim. Each permission has a description to help users know what it does. Will be used in roles to check what permission a role has.

- [✓] Implement `LandRole` class
    ### Overview: LandRole Implementation:
    LandRole: Role object for a land, just a string for the role name, and the set of permissions given to the role.

        Concern: Should we also store the players who have this role? Yeh, cause where else would we store that information for a land. So the Landrole object for a land is where we get all the players, automatically every player has the outsider role for a land. Then when you trust a player, they are given the member role, or the next role with the least permissions. Actually the Land object can have the hash map of players and their roles in the land, the outsider role is for anyone not in that hash map.

- [✓] Implement `Land` class
    ### Overview: Land Implementation:
    Land: The main Land claim object class. Each land has an id, name, owner, list of regions, members, roles, and a timestamp for when it was created. For the roles, is if a hash map of the role names and the corresponding Landrole object. For the members it is the Player UUID and role name for that player. So we dont need to track player for each role in the role object. When a Land is first created there are default roles added to the role set, owner, admin, member, and outsider. The outsider role wont be stored in the hash map, rather any player not in the member's hash map will be an outsider. However, there are plans to allow the creation of multiple "outsider" roles, essentially allowing land claim owners to give permissions to players without having them inside the land claim it self. Perhaps with would mean that we need another list for outsider members. In the future we will also need to create sub claims, which are regions entirely inside of the land claim, which can have its own roles, so this can allow further roleplay of organizations outside of land claim owners; government.
