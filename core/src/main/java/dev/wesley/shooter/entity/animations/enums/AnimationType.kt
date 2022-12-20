package dev.wesley.shooter.entity.animations.enums

enum class AnimationType {
    IDLE,
    RUN,
    ATTACK,
    DEATH,
    OPEN;

    val atlasKey : String = this.toString().lowercase()
}