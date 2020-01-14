package roenstaak.combat

import api.predef.*
import io.luna.game.action.Action
import io.luna.game.action.ConditionalAction
import io.luna.game.model.EntityState
import io.luna.game.model.def.NpcCombatDefinition
import io.luna.game.model.item.Equipment
import io.luna.game.model.mob.*
import java.security.InvalidParameterException
import kotlin.math.ceil


class CombatAction(private val attacker: Mob, private val victim: Mob) : ConditionalAction<Mob>(attacker, true, 1) {

    companion object {
        val last_swing = HashMap<Mob, Long>()
        const val attack_speed = 4
    }

    override fun start(): Boolean {
        println("start ")
        if (!attacker.position.isWithinDistance(victim.position, 3)) {
            attacker.walking.walk(attacker.position, victim.position)
            attacker.walking.removeLast()
            attacker.walking.removeLast()
        }
        return true
    }

    override fun condition(): Boolean {
        if (!victim.isAlive || !attacker.isAlive)
            return false
        if (!attacker.position.isWithinDistance(victim.position, 3)) {
            if (!attacker.position.isWithinDistance(victim.position, 10)) {
                return false
            }
            attacker.walking.walk(attacker.position, victim.position)
            attacker.walking.removeLast()
            attacker.walking.removeLast()
        }
        if (!last_swing.containsKey(attacker) || attacker.world.currentTick.minus(last_swing.getValue(attacker)) >= if (attacker is Npc)
                    attacker.combatDefinition.get().attackSpeed else attack_speed) {
            attacker.interact(victim)
            when (attacker) {
                is Player -> attacker.animation(Animation(451))
                is Npc -> {
                    attacker.animation(Animation(attacker.combatDefinition.get().attackAnimation))
                    victim as Player
                    if (victim.settings.isAutoRetaliate) {
                        /*if (!victim.interactingWith.isPresent ||
                                if (victim.interactingWith is Mob) !(victim.interactingWith.get() as Mob).isAlive else true) {*/
                            if(victim.actions.currentAction == null || victim.actions.currentAction.isInterrupted) {
                                println("retaliation")
                                victim.submitAction(CombatAction(victim, attacker))
                            }

                    }
                }
            }

            last_swing[attacker] = attacker.world.currentTick
            world.scheduleOnce(1) {
                val dmg = getDamage(attacker, victim)

                victim.damage(Hit(dmg, if (dmg == 0) Hit.HitType.BLOCKED else Hit.HitType.NORMAL))
                if (dmg > 0) {
                    when (attacker) {
                        is Player -> {
                            attacker.hitpoints.addExperience(1.33 * dmg)
                            attacker.attack.addExperience(1.33 * dmg)
                            attacker.defence.addExperience(1.33 * dmg)
                            attacker.strength.addExperience(1.33 * dmg)
                        }
                    }
                }
            }
        }

        return true
    }

    override fun ignoreIf(other: Action<*>?): Boolean {
        return false
    }
}

fun getDamage(attacker: Mob, victim: Mob): Int {
    when (attacker) {
        is Player -> {
            victim as Npc
            val slash = attacker.equipment.bonuses[Equipment.SLASH_ATTACK]
            val strength = attacker.equipment.bonuses[Equipment.STRENGTH]
            val baseDmg = .5 + (attacker.strength.level * (strength + 64.0) / 640.0)

            val max_attack_roll = attacker.attack.level * (slash + 64.0)
            val def = victim.combatDefinition.get().getSkill(NpcCombatDefinition.DEFENCE)
            val max_defence_roll = (if (def > 0) def else 1) *
                    (victim.combatDefinition.get().bonuses[NpcCombatDefinition.SLASH_DEFENCE] + 64.0)
            val hitChance = getHitChance(max_attack_roll, max_defence_roll)

            if (rand(100) > hitChance * 100) {
                return 0
            }
            return rand(0, ceil(baseDmg).toInt())
        }
        is Npc -> {
            victim as Player
            val slash = attacker.combatDefinition.get().bonuses[NpcCombatDefinition.SLASH_ATTACK]
            val strength = attacker.combatDefinition.get().bonuses[NpcCombatDefinition.STRENGTH]
            val baseDmg = .5 + (attacker.combatDefinition.get().getSkill(NpcCombatDefinition.STRENGTH) * (strength + 64.0) / 640.0)

            val max_attack_roll = attacker.combatDefinition.get().getSkill(NpcCombatDefinition.ATTACK) * (slash + 64.0)
            val def = victim.defence.level
            val max_defence_roll = (if (def > 0) def else 1) *
                    (victim.equipment.bonuses[Equipment.SLASH_DEFENCE] + 64.0)
            val hitChance = getHitChance(max_attack_roll, max_defence_roll)

            if (rand(100) > hitChance * 100) {
                return 0
            }
            return rand(0, ceil(baseDmg).toInt())
        }
    }
    throw InvalidParameterException("Parameters should be Player and Npc")
}

fun getHitChance(attackRoll: Double, defenceRoll: Double): Double {
    when (attackRoll > defenceRoll) {
        true -> return 1.0 - ((defenceRoll + 2.0) / (2.0 * (attackRoll + 1.0)))
        false -> return attackRoll / (2.0 * defenceRoll + 1)
    }
}

