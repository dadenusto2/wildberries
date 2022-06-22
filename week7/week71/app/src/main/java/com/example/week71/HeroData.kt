package com.example.week71

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

/**
 * Класс для данных героя
 */
@JsonClass(generateAdapter = true)
class HeroData : Serializable {
    @Json(name = "localized_name")
    lateinit var localizedName: String

    @Json(name = "primary_attr")
    var primaryAttr: String = ""

    @Json(name = "attack_type")
    var attackType: String = ""

    @Json(name = "roles")
    var roles: List<String> = listOf()

    @Json(name = "img")
    var img: String = ""

    @Json(name = "icon")
    var icon: String = ""

    @Json(name = "base_health")
    var baseHealth: Double = 0.0

    @Json(name = "base_health_regen")
    var baseHealthRegen: Double = 0.0

    @Json(name = "base_mana")
    var baseMana: Double = 0.0

    @Json(name = "base_mana_regen")
    var baseManaRegen: Double = 0.0

    @Json(name = "base_armor")
    var baseArmor: Double = 0.0

    @Json(name = "base_mr")
    var baseMr: Double = 0.0

    @Json(name = "base_attack_min")
    var baseAttackMin: Double = 0.0

    @Json(name = "base_attack_max")
    var baseAttackMax: Double = 0.0

    @Json(name = "base_str")
    var baseStr: Double = 0.0

    @Json(name = "base_agi")
    var baseAgi: Double = 0.0

    @Json(name = "base_Int")
    var baseInt: Double = 0.0

    @Json(name = "str_gain")
    var strGain: Double = 0.0

    @Json(name = "agi_gain")
    var agiGain: Double = 0.0

    @Json(name = "int_gain")
    var intGain: Double = 0.0

    @Json(name = "attack_range")
    var attackRange: Double = 0.0

    @Json(name = "projectile_speed")
    var projectileSpeed: Double = 0.0

    @Json(name = "attack_rate")
    var attackRate: Double = 0.0

    @Json(name = "move_speed")
    var moveSpeed: Double = 0.0

    @Json(name = "turn_rate")
    var turnRate: Double? = null

    @Json(name = "cm_enabled")
    var cmEnabled: Boolean = true

    @Json(name = "legs")
    var legs: Double = 0.0
    override fun toString(): String {
        return "HeroData(localizedName='$localizedName', primaryAttr='$primaryAttr', attackType='$attackType', roles=$roles, img='$img', icon='$icon', baseHealth=$baseHealth, baseHealthRegen=$baseHealthRegen, baseMana=$baseMana, baseManaRegen=$baseManaRegen, baseArmor=$baseArmor, baseMr=$baseMr, baseAttackMin=$baseAttackMin, baseAttackMax=$baseAttackMax, baseStr=$baseStr, baseAgi=$baseAgi, baseInt=$baseInt, strGain=$strGain, agiGain=$agiGain, intGain=$intGain, attackRange=$attackRange, projectileSpeed=$projectileSpeed, attackRate=$attackRate, moveSpeed=$moveSpeed, turnRate=$turnRate, cmEnabled=$cmEnabled, legs=$legs)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HeroData

        if (localizedName != other.localizedName) return false
        if (primaryAttr != other.primaryAttr) return false
        if (attackType != other.attackType) return false
        if (roles != other.roles) return false
        if (img != other.img) return false
        if (icon != other.icon) return false
        if (baseHealth != other.baseHealth) return false
        if (baseHealthRegen != other.baseHealthRegen) return false
        if (baseMana != other.baseMana) return false
        if (baseManaRegen != other.baseManaRegen) return false
        if (baseArmor != other.baseArmor) return false
        if (baseMr != other.baseMr) return false
        if (baseAttackMin != other.baseAttackMin) return false
        if (baseAttackMax != other.baseAttackMax) return false
        if (baseStr != other.baseStr) return false
        if (baseAgi != other.baseAgi) return false
        if (baseInt != other.baseInt) return false
        if (strGain != other.strGain) return false
        if (agiGain != other.agiGain) return false
        if (intGain != other.intGain) return false
        if (attackRange != other.attackRange) return false
        if (projectileSpeed != other.projectileSpeed) return false
        if (attackRate != other.attackRate) return false
        if (moveSpeed != other.moveSpeed) return false
        if (turnRate != other.turnRate) return false
        if (cmEnabled != other.cmEnabled) return false
        if (legs != other.legs) return false

        return true
    }

    override fun hashCode(): Int {
        var result = localizedName.hashCode()
        result = 31 * result + primaryAttr.hashCode()
        result = 31 * result + attackType.hashCode()
        result = 31 * result + roles.hashCode()
        result = 31 * result + img.hashCode()
        result = 31 * result + icon.hashCode()
        result = 31 * result + baseHealth.hashCode()
        result = 31 * result + baseHealthRegen.hashCode()
        result = 31 * result + baseMana.hashCode()
        result = 31 * result + baseManaRegen.hashCode()
        result = 31 * result + baseArmor.hashCode()
        result = 31 * result + baseMr.hashCode()
        result = 31 * result + baseAttackMin.hashCode()
        result = 31 * result + baseAttackMax.hashCode()
        result = 31 * result + baseStr.hashCode()
        result = 31 * result + baseAgi.hashCode()
        result = 31 * result + baseInt.hashCode()
        result = 31 * result + strGain.hashCode()
        result = 31 * result + agiGain.hashCode()
        result = 31 * result + intGain.hashCode()
        result = 31 * result + attackRange.hashCode()
        result = 31 * result + projectileSpeed.hashCode()
        result = 31 * result + attackRate.hashCode()
        result = 31 * result + moveSpeed.hashCode()
        result = 31 * result + (turnRate?.hashCode() ?: 0)
        result = 31 * result + cmEnabled.hashCode()
        result = 31 * result + legs.hashCode()
        return result
    }
}