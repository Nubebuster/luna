package roenstaak.mining

enum class Ore(val ore: String, val ids: List<Int>, val level: Int, val experience: Double, val speed: Int){
    TIN("Tin ore", listOf(2095, 2094), 1,17.5, 7),
    COPPER("Copper ore", listOf(2091, 2090), 1,17.5, 7),
    IRON("Iron ore", listOf(2093, 2092), 15,35.0,10),

    SILVER("Silver ore", listOf(2101, 2100), 20,40.0, 12),
    COAL("Coal", listOf(2097, 2096), 30,50.0, 15),
    GOLD("Gold ore", listOf(2099, 2098), 40,65.0, 12),
    MITHRIL("Mithril ore", listOf(2103, 2102), 55,80.0, 24),
    ADAMANTITE("Adamantite ore", listOf(2105, 2104), 70,95.0, 96),
    RUNITE("Runite ore", listOf(14860, 14859), 85,1215.0, 128)



}