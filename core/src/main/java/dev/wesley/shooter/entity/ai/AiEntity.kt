package dev.wesley.shooter.entity.ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import dev.wesley.shooter.entity.ai.enums.DefaultGlobalState
import dev.wesley.shooter.entity.ai.interfaces.EntityState
import dev.wesley.shooter.entity.animations.component.AnimationComponent
import dev.wesley.shooter.entity.animations.enums.AnimationType
import dev.wesley.shooter.entity.attacking.component.AttackComponent
import dev.wesley.shooter.entity.life.component.LifeComponent
import dev.wesley.shooter.entity.movement.component.MoveComponent
import dev.wesley.shooter.entity.state.component.StateComponent

data class AiEntity(
    private val entity : Entity,
    private val world : World,
    private val attackCmps : ComponentMapper<AttackComponent> = world.mapper(),
    private val moveCmps : ComponentMapper<MoveComponent> = world.mapper(),
    private val aniCmps : ComponentMapper<AnimationComponent> = world.mapper(),
    private val stateCmps : ComponentMapper<StateComponent> = world.mapper(),
    private val lifeCmps : ComponentMapper<LifeComponent> = world.mapper()

) {

    val isAnimationDone: Boolean
        get() = aniCmps[entity].isAnimationDone

    val isDead : Boolean
        get() = lifeCmps[entity].isDead

    val wantsToRun : Boolean
        get() {
            val moveCmp = moveCmps[entity]
            return moveCmp.cos != 0f || moveCmp.sine != 0f
        }

    val attackCmp : AttackComponent
        get() = attackCmps[entity]

    val wantsToAttack : Boolean
        get() = attackCmps.getOrNull(entity)?.doAttack ?: false


    fun state(next : EntityState, immediateChange : Boolean = false) {
        with (stateCmps[entity]) {
            nextState = next

            if (immediateChange) {
                stateMachine.changeState(nextState)
            }
        }
    }

    fun animation(type: AnimationType, mode : PlayMode = PlayMode.LOOP, resetAnimation : Boolean = false) {
        with (aniCmps[entity]) {
            nextAnimation(type)
            playMode = mode

            if (resetAnimation) {
                stateTime = 0f
            }
        }
    }

    fun root(enable : Boolean) {
        with(moveCmps[entity]) {
            root = enable
        }
    }
    fun startAttack() {
        with (attackCmps[entity]) {
            startAttack()
        }
    }

    fun changeToPreviousState() {
        with (stateCmps[entity]) {
            nextState = stateMachine.previousState
        }
    }

    fun enableGlobalState(enable : Boolean) {
        with (stateCmps[entity]) {
            if (enable) {
                stateMachine.globalState = DefaultGlobalState.CHECK_ALIVE
            } else {
                stateMachine.globalState = null
            }
        }
    }
}