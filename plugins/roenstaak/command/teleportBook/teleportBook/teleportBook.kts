package world.player.command.developer.teleportBook

import api.predef.RIGHTS_DEV
import api.predef.button
import api.predef.on
import io.luna.game.event.impl.ItemClickEvent
import io.luna.game.model.Position
import io.luna.game.model.item.Item
import io.luna.game.model.mob.Animation
import io.luna.game.model.mob.Player
import io.luna.net.msg.out.CloseWindowsMessageWriter
import io.luna.net.msg.out.InterfaceMessageWriter
import world.player.command.cmd

on(ItemClickEvent.ItemFirstClickEvent::class) {
    if (id == 7633) {
        /* plr.newDialogue().options(
                 "Goblins", { teleport(plr, Position(3242, 3241)) },
                 "Lumbridge", {teleport(plr, Position( 3222, 3218)) },
                 "Draynor", { teleport(plr, Position(3091, 3248))  },
                 "Varrock", {teleport(plr, Position(3207, 3429))  },
                 "Falador", { teleport(plr, Position(2964, 3380)) }).open()*/
        plr.queue(InterfaceMessageWriter(18790))
        plr.sendText("Ardougne", 18849)
        plr.sendText("Camelot", 18845)
        plr.sendText("Al Kharid", 18838)
    }
}

button(18828) {
    teleport(plr, Position(3091, 3248))//draynor
    plr.queue(CloseWindowsMessageWriter())
}

button(18829) {
    teleport(plr, Position(3242, 3241))//goblins
    plr.queue(CloseWindowsMessageWriter())
}

button(18830) {
    teleport(plr, Position(3222, 3218))//lumbridge
    plr.queue(CloseWindowsMessageWriter())
}

button(18836) {
    teleport(plr, Position(2964, 3380))//falador
    plr.queue(CloseWindowsMessageWriter())
}

button(18847) {
    teleport(plr, Position(3207, 3429))//varrock
    plr.queue(CloseWindowsMessageWriter())
}

button(18849) {
    teleport(plr, Position(2661, 3305))//Ardougne
    plr.queue(CloseWindowsMessageWriter())

}

button(18845) {
    teleport(plr, Position(2725, 3484))//Camelot
    plr.queue(CloseWindowsMessageWriter())
}


button(18838) {
    teleport(plr, Position(3270, 3167))//Al Kharid
    plr.queue(CloseWindowsMessageWriter())
}

fun teleport(plr: Player, pos: Position) {
    plr.teleport(pos)
}

cmd("teleport", RIGHTS_DEV) {
    plr.giveItem(Item(7633))
}

cmd("ani", RIGHTS_DEV) {
    plr.animation(Animation(args[0].toInt()))
}