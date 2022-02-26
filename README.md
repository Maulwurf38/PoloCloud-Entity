# PoloCloud-Entity


#### What is "PoloCloud-Entity"?
**PoloCloud-Entity** is a **Plugin** for the **PoloCloud**, where you can create Entities.
These Entities will then work as a "server-switcher".

:information_source: Everything will update automaticly when, for example a new Player joins to a Group

# Usage

**Command**: */cloudentity*
**Aliases**: */entity, /entities, /cloudentites*
**Permission**: (default, is changeable in the Config) -> *cloud.plugin.entity.command*

## Create a Entity

To create a entity there are differnt ways:
1. **Command (or aliases) create < EntityType > < GroupName >
	- Example:
		- /cloudentity create Villager Lobby
		
2. **Command (or aliases) create < EntityType > < GroupName > < Title >
	- This is used for creating a CloudEntity with a custom Title
	- [Message Placeholders](#placeholders)
	- :information_source: This Command Supports ColorCodes (Use & for displaying a § Color) -> [ChatColors](https://htmlcolorcodes.com/minecraft-color-codes/) 
	- Example:
		- /cloudentity create Villager Lobby &7Group &b{GROUP}


## Removing a Entity

For removing there is only one Way:
- **Command (or aliases) create < EntityType > < GroupName >
	- :information_source: You have to be 0.5 Blocks near the Entity you want to Remove
	- Example:
		- /cloudentity remove
	
# Placeholders

There are different placeholders. You use them with these brackets {} in the bracket you have to write the Name of the Placeholder
|                |Funktion                          |Example                       |
|----------------|-------------------------------|-----------------------------|
|{GROUP}|`ServiceGroup#getName()` Gets the Name of the specific ServiceGroup            |{GROUP} -> Lobby         |
|{SERVER}|`CloudService#getName()` Gets the Name of the specific CloudService            |{SERVER} -> Lobby-1         |
|{ONLINE}|`CloudService#getOnlineCount()` Gets the OnlinePlayers of the specific CloudService            |{ONLINE} -> 25         |
|{MAX}|`CloudService#getMaxPlayers()` Gets the Maximum Players of the specific CloudService            |{MAX} -> 50         |
|{MOTD}|`CloudService#getMotd()` Gets the MOTD of the specific CloudService            |{MOTD} -> "Super tolle Motd"         |

# Developers

**PoloCloud-Entity** offers the possibility to change the to open Inventory when a Player clicks on a CloudEntity

*How does it work?*
You have to register a Listener, from there easly create a new void-Method with the @EventHandler Annonation

    public class Test implements Listener {  
      
      @EventHandler  
      public void handle(CloudEntityInventoryOpenEvent event) {  
      
      }  
      
    }

Used Imports:

    import de.polocloud.plugin.entity.event.CloudEntityInventoryOpenEvent;  
    import org.bukkit.event.EventHandler;  
    import org.bukkit.event.Listener;

#### From there you can access these Values:
- The Player, who has clicked on the CloudEntity (example for the example above `event.getPlayer()`
- The CloudEntity the Player has clicked on (example for the example above `event.getCloudEntity()`
- The ServiceGroup the CloudEntity is from  `event.getServiceGroup()`
- The Inventory you can change (example for the example above `event.getInventory()`
- The List with the displayed CloudServices in it (`List<CloudService>`) (example for the example above `event.getCloudServices()`

Now if you want to change the Inventory (add Item, remove Item, change Item) you can easily set the Inventory with `event.setInventory(updateInventory);`

**Example**

    public class Test implements Listener {  
      
      @EventHandler  
      public void handle(CloudEntityInventoryOpenEvent event) {  
      Inventory inventory = event.getInventory();  
      inventory.addItem(new ItemStack(Material.WOODEN_AXE));  
      event.setInventory(inventory);  
      }  
      
    }

Used Imports:

    import de.polocloud.plugin.entity.event.CloudEntityInventoryOpenEvent;  
    import org.bukkit.Material;  
    import org.bukkit.event.EventHandler;  
    import org.bukkit.event.Listener;  
    import org.bukkit.inventory.Inventory;  
    import org.bukkit.inventory.ItemStack;


:information_source: Also you can cancel to opening of the Inventory with `event.setCanceled(true);`

## Get a CloudEntity

Ways to get a CloudEntity:

:warning: **All of these Methods can return a nulled CloudEntity**



1. Of Location (from spawned CloudEntity) 
	
        CloudEntityHandler.getInstance().getCloudEntityOfLocation(yourLocation);

2. Of Location (from the Config Location) 
	
        CloudEntityHandler.getInstance().getCloudEntityOfLocation0(yourLocation);
3. Of Entity (the SpawnedEntity) 
	
        CloudEntityHandler.getInstance().getCloudEntityOfEntity(yourEntity);

## Destroy a CloudEntity

You need a CloudEntity, then invoke:

    CloudEntity cloudEntity; //<- Your CloudEntity (only for placeholder)
    cloudEntity.destroy();

## Spawn a CloudEntity

You need a CloudEntity, then invoke:

    CloudEntity cloudEntity; //<- Your CloudEntity (only for placeholder)
    cloudEntity.spawn();


## Create a new CloudEntity
You need:
- Location
- EntityType
- ServiceGroup
- (:information_source: Optional (How to use then? Simple use **null** as Title)

:information_source: This can return you a valid CloudEntity Object

    Location location; //<- Your CloudEntity (only for placeholder)
    EntityType entityType; //<- Your EntityType (only for placeholder)
    ServiceGroup serviceGroup; //<- Your ServiceGroup (only for placeholder)
    String title; //<- Optional (Nullable) (only for placeholder)
    
    CloudEntity cloudEntity = CloudEntityHandler.getInstance().createCloudEntity(location, entityType, serviceGroup, title);
    or when the title should be default:
    
    CloudEntity cloudEntity = CloudEntityHandler.getInstance().createCloudEntity(location, entityType, serviceGroup, null);

## Remove a CloudEntity
You need a CloudEntity, then invoke:

    CloudEntity cloudEntity; //<- Your CloudEntity (only for placeholder)
    CloudEntityHandler.getInstance().removeEntity(yourCloudEntity);
