package dev.wesley.shooter.entity.spawning.component

import com.badlogic.gdx.math.Vector2
import ktx.math.vec2

class SpawnComponent(
    var type : String = "",
    var location : Vector2 = vec2()
) {

    companion object {
        const val DEFAULT_SPEED = 3f
        const val DEFAULT_ATTACK_DAMAGE = 5
        const val DEFAULT_LIFE = 15
    }
}