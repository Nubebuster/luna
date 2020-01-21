package roenstaak.npcSpawning

import api.item.LootTable
import api.predef.addNpc
import api.predef.area
import api.predef.ctx
import io.luna.game.model.Area
import io.luna.game.model.Position
import io.luna.game.model.mob.Npc
import world.player.skill.fishing.fishingSpot.FishingSpot

/**
 * An [Area] implementation for wilderness areas.
 *
 * @author lare96 <http://github.com/lare96>
 */
class NPCSpawn(id: Int, val spawn: Position, val area: Area, val loot: List<LootTable>?) : Npc(ctx, id, spawn) {

    companion object {

        /**
         * A range of how npcs can move, in seconds.
         */
        val MOVE_INTERVAL = 8..20 // seconds.
    }

    /**
     * The countdown timer. This spot will move when it reaches 0.
     */
    var countdown = MOVE_INTERVAL.random()

    /**
     * Performs a countdown and returns true if the spot should be moved.
     */
    fun countdown(): Boolean {
        countdown--
        if (countdown <= 0) {
            countdown = MOVE_INTERVAL.random()
            return true
        }
        return false
    }

}