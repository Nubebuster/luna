import api.predef.*
import io.luna.game.event.impl.ItemOnObjectEvent
import io.luna.game.event.impl.ObjectClickEvent
import io.luna.game.event.impl.ServerLaunchEvent
import io.luna.game.model.`object`.GameObject
import io.luna.game.model.item.Item
import io.luna.game.model.mob.Animation
import io.luna.game.model.mob.Player
import io.luna.net.msg.out.ChunkPlacementMessageWriter
import io.luna.net.msg.out.InterfaceMessageWriter
import io.luna.net.msg.out.RemoveObjectMessageWriter
import world.player.command.cmd


val eatAnimation = Animation(618  )

// Add first click event for NPC(1)
/*
npc1(316) {
}
*/



cmd("give", RIGHTS_DEV) {
    plr.giveItem(Item(args[0].toInt()))
}

cmd("test", RIGHTS_DEV) {
   // plr.queue(MusicMessageWriter(args[0].toInt()))
    plr.queue(InterfaceMessageWriter(args[0].toInt()))

}





object1(1530	) {
    remove(plr, gameObject)
  //*  gameObject.hide()



    world.objects.unregister(gameObject)
    world.removeObject(gameObject)
    plr.giveItem(
            Item(303, 1))//small fishing net)

    plr.giveItem(
            Item(1351, 1))//bronze xe
}

object1(1516	) {
    remove(plr, gameObject)
}
object1(7049	) {
    remove(plr, gameObject)
}
object1(7050	) {
    remove(plr, gameObject)
}
object1(2069	) {
    remove(plr, gameObject)
}

object1(11707	) {
    remove(plr, gameObject)
}
object1(1536	) {
    remove(plr, gameObject)
}
object1(1519) {
    remove(plr, gameObject)
}
object1(2882) {
    remove(plr, gameObject)
}
object1(2883) {
    remove(plr, gameObject)
}
object1(1553) {
    remove(plr, gameObject)
}
object1(1551) {
    remove(plr, gameObject)
}

object1(59) {
    remove(plr, gameObject)
}

object1(1738) {
    plr.teleport(plr.position.translate(0, 0, 1))
}

object1(1739) {
    plr.teleport(plr.position.translate(0, 0, 1))
}
object2(1739) {
    plr.teleport(plr.position.translate(0, 0, 1))
}

object1(1740) {
    plr.teleport(plr.position.translate(0, 0, -1))
}
object1(1747) {
    plr.teleport(plr.position.translate(0, 0, 1))
}
object1(1746) {
    plr.teleport(plr.position.translate(0, 0, -1))
}

object1(60) {
    plr.teleport(plr.position.translate(0, 0, 1))
}
object1(9582) {
    plr.teleport(plr.position.translate(0, 0, 1))
}
object1(9584) {
    plr.teleport(plr.position.translate(0, 0, -1))
}

//TODO object3 is broken?
object3(1739) {
    plr.teleport(plr.position.translate(0, 0, -1))
}


fun remove(plr : Player, obj : GameObject) {
    plr.queue(ChunkPlacementMessageWriter(obj.position))
    plr.queue(RemoveObjectMessageWriter(0, 0,0))
}


// Spawn NPC on launch
on(ServerLaunchEvent::class) {
   /* for (i in 1..10000) {
        if (objectDef(i).name == "Door") {

            println( i)
        }
    }*/

    world.addNpc(316, 3239, 3244)
    world.addNpc(316, 3085, 3231)
    world.addNpc(316, 3086, 3227)

}

on(ObjectClickEvent.ObjectFirstClickEvent::class) {
    println(id)
}



on(ItemOnObjectEvent::class) {

    plr.sendMessage(gameObject.definition.name)
}

