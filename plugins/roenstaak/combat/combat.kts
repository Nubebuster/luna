import api.predef.*
import io.luna.game.event.impl.MobEvent
import io.luna.game.event.impl.NpcClickEvent
import io.luna.game.model.mob.Mob
import io.luna.game.model.mob.PlayerInteraction
import io.luna.game.task.Task
import io.luna.net.msg.out.NpcUpdateMessageWriter
import io.luna.net.msg.out.PlayerInteractionMessageWriter


npc3(81) {
    world.schedule(1, true) {
        plr.interact(npc)
        plr.walking.walk(plr.position, npc.position)
        if(npc.health <=0) {
            it.cancel()
            println("death")
        }

        npc.health -= 1
        //npc.combatDefinition.get().attackAnimation
        plr.queue(PlayerInteractionMessageWriter(PlayerInteraction.ATTACK))
        plr.queue(NpcUpdateMessageWriter())

    }

}


