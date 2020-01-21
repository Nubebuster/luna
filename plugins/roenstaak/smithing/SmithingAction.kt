package roenstaak.smithing

import api.predef.addArticle
import api.predef.itemDef
import api.predef.smithing
import io.luna.game.action.Action
import io.luna.game.action.InventoryAction
import io.luna.game.model.item.Item
import io.luna.game.model.mob.Player

class SmithingAction(val plr: Player, val bar: Bar) : InventoryAction(plr, false, 4, 28) {


    override fun executeIf(start: Boolean): Boolean {
        if (bar.level > plr.smithing.level) {
            plr.sendMessage("You need level ${bar.level} Smithing to make ${addArticle(itemDef(bar.barID).name)}.")
            return false
        }
        bar.oreIds.forEach {
            if (!plr.inventory.contains(it.id) || it.amount > plr.inventory.filter { it2 -> it2 != null && it2.id == it.id }.size) {
                if (start) {
                    var requirements = ""
                    bar.oreIds.forEach { req ->
                        if (requirements.isEmpty())
                            requirements = "" + req.amount + " " + itemDef(req.id).name
                        else
                            requirements += " and " + req.amount + " " + itemDef(req.id).name
                    }

                    plr.sendMessage("You need $requirements to make ${addArticle(itemDef(bar.barID).name)}.")
                }
                return false
            }
        }

        //TODO animate
        return true
    }

    override fun execute() {
        bar.oreIds.forEach {
            if (!plr.inventory.contains(it.id) || it.amount > plr.inventory.filter { it2 -> it2 != null && it2.id == it.id }.size) {
                return
            }
        }
        bar.oreIds.forEach {
            for(am in 1..it.amount)
              plr.inventory.remove(Item( it.id,1))
        }
        plr.inventory.add(Item(bar.barID, 1))
        plr.smithing.addExperience(bar.experience)
        plr.sendMessage("You smelt the ores into ${addArticle(itemDef(bar.barID).name)}.")
    }

    override fun ignoreIf(other: Action<*>?): Boolean {
        return false
    }
}