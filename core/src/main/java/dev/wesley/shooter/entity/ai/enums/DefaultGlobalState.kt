package dev.wesley.shooter.entity.ai.enums

import dev.wesley.shooter.entity.ai.AiEntity
import dev.wesley.shooter.entity.ai.interfaces.EntityState

enum class DefaultGlobalState : EntityState {
    CHECK_ALIVE {
        override fun update(entity: AiEntity) {
            if (entity.isDead) {
                entity.enableGlobalState(false)
                entity.state(DefaultState.DEAD, true)
            }
        }
    }
}