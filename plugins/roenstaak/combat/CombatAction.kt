package roenstaak.combat

import api.predef.*
import io.luna.game.action.Action
import io.luna.game.action.ConditionalAction
import io.luna.game.model.def.NpcCombatDefinition
import io.luna.game.model.item.Equipment
import io.luna.game.model.mob.*
import java.security.InvalidParameterException
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min


class CombatAction(val attacker: Mob, val victim: Mob) : ConditionalAction<Mob>(attacker, true, 1) {

    enum class AttackStyle {
        ACCURATE, AGGRESSIVE, DEFENSIVE, CONTROLLED;//TODO there are actually different styles per weapon
    }

    companion object {
        val last_swing = HashMap<Mob, Long>()
        val lootOwner = HashMap<Mob, Player>()
        val attackStyle = HashMap<Player, AttackStyle>()
    }

    override fun start(): Boolean {
        //println("start ")
        if (!attacker.position.isWithinDistance(victim.position, 3)) {
            attacker.walking.clear()
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
            attacker.walking.clear()
            attacker.walking.walk(attacker.position, victim.position)
            attacker.walking.removeLast()
            attacker.walking.removeLast()
        }

        val attackSpeed = when (attacker) {
            is Npc -> attacker.combatDefinition.get().attackSpeed
            is Player -> WeaponCharacteristics.getWeaponCharacteristics(attacker.equipment.weapon)?.attackSpeed ?: 4
            else -> {
                println("cannot find attack speed of mob " + attacker.javaClass.name)
                return false
            }
        }

        if (!last_swing.containsKey(attacker) || attacker.world.currentTick.minus(last_swing.getValue(attacker)) >= attackSpeed) {
            attacker.interact(victim)
            when (attacker) {
                is Player -> attacker.animation(Animation(451))
                is Npc -> {
                    attacker.animation(Animation(attacker.combatDefinition.get().attackAnimation))
                    victim as Player
                    if (victim.settings.isAutoRetaliate) {
                        if (victim.actions.currentAction == null || victim.actions.currentAction.isInterrupted) {
                            //println("retaliation")
                            victim.submitAction(CombatAction(victim, attacker))
                        }
                    }
                }
            }

            last_swing[attacker] = attacker.world.currentTick

            val dmg = getDamage(attacker, victim)
            if (dmg > 0) {
                when (attacker) {
                    is Player -> {
                        when (attackStyle[attacker]) {
                            AttackStyle.CONTROLLED -> {
                                attacker.attack.addExperience(1.33 * dmg)
                                attacker.defence.addExperience(1.33 * dmg)
                                attacker.strength.addExperience(1.33 * dmg)
                            }
                            AttackStyle.ACCURATE ->
                                attacker.attack.addExperience(4.0 * dmg)
                            AttackStyle.AGGRESSIVE ->
                                attacker.strength.addExperience(4.0 * dmg)
                            AttackStyle.DEFENSIVE ->
                                attacker.defence.addExperience(4.0 * dmg)
                        }
                        attacker.hitpoints.addExperience(1.33 * dmg)
                    }
                }
            }
            world.scheduleOnce(1) {
                victim.damage(Hit(dmg, if (dmg == 0) Hit.HitType.BLOCKED else Hit.HitType.NORMAL))
                if (attacker is Player)
                    if (victim.health == 0) {
                        attacker as Player
                        lootOwner[victim] = attacker
                    }
            }
        }

        return true
    }

    override fun ignoreIf(other: Action<*>?): Boolean {
        return false
    }


    private fun getDamage(attacker: Mob, victim: Mob): Int {
        when (attacker) {
            // Player attacking NPC
            is Player -> {
                victim as Npc
                val potionBonus = 0//TODO potions
                val prayerBonus = 1.0//TODO prayers
                val strength = floor((attacker.equipment.bonuses[Equipment.STRENGTH] + potionBonus) * prayerBonus) +
                        (if (attackStyle[attacker] == AttackStyle.AGGRESSIVE) 3 else 0)


                val attackStyleType =
                        if (attackStyle[attacker] == AttackStyle.CONTROLLED) WeaponCharacteristics.getWeaponCharacteristics(attacker.equipment.weapon)?.secondary
                                ?: WeaponCharacteristics.Hand.secondary
                        else WeaponCharacteristics.getWeaponCharacteristics(attacker.equipment.weapon)?.primary
                                ?: WeaponCharacteristics.Hand.primary
                val styleWeaponBonus = attacker.equipment.bonuses[attackStyleType.getEquipmentStatIndex()]

                val baseDmg = .5 + (attacker.strength.level * (strength + 64.0) / 640.0)

                val maxAttackRoll = (attacker.attack.level + (if (attackStyle[attacker] == AttackStyle.ACCURATE) 3 else 0)) * (styleWeaponBonus + 64.0)
                val def = victim.combatDefinition.get().getSkill(NpcCombatDefinition.DEFENCE)
                val maxDefenceRoll = (if (def > 0) def else 1) *
                        (victim.combatDefinition.get().bonuses[attackStyleType.getDefenceStatIndex()] + 64.0)
                val hitChance = getHitChance(maxAttackRoll, maxDefenceRoll)

                if (rand(100) > hitChance * 100) {
                    return 0
                }
                return min(rand(0, ceil(baseDmg).toInt()), victim.health)
            }
            //NPC attacking player
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
                return min(rand(0, ceil(baseDmg).toInt()), victim.health)
            }
        }
        throw InvalidParameterException("Parameters should be Player and Npc")
    }

    private fun getHitChance(attackRoll: Double, defenceRoll: Double): Double {
        return if (attackRoll > defenceRoll) 1.0 - ((defenceRoll + 2.0) / (2.0 * (attackRoll + 1.0))) else attackRoll / (2.0 * defenceRoll + 1)
    }
}


