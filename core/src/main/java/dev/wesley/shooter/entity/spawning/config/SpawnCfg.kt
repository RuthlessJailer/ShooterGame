package dev.wesley.shooter.entity.spawning.config

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import dev.wesley.shooter.entity.animations.enums.AnimationModel
import ktx.math.vec2

data class SpawnCfg(
    val model : AnimationModel,
    val speedScaling : Float = 1f,
    val physicScaling : Vector2 = vec2(1f, 1f),
    val physicOffset : Vector2 = vec2(0f, 0f),
    val bodyType: BodyDef.BodyType = BodyDef.BodyType.StaticBody,
    val canAttack : Boolean = true,
    val attackScaling : Float = 1f,
    val attackDelay : Float = 0.2f,
    val attackExtraRange : Float = 0f,
    val lifeScaling : Float = 1f,
    val lootable : Boolean = false,
)