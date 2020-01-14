import api.predef.on
import api.predef.scheduleOnce
import io.luna.game.event.impl.LogoutEvent
import io.luna.game.event.impl.NpcClickEvent
import io.luna.game.event.impl.NpcDeathEvent
import io.luna.game.event.impl.PlayerDeathEvent
import roenstaak.combat.CombatAction

//TODO("fix mobs walking and walking. fix weapon stuff. Add prayers etc. elaborate damage calculation")

on(NpcClickEvent.NpcThirdClickEvent::class) {
    var attackable = false
    for (a in npc.definition.actions)
        if (a == "Attack")
            attackable = true

    if (!attackable)
        return@on

    if (!plr.position.isWithinDistance(npc.position, 2)) {
        plr.walking.walk(plr.position, npc.position)
        plr.walking.removeLast()
    }

    plr.submitAction(CombatAction(plr, npc))

    npc.world.scheduleOnce(2) {
        npc.submitAction(CombatAction(npc, plr))
    }
}

on(NpcDeathEvent::class) {
    CombatAction.last_swing.remove(npc)
}

on(PlayerDeathEvent::class) {
    CombatAction.last_swing.remove(plr)
}

on(LogoutEvent::class) {
    CombatAction.last_swing.remove(plr)
}