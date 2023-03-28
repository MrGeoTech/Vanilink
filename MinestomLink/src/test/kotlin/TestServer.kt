import lighting.recalculateLight
import net.minestom.server.MinecraftServer
import net.minestom.server.attribute.AttributeModifier
import net.minestom.server.attribute.AttributeOperation
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.instance.InstanceChunkLoadEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerRespawnEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.event.server.ServerTickMonitorEvent
import net.minestom.server.tag.Tag
import net.minestom.server.world.DimensionType
import java.time.Duration

fun main() {
    val server = MinecraftServer.init()

    MinecraftServer.getCommandManager().register(LoadChunkCommand())

    val instance = MinecraftServer.getInstanceManager()
        .createInstanceContainer(DimensionType.OVERWORLD)

    instance.setTag(Tag.Boolean("minestomlink"), true)

    var lastTickInTps = 20.0

    MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent::class.java) { event ->
        println("Player joined! Instance Time Rate: $lastTickInTps")
        event.setSpawningInstance(instance)
        event.player.respawnPoint = Pos(0.0, 100.0, 0.0)
    }.addListener(PlayerRespawnEvent::class.java) { event ->
        println("Player respawned!")
        event.respawnPosition = Pos(0.0, 100.0, 0.0)
        val player = event.player
        player.gameMode = GameMode.CREATIVE
    }.addListener(PlayerSpawnEvent::class.java) { event ->
        println("Player spawned!")
        event.player.gameMode = GameMode.CREATIVE
        event.player.teleport(Pos(0.0, 100.0, 0.0))
        event.spawnInstance.recalculateLight()
    }.addListener(ServerTickMonitorEvent::class.java) { event ->
        lastTickInTps = 1 / event.tickMonitor.tickTime
    }

    //VelocityProxy.enable("TgTAR9Rvee1H")

    MinecraftServer.getSchedulerManager().buildTask {
        instance.loadChunk(0, 0)
    }.delay(Duration.ofSeconds(1)).schedule()

    server.start("0.0.0.0", 25566)
}