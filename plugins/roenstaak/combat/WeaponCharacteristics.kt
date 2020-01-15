package roenstaak.combat

import io.luna.game.model.def.NpcCombatDefinition
import io.luna.game.model.item.Equipment
import io.luna.game.model.item.Item

enum class WeaponCharacteristics(var itemName: String, var attackSpeed: Int, var primary: AttackStyleType, var secondary: AttackStyleType) {
    Dagger("dagger", 4, AttackStyleType.Stab, AttackStyleType.Slash),
    PickAxe("pickaxe", 5, AttackStyleType.Stab, AttackStyleType.Crush),
    BattleAxe("battleaxe", 6, AttackStyleType.Slash, AttackStyleType.Crush),
    Axe("axe", 5, AttackStyleType.Slash, AttackStyleType.Crush),
    Mace("mace", 5, AttackStyleType.Crush, AttackStyleType.Stab),
    Scimitar("scimitar", 4, AttackStyleType.Slash, AttackStyleType.Stab),
    Longsword("longsword", 5, AttackStyleType.Slash, AttackStyleType.Stab),
    Warhammer("warhammer", 6, AttackStyleType.Crush, AttackStyleType.Crush),
    TwoHandSword("2h sword", 7, AttackStyleType.Slash, AttackStyleType.Crush),
    Halberd("halberd", 7, AttackStyleType.Stab, AttackStyleType.Slash),
    Spear("spear", 5, AttackStyleType.Stab, AttackStyleType.Slash),
    Claws("claws", 4, AttackStyleType.Slash, AttackStyleType.Stab),
    Maul("maul", 7, AttackStyleType.Crush, AttackStyleType.Crush),
    Sword("sword", 4, AttackStyleType.Stab, AttackStyleType.Slash),
    Hand("easter egg here", 4, AttackStyleType.Crush, AttackStyleType.Crush);

    companion object {
        fun getWeaponCharacteristics(item: Item?): WeaponCharacteristics? {
            if (item == null) return null
            val name = item.itemDef.name.toLowerCase()
            for (ch in values())
                if (name.contains(ch.itemName)) return ch
            return null
        }
    }
}

enum class AttackStyleType {
    Stab, Slash, Crush;

    fun getEquipmentStatIndex(): Int {
        if (this == Stab) return Equipment.STAB_ATTACK
        if (this == Slash) return Equipment.SLASH_ATTACK
        if (this == Crush) return Equipment.CRUSH_ATTACK
        throw Exception("should not reach this")
    }

    //TODO this is only pvm not pvp or mvp
    fun getDefenceStatIndex(): Int {
        if (this == Stab) return NpcCombatDefinition.STAB_DEFENCE
        if (this == Slash) return NpcCombatDefinition.SLASH_DEFENCE
        if (this == Crush) return NpcCombatDefinition.CRUSH_DEFENCE
        throw Exception("should not reach this")
    }
}