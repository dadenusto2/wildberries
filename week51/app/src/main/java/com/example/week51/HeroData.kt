package com.example.week51

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
class HeroData: Serializable {
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
    var turnRate: Double? = 0.0
    @Json(name = "cm_enabled")
    var cmEnabled: Boolean = true
    @Json(name = "legs")
    var legs: Double = 0.0
}