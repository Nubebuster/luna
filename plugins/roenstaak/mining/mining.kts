package roenstaak.mining

import api.predef.addArticle
import api.predef.mining
import api.predef.on
import api.predef.weapon
import io.luna.game.event.impl.ObjectClickEvent

on(ObjectClickEvent.ObjectFirstClickEvent::class) {
    val ore = Ore.values().filter {
        it.ids.contains(id)
    }.firstOrNull() ?: return@on

    if(ore.level > plr.mining.level) {
        plr.sendMessage("You need level ${ore.level} Mining to mine ${ore.ore}.")
        return@on
    }

    var axe = Pickaxe.getFromID(plr.equipment.weapon?.id ?: 0)

    if (axe?.level ?: 0 > plr.mining.level) axe = null

    plr.inventory.forEach {
        if (it != null) {
            val tAxe = Pickaxe.getFromID(it.id)
            if (tAxe != null && tAxe.level <= plr.mining.level &&
                    tAxe.level > axe?.level ?: 0) axe = tAxe
        }
    }

    if (axe == null) {
        plr.sendMessage("You do not have a pickaxe you are skilled enough to use.")
        return@on
    }
    plr.submitAction(MiningAction(plr, gameObject, ore, axe!!))


}

