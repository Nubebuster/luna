package roenstaak.mining

import io.luna.game.model.mob.Animation

enum class Pickaxe(val id: Int, val level: Int, val speed: Double, val animation: Int) {
    BRONZE(1265, 0,1.0, 625),
    IRON(1267, 1, 1.1,626),
    STEEL(1269, 6, 1.21,627),
    MITHRIL(1273, 21, 1.331,629),
    ADAMANT(1271, 31, 1.463,628),
    RUNE(1275, 41, 1.611,624);


    companion object {
        fun getFromID(id: Int): Pickaxe? {
            values().forEach {
                if (it.id == id) return it
            }
            return null
        }
    }
}