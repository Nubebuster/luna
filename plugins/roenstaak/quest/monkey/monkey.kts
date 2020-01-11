
import api.predef.*
import io.luna.game.event.impl.LoginEvent
import io.luna.game.event.impl.ServerLaunchEvent
import io.luna.game.model.Area
import io.luna.game.model.item.IndexedItem
import io.luna.game.model.item.Item
import io.luna.game.model.mob.WalkingQueue
import io.luna.net.msg.out.CloseWindowsMessageWriter
import io.luna.net.msg.out.ColorChangeMessageWriter
import io.luna.net.msg.out.InterfaceMessageWriter
import io.luna.net.msg.out.WidgetIndexedItemsMessageWriter
import java.awt.Color
import java.util.function.Consumer


val area = Area(3075, 3259, 3079, 3261);

on(ServerLaunchEvent::class) {
    val npc = world.addNpc(1464, 3077, 3260, 0)

    world.schedule( 5){
        npc.walking.walk(npc.position, area.random())
    }
}

npc1(1464) {
/*    npc.walking.add(WalkingQueue.Step(npc.position.x + 1, npc.position.y))
    npc.walking.process()*/
    if(plr.inventory.contains(1963)) {
        plr.newDialogue().npc(1464, "OOOWAOOAO GIVE").player("What do I get for it?")
                .npc(1464,"?!?@???").player("Hmmmm").options("Okey...", {
                    plr.inventory.remove(Item(1963))
                    world.scheduleOnce(1) {
                        plr.queue(InterfaceMessageWriter(297))
                        plr.sendText("", 4444)
                        plr.sendText("A monkey bar!", 6158)
                        plr.sendText("", 303)
                        plr.sendText("", 304)
                        plr.inventory.add(Item(4014))
                    }
                }, "No!", { }).open()


    } else plr.newDialogue().npc(1464, "Help!").player("Waddup?").npc(1464, "Owaaooo Bananana")
            .player("Sure!", "I will get you a banana...").give(Item(4012, 1)).open()
}


button(7332) {
    plr.queue(ColorChangeMessageWriter(7332, 0))
    plr.queue(ColorChangeMessageWriter(8144, 0))
    plr.queue(InterfaceMessageWriter(8134))

    for( i in 8145..8195) {
        plr.sendText("", i)
    }

    for( i in 12174..12223) {
        plr.sendText("", i)
    }

    plr.sendText("Monkey Quest Log", 8145)
    plr.sendText("TODO", 8147)
    plr.sendText("Find a banana", 8149)
    plr.sendText("Give banana", 8150)
}

button(8657) {

    plr.queue(InterfaceMessageWriter(18790))

    plr.queue(WidgetIndexedItemsMessageWriter(18800, IndexedItem(0, 1963, 1)))

}

button(18796) {

    plr.queue(CloseWindowsMessageWriter())

}


on(LoginEvent::class) {
    plr.sendText("The Monkey is Nuts!", 7332)
}