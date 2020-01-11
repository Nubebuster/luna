import api.predef.on
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

    for (i in 1..2) {
        for (id in 3242..3245)
            npcSpawns.add(NPCSpawn(id, Area(3219, 3362, 3236, 3377).random(),
                    Area(3219, 3362, 3236, 3377)))
    }

    if (npcSpawns.isNotEmpty()) {
        addSpots()

        world.schedule(1) {
            moveNPCS()
        }
    }
}

