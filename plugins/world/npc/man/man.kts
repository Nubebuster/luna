import api.predef.*
import io.luna.game.event.impl.LoginEvent
import io.luna.game.event.impl.ObjectClickEvent
import io.luna.game.event.impl.ServerLaunchEvent
import io.luna.game.model.Direction
import io.luna.game.model.Position
import io.luna.game.model.item.Item

val giftItem = Item(995, 3) // coins

// Add first click event for NPC(1)
npc1(1) { plr.newDialogue()
        .npc(1, "Hello! How're you doing?")
        .player("Not too bad, just learning some scripting.")
        .npc(1, "And how's that going, pretty well?")
        .player("Yeah! I'm learning a lot.")
        .npc(1, "Well here's a little gift for all your hard work.")
        .player("Thanks! Guess I'll be going now.")
        .npc(1, "Okay, take care!")
        .open()
}

npc2(1) { plr.giveItem(giftItem)
    plr.sendMessage("You stole 3 gold coins from the man")
    plr.thieving.addExperience(15.0)
}


// Spawn NPC on launch
on(ServerLaunchEvent::class) {
    world.addNpc(494, 3090, 3245)
    world.addNpc(494, 3090, 3243).face(Direction.EAST)//facing not working
    world.addNpc(494, 3090, 3242).face(Position(3092, 3244))//facing not working
}

on(LoginEvent::class) {
}

on(ObjectClickEvent.ObjectFirstClickEvent::class) {
    println(gameObject.id)
}