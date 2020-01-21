package roenstaak.mining

import api.predef.addArticle
import api.predef.mining
import api.predef.rand
import api.predef.schedule
import io.luna.game.action.Action
import io.luna.game.action.InventoryAction
import io.luna.game.model.`object`.GameObject
import io.luna.game.model.def.ItemDefinition
import io.luna.game.model.item.Item
import io.luna.game.model.mob.Animation
import io.luna.game.model.mob.Player

class MiningAction(val player: Player, val rock: GameObject, val ore: Ore, val axe: Pickaxe) : InventoryAction(player, false, 1/* rand(8, 20)*/, 28) {


    override fun executeIf(start: Boolean): Boolean {
        if (player.inventory.isFull)
            return false

        mob.sendMessage("You swing your pickaxe at the rock...")
        player.animation(Animation(axe.animation))
        return true
    }

    override fun execute() {
        //TODO remove rock
        delay = rand(8, 12)
        val item = ItemDefinition.ALL.filter { it != null && it.name == ore.ore }.first()
        player.inventory.add(Item(item.id, 1))
        player.mining.addExperience(ore.experience)
        player.sendMessage("You mine ${addArticle(ore.ore)}.")
        delay = determineDelay()


        world.schedule(5) {
            if (isInterrupted) {
                it.cancel()
                return@schedule
            }
            player.animation(Animation(axe.animation))
        }
    }

    private fun determineDelay(): Int {
        val ticks = ore.speed * 8
        val levelFactor = player.mining.level / 10 + 1
        val pickModifier = axe.speed
        val result = (ticks / levelFactor) * (1 / pickModifier)
        return rand(2, 2 + (result.toInt()))
    }


    override fun ignoreIf(other: Action<*>?): Boolean {
        return false
    }
}