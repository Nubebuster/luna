package world.player.command.developer.teleportBook

import api.predef.*
import io.luna.game.event.impl.ItemClickEvent
import io.luna.game.model.Position
import io.luna.game.model.item.Item
import io.luna.game.model.mob.Player
import io.luna.net.msg.out.CloseWindowsMessageWriter
import io.luna.net.msg.out.InterfaceMessageWriter
import world.player.command.cmd

on(ItemClickEvent.ItemFirstClickEvent::class) {
    if(id == 7633) {
       /* plr.newDialogue().options(
                "Goblins", { teleport(plr, Position(3242, 3241)) },
                "Lumbridge", {teleport(plr, Position( 3222, 3218)) },
                "Draynor", { teleport(plr, Position(3091, 3248))  },
                "Varrock", {teleport(plr, Position(3207, 3429))  },
                "Falador", { teleport(plr, Position(2964, 3380)) }).open()*/
        plr.queue(InterfaceMessageWriter(18790))
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
    teleport(plr, Position( 3222, 3218))//lumbridge
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

fun teleport(plr: Player, pos: Position) {
    plr.teleport(pos)
}

cmd("teleport", RIGHTS_DEV) {
    plr.giveItem(Item(7633))
}