import com.ghostchu.quickshop.api.shop.Shop
import com.ghostchu.quickshop.compatibility.CompatibilityModule
import me.wiefferink.areashop.events.notify.DeletedRegionEvent
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class Main : CompatibilityModule(), Listener {
    override fun init() {}

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onShopNeedDeletion(event: DeletedRegionEvent) {
        val region = event.region

        val minPoint = region.minimumPoint
        val maxPoint = region.maximumPoint
        val world = region.world
        val chuckLocations = hashSetOf<Chunk>()

        var x = minPoint.blockX
        while (x <= maxPoint.blockX + 16) {
            var z = minPoint.blockZ
            while (z <= maxPoint.blockZ + 16) {
                chuckLocations.add(world.getChunkAt(x shr 4, z shr 4))
                z += 16
            }
            x += 16
        }

        val shopMap = hashMapOf<Location, Shop>()

        for (chunk in chuckLocations) {
            val shopsInChunk: MutableMap<Location, Shop>? = api.shopManager.getShops(chunk)
            if (shopsInChunk != null) {
                shopMap.putAll(shopsInChunk)
            }
        }

        for ((shopLocation, shop) in shopMap) {
            if (region.region.contains(shopLocation.blockX, shopLocation.blockY, shopLocation.blockZ)) {
                api.shopManager.deleteShop(shop)
            }
        }
    }
}