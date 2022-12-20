package dev.wesley.shooter.entity.animations.enums

enum class AnimationModel {
    PLAYER,
    SLIME,
    CHEST,
    UNDEFINED;

    val atlasKey : String = this.toString().lowercase()
}