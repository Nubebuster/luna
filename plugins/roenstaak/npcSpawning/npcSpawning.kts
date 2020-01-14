import api.predef.on
import api.predef.rand
import api.predef.schedule
import api.predef.world
import io.luna.game.event.impl.ServerLaunchEvent
import io.luna.game.model.Area
import io.luna.game.model.Position
import roenstaak.npcSpawning.NPCSpawn


val npcSpawns: ArrayList<NPCSpawn> = ArrayList(listOf(NPCSpawn(520, Position(3215, 3416), Area(3214, 3414, 3217, 3418)),
        NPCSpawn(520, Position(2958, 3387), Area(2957, 3385, 2959, 3388))))

/**
 * Attempts to move fishing spots from their 'home' to 'away' positions, and vice-versa.
 */
fun moveNPCS() {
    npcSpawns.stream()
            .filter { it.countdown() }
            .forEach {
                it.walking.walk(it.position, it.movementArea.random())
            }
}

/**
 * Spawns fishing spots.
 */
fun addSpots() = npcSpawns.forEach {
    world.npcs.add(it)
}

on(ServerLaunchEvent::class) {

    val wizards = Area(3219, 3362, 3236, 3377)
    for (i in 1..2) {
        for (id in 3242..3245)
            npcSpawns.add(NPCSpawn(id, wizards.random(),
                    wizards))
    }
    val cows =  Area(3253, 3255, 3265, 3296)
    for(i in 1..12) {
        npcSpawns.add(NPCSpawn(81, cows.random(),
                cows))
    }
    val cows2 = Area(3242, 3283, 3255, 3298)
    val goblins = Area(3248, 3230, 3262, 3242)
    for(i in 1..8) {
        npcSpawns.add(NPCSpawn(81, cows2.random(),
                cows2))
        npcSpawns.add(NPCSpawn(rand(100, 102), goblins.random(), goblins))
        npcSpawns.add(NPCSpawn(110, cows2.random(), cows2))//fire giant
    }

    if (npcSpawns.isNotEmpty()) {
        addSpots()

        world.schedule(1) {
            moveNPCS()
        }
    }
}

