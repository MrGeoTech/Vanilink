import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType

class LoadChunkCommand : Command("loadchunk") {

    init {
        setDefaultExecutor { sender, _ ->
            sender.sendMessage("Usage: /loadchunk <x1> <z1> <x2> <z2> <instance>\nAvailable instances: ${MinecraftServer.getInstanceManager().instances.map { it.uniqueId }}")
        }
        val x1Arg = ArgumentType.Integer("x1")
        val z1Arg = ArgumentType.Integer("z1")
        val x2Arg = ArgumentType.Integer("x2")
        val z2Arg = ArgumentType.Integer("z2")
        val instanceArg = ArgumentType.UUID("instance")
        addSyntax({ sender, context ->
            val x1 = context.get(x1Arg)
            val z1 = context.get(z1Arg)
            val x2 = context.get(x2Arg)
            val z2 = context.get(z2Arg)
            val instance = MinecraftServer.getInstanceManager().getInstance(context.get(instanceArg))
            if (instance == null) {
                sender.sendMessage("Instance not found")
                return@addSyntax
            }
            for (x in x1..x2) {
                for (z in z1..z2) {
                    instance.loadChunk(x, z)
                }
            }
            sender.sendMessage("Loaded chunks in instance ${instance.uniqueId}")
        }, x1Arg, z1Arg, x2Arg, z2Arg, instanceArg)
    }

}