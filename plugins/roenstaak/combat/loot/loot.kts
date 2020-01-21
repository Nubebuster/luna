import api.item.LootTable
import api.item.LootTableItem
import api.predef.addItem
import api.predef.on
import api.predef.rand
import api.predef.world
import io.luna.game.event.impl.NpcDeathEvent
import io.luna.game.model.def.NpcDefinition
import io.luna.game.model.item.Item
import io.luna.game.model.mob.Npc
import io.luna.util.Rational
import roenstaak.npcSpawning.NPCSpawn

val rdt = LootTable(listOf(
        LootTableItem("Nature rune", IntRange(67, 67), Rational(1, 43), false),
        LootTableItem("Death rune", IntRange(45, 45), Rational(1, 64), false),
        LootTableItem("Rune arrow", IntRange(42, 42), Rational(1, 64), false),
        LootTableItem("Steel arrow", IntRange(150, 150), Rational(1, 64), false),

        LootTableItem("Rune 2h sword", IntRange(1, 1), Rational(1, 43), false),
        LootTableItem("Rune battleaxe", IntRange(1, 1), Rational(1, 43), false),
        LootTableItem("Rune sq shield", IntRange(1, 1), Rational(1, 64), false),
        LootTableItem("Dragon med helm", IntRange(1, 1), Rational(1, 128), false),
        LootTableItem("Rune kiteshield", IntRange(1, 1), Rational(1, 128), false),

        LootTableItem(985, IntRange(1, 1), Rational(1, 6)),//tooth half of key
        LootTableItem(987, IntRange(1, 1), Rational(1, 6)),//loop half of key
        LootTableItem("Runite bar", IntRange(1, 1), Rational(1, 26), false),
        LootTableItem("Dragonstone", IntRange(1, 1), Rational(1, 64), false),
        LootTableItem("Silver ore", IntRange(100, 100), Rational(1, 64), true)
))

val gdt = LootTable(listOf(
        LootTableItem("Uncut sapphire", 1, Rational(1, 4), false),
        LootTableItem("Uncut emerald", 1, Rational(1, 8), false),
        LootTableItem("Uncut ruby", 1, Rational(1, 16), false),
        LootTableItem("Uncut diamond", 1, Rational(1, 64), false),
        LootTableItem(985, IntRange(1, 1), Rational(1, 128)),//tooth half of key
        LootTableItem(987, IntRange(1, 1), Rational(1, 128)),//loop half of key
        LootTableItem("Dragon spear",1, Rational(1, 4096))
))

on(NpcDeathEvent::class) {
    when (npc.id) {
        in lookup("Goblin") -> {
            if (roll(Rational(1, 128))) doLootDrop(npc, rdt.pick())
        }
        in lookup("Fire giant") -> {
            if (roll(Rational(1, 11))) doLootDrop(npc, gdt.pick())
            if (roll(Rational(1, 64))) doLootDrop(npc, rdt.pick())
        }
        in lookup("Hobgoblin") -> {
            if (roll(Rational(1, 64))) doLootDrop(npc, gdt.pick())
            if (roll(Rational(1, 64))) doLootDrop(npc, rdt.pick())
        }
    }

    val smartNpc = npc
    if (smartNpc is NPCSpawn) {
        val loot = smartNpc.loot
        if (!loot.isNullOrEmpty())
            for (table in loot) {
                doLootDrop(npc, table.pick())
            }
    }
}

fun lookup(name: String): List<Int> {
    val ids = ArrayList<Int>()
    NpcDefinition.ALL.forEach {
        if (it != null && it.name == name) ids.add(it.id)
    }
    return ids
}

fun roll(chance: Rational): Boolean {
    return when {
        chance.numerator <= 0 -> false
        chance.numerator >= chance.denominator -> true
        rand(0, chance.denominator) <= chance.numerator -> true
        else -> false
    }
}


fun doLootDrop(npc: Npc, item: Item?) {
    if (item != null) {
        //if (killer is Player)
        //    npc.world.addItem(item.id, item.amount, npc.position, killer)
        //else
        npc.world.addItem(item.id, item.amount, npc.position, null)

        world.players.forEach {
            it.sendMessage("Drop: " + item.amount + " " + item.itemDef.name )

        }
    }

    /*        val action = npc.actions.currentAction
        if (action is CombatAction) {
            if (action.attacker is Player)
                npc.world.addItem(item.id, item.amount, npc.position, action.attacker)
            else
                npc.world.addItem(item.id, item.amount, npc.position, null)
        }*/
}