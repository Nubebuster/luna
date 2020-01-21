import api.item.LootTable
import api.item.LootTableItem
import api.predef.on
import api.predef.schedule
import api.predef.scheduleOnce
import api.predef.world
import io.luna.game.event.impl.NpcDeathEvent
import io.luna.game.event.impl.ServerLaunchEvent
import io.luna.game.model.Area
import io.luna.game.model.Position
import io.luna.game.model.def.NpcDefinition
import io.luna.util.Rational
import roenstaak.npcSpawning.NPCSpawn


val npcSpawns: ArrayList<NPCSpawn> = ArrayList(listOf(NPCSpawn(520, Position(3215, 3416), Area(3214, 3414, 3217, 3418), null),
        NPCSpawn(520, Position(2958, 3387), Area(2957, 3385, 2959, 3388), null)))


fun moveNPCS() {
    npcSpawns.stream()
            .filter { it.countdown() }
            .forEach {
                it.walking.walk(it.position, it.area.random())
            }
}

fun addSpots() = npcSpawns.forEach {
    world.npcs.add(it)
}

fun lookup(name: String): List<Int> {
    val ids = ArrayList<Int>()
    NpcDefinition.ALL.forEach {
        if (it != null && it.name == name) ids.add(it.id)
    }
    return ids
}

on(NpcDeathEvent::class) {
    val checkedNpc = npc
    if (checkedNpc is NPCSpawn) {
        npcSpawns.remove(checkedNpc)
        val newSpawn = NPCSpawn(checkedNpc.id, checkedNpc.spawn, checkedNpc.area, checkedNpc.loot)
        npcSpawns.add(newSpawn)
        world.scheduleOnce(40) { world.npcs.add(newSpawn) }//24 seconds
    }
}

fun add(id: Int, area: Area, loot: List<LootTable>?) {
    npcSpawns.add(NPCSpawn(id, area.random(), area, loot))
}

fun add(amount: Int, id: Int, area: Area, loot: List<LootTable>?) {
    for (i in 1..amount)
        npcSpawns.add(NPCSpawn(id, area.random(), area, loot))
}

/**
 * Looks for ids with the name and picks one random of the results
 */
fun add(name: String, area: Area, loot: List<LootTable>?) {
    npcSpawns.add(NPCSpawn(lookup(name).random(), area.random(), area, loot))
}

fun add(amount: Int, name: String, area: Area, loot: List<LootTable>?) {
    for (i in 1..amount)
        npcSpawns.add(NPCSpawn(lookup(name).random(), area.random(), area, loot))
}

val boneDropTable = LootTable(listOf(LootTableItem("Bones", IntRange(1, 1), Rational.ALWAYS, false)))
val coinsID = 995

on(ServerLaunchEvent::class) {

    val wizards = Area(3219, 3362, 3236, 3377)
    for (id in 3242..3245)
        add(2, id, wizards, null)

    val cows = Area(3253, 3255, 3265, 3296)
    val cowLoot = listOf(
            LootTable(listOf(
                    LootTableItem("Raw beef", 1, Rational.ALWAYS, false)
            )),
            boneDropTable,
            LootTable(listOf(
                    LootTableItem("Cowhide", 1, Rational.ALWAYS, false)
            )))
    add(12, "Cow", cows, cowLoot)

    val cows2 = Area(3242, 3283, 3255, 3298)
    val goblins = Area(3248, 3230, 3262, 3242)
    val goblinLoot = listOf(
            LootTable(listOf(
                    LootTableItem(1007, IntRange(1, 1), Rational(1, 128)),//red cape
                    LootTableItem("Bronze axe", 1, Rational(1, 43), false),
                    LootTableItem("Tin ore", 1, Rational(1, 128), false),
                    LootTableItem("Hammer", 1, Rational(1, 9), false),
                    LootTableItem("Chef's hat", 1, Rational(1, 43), false),
                    LootTableItem("Beer", 1, Rational(1, 64), false))),
            LootTable(listOf(
                    LootTableItem("Water rune", IntRange(6, 8), Rational(1, 21), false),
                    LootTableItem("Body rune", 7, Rational(1, 26), false),
                    LootTableItem("Earth rune", IntRange(4, 8), Rational(1, 43), false),
                    LootTableItem("Mind rune", IntRange(4, 8), Rational(1, 43), false),
                    LootTableItem("Chaos rune", 1, Rational(1, 64), false),
                    LootTableItem("Nature rune", 1, Rational(1, 64), false)
            )),
            LootTable(listOf(
                    LootTableItem(coinsID, IntRange(1, 15), Rational.VERY_COMMON)
            )),
            boneDropTable
    )

    add(8, "Cow", cows2, cowLoot)
    add(8, "Goblin", goblins, goblinLoot)


    val fireGiantLoot = listOf(
            LootTable(listOf(
                    LootTableItem("Rune scimitar", 1, Rational(1, 128), false),
                    LootTableItem("Steel axe", 1, Rational(1, 43), false),
                    LootTableItem("Mithril sq shield", 1, Rational(1, 64), false),
                    LootTableItem("Fire battlestaff", 1, Rational(1, 128), false)
            )),
            LootTable(listOf(
                    LootTableItem("Fire rune", 150, Rational(1, 13), false),
                    LootTableItem("Chaos rune", 5, Rational(1, 18), false),
                    LootTableItem("Rune arrow", 12, Rational(1, 26), false),
                    LootTableItem("Fire rune", 37, Rational(1, 128), false),
                    LootTableItem("Law rune", 2, Rational(1, 128), false)
            )),
            LootTable(listOf(
                    LootTableItem("Lobster", 1, Rational(1, 43), false),
                    LootTableItem("Steel bar", 1, Rational(1, 64), false),
                    LootTableItem("Strength potion(2)", 1, Rational(1, 128), false)
            )),
            boneDropTable
    )

    add("Fire giant", cows2, fireGiantLoot)

    val hobgoblinLoot = listOf(
            LootTable(listOf(
                    LootTableItem("Law rune", 2, Rational(1, 43), false),
                    LootTableItem("Water rune", 2, Rational(1, 64), false),
                    LootTableItem("Fire rune", 7, Rational(1, 64), false),
                    LootTableItem("Body rune", 6, Rational(1, 64), false),
                    LootTableItem("Chaos rune", 3, Rational(1, 64), false),
                    LootTableItem("Nature rune", 4, Rational(1, 64), false),
                    LootTableItem("Cosmic rune", 2, Rational(1, 128), false)
            )),
            LootTable(listOf(
                    LootTableItem("Limpwurt root", 1, Rational(1, 6), false)
            )),
            boneDropTable
    )

    add(8, "Hobgoblin", cows, hobgoblinLoot)


    if (npcSpawns.isNotEmpty()) {
        addSpots()

        world.schedule(1) {
            moveNPCS()
        }
    }
}
