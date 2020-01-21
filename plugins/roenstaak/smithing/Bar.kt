package roenstaak.smithing

enum class Bar(val oreIds: List<OreItem>, val barID: Int, val button: Int, val level: Int, val experience: Double) {
    BRONZE(listOf(OreItem(436, 1), OreItem(438, 1)), 2349, 3987, 1, 6.2),
    IRON(listOf(OreItem(440, 1)), 2351, 3991, 15, 12.5),
    SILVER(listOf(OreItem(442, 1)), 2355, 3995, 20, 13.7),
    STEEL(listOf(OreItem(440, 1), OreItem(453, 2)), 2353, 3999, 30, 17.5),
    GOLD(listOf(OreItem(444, 1)), 2357, 4003, 40, 22.5),
    MITHRIL(listOf(OreItem(447, 1), OreItem(453, 4)), 2359,7441, 50, 30.0),
    ADAMANTITE(listOf(OreItem(449, 1), OreItem(453, 6)), 2361, 7446, 70, 37.5),
    RUNITE(listOf(OreItem(451, 1), OreItem(453, 8)), 2363, 7450, 85,50.0)
}


class OreItem(val id:Int, val amount: Int)