package net.mrgeotech

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BlockDataCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true
        val block = sender.getTargetBlock(null, 10)
        sender.sendMessage("${block.type.name} ${block.blockData}")
        return true
    }


}