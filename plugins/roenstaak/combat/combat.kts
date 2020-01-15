import api.predef.button
import api.predef.on
import api.predef.scheduleOnce
import api.predef.weapon
import io.luna.game.event.impl.*
import io.luna.game.model.item.Item
import io.luna.game.model.mob.Player
import roenstaak.combat.CombatAction
import roenstaak.combat.WeaponCharacteristics

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
    CombatAction.attackStyle.remove(plr)
}

on(LoginEvent::class) {
    CombatAction.attackStyle[plr] = CombatAction.AttackStyle.ACCURATE
    val weapon = plr.equipment.weapon ?: return@on
    sendWeaponInterfaceInfo(plr, weapon)
}

on(EquipItemEvent::class) {
    val weapon = plr.inventory[index]
    sendWeaponInterfaceInfo(plr, weapon)
}
//TODO add combat option sprites for non slash weapons
fun sendWeaponInterfaceInfo(plr: Player, weapon: Item) {
    plr.sendText(weapon.itemDef.name, 2426)

    val characteristics = WeaponCharacteristics.getWeaponCharacteristics(weapon)

    val primary = characteristics?.primary?.name ?: return//TODO check if every weapon is in this characteristics
    val secondary = characteristics?.secondary.name

    val accurate = 2445
    val aggressive = 2446
    val controlled = 2447
    val defensive = 2448
    plr.sendText(primary, accurate)
    plr.sendText(primary, aggressive)
    plr.sendText(secondary, controlled)
    plr.sendText(primary, defensive)
}

button(2429) {
    CombatAction.attackStyle[plr] = CombatAction.AttackStyle.ACCURATE
}
button(2432) {
    CombatAction.attackStyle[plr] = CombatAction.AttackStyle.AGGRESSIVE
}
button(2431) {
    CombatAction.attackStyle[plr] = CombatAction.AttackStyle.CONTROLLED
}
button(2430) {
    CombatAction.attackStyle[plr] = CombatAction.AttackStyle.DEFENSIVE
}