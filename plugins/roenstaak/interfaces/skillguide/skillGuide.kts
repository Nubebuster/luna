package world.player.`interface`.skillGuide

import api.predef.button
import io.luna.game.model.mob.Player
import io.luna.net.msg.out.InterfaceMessageWriter
import io.luna.net.msg.out.WidgetItemModelMessageWriter
import java.awt.Color


/**
 * At skill its 8720..8759
 * At Advancement its 8760..8799
 *
 */

button(8654) {
   mainInterface(plr);
/*
    for(i in 8720..
            10000)
        plr.sendText("HAI" + i, i)

*/
}

fun mainInterface(plr:Player) {
    plr.queue(InterfaceMessageWriter(8714))
    plr.sendText("Skill guide", 8849)

    plr.sendText("Attack", 8846)
    plr.sendText("Defence", 8823)
    plr.sendText("Ranged", 8824)
    plr.sendText("Prayer", 8827)
    plr.sendText("Magic", 8837)
    plr.sendText("Herblore", 8840)
    plr.sendText("Thieving", 8843)
    plr.sendText("Crafting", 8859)
    plr.sendText("Mining", 8862)
    plr.sendText("Smithing", 8865)
    plr.sendText("Fishing", 15303)
    plr.sendText("Cooking", 15306)
    plr.sendText("Woodcutting", 15309)
}


button(8846) {
    plr.sendText( "Attack", 8716)

    plr.sendText(  "1", 8720)
    plr.sendText(  "Wield bronze and iron weaponry", 8760)


    plr.sendText(  "5", 8721)
    plr.sendText(  "Wield steel weaponry", 8761)

    plr.queue(WidgetItemModelMessageWriter(8760, 100, 1323))//iron scim
}

button(8823) {
    plr.sendText( "Defence", 8716)

    plr.sendText(  "1", 8720)
    plr.sendText(  "Wield bronze and iron armour", 8760)


    plr.sendText(  "5", 8721)
    plr.sendText(  "Wield steel armour", 8761)

}
