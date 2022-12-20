package dev.wesley.shooter.entity.ai.interfaces

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import dev.wesley.shooter.entity.ai.AiEntity

interface EntityState : State<AiEntity> {
    override fun enter(p0: AiEntity) = Unit
    override fun update(p0: AiEntity) = Unit
    override fun exit(p0: AiEntity) = Unit
    override fun onMessage(p0: AiEntity, p1: Telegram?) = false
}