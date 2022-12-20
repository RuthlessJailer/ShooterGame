package dev.wesley.shooter.entity.attacking

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import dev.wesley.shooter.entity.animations.component.AnimationComponent
import dev.wesley.shooter.entity.attacking.component.AttackComponent
import dev.wesley.shooter.entity.attacking.enums.AttackState
import dev.wesley.shooter.entity.life.component.LifeComponent
import dev.wesley.shooter.entity.spawning.EntitySpawnSystem.Companion.HIT_BOX_SENSOR
import dev.wesley.shooter.event.EntityAttackEvent
import dev.wesley.shooter.event.ItemEntityAttackEvent
import dev.wesley.shooter.event.fire
import dev.wesley.shooter.physics.component.PhysicComponent
import dev.wesley.shooter.physics.entity
import dev.wesley.shooter.player.components.PlayerComponent
import dev.wesley.shooter.rendering.component.ImageComponent
import ktx.box2d.query
import ktx.math.component1
import ktx.math.component2

@AllOf([AttackComponent::class, PhysicComponent::class, ImageComponent::class])
class AttackSystem(
    private val attackCmps : ComponentMapper<AttackComponent>,
    private val physicCmps : ComponentMapper<PhysicComponent>,
    private val imageCmps : ComponentMapper<ImageComponent>,
    private val lifeCmps : ComponentMapper<LifeComponent>,
    private val playerCmps : ComponentMapper<PlayerComponent>,
    private val animCmps : ComponentMapper<AnimationComponent>,
    private val phWorld : World,
    private val gameStage : Stage,
) : IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        val attackCmp = attackCmps[entity]

        if (attackCmp.isReady && !attackCmp.doAttack) {
            return
        }

        if (attackCmp.isPrepared && attackCmp.doAttack) {
            attackCmp.doAttack = false
            attackCmp.state = AttackState.ATTACKING
            attackCmp.delay = attackCmp.maxDelay
            gameStage.fire(EntityAttackEvent(entity, animCmps[entity].model))
            return
        }

        attackCmp.delay -= deltaTime

        if (attackCmp.delay <= 0f && attackCmp.isAttacking) {
            var usableDamage = attackCmp.damage
            val event = ItemEntityAttackEvent(entity, attackCmp.damage)
            gameStage.fire(event)

            if (event.isHandled) {
                usableDamage = event.damage
            }

            attackCmp.state = AttackState.DEAL_DAMAGE

            val image = imageCmps[entity].image
            val physicCmp = physicCmps[entity]
            val attackLeft = image.flipX
            val (x, y) = physicCmp.body.position
            val (offX, offY) = physicCmp.offset
            val (w, h) = physicCmp.size
            val halfW = w * 0.5f
            val halfH = h * 0.5f

            if (attackLeft) {
                AABB_RECT.set(
                    x + offX - halfW - attackCmp.extraRange,
                    y + offY - halfH,
                    x + offX + halfW,
                    y + offY + halfH
                )
            } else {
                AABB_RECT.set(
                    x + offX - halfW,
                    y + offY - halfW,
                    x + offX + halfW + attackCmp.extraRange,
                    y + offY + halfH
                )
            }

            phWorld.query(AABB_RECT.x, AABB_RECT.y, AABB_RECT.width, AABB_RECT.height) { fixture ->
                if (fixture.userData != HIT_BOX_SENSOR) {
                    return@query true
                }

                val fixtureEntity = fixture.entity

                if (fixtureEntity == entity) {
                    return@query true
                }

                configureEntity(fixtureEntity) {
                    lifeCmps.getOrNull(it)?.let { lifeCmp ->
                        lifeCmp.takeDamage += usableDamage * MathUtils.random(0.9f, 1.2f)
                    }
                }
                return@query true
            }
        }


        val isDone = animCmps.getOrNull(entity)?.isAnimationDone ?: true

        if (isDone) {
            attackCmp.state = AttackState.READY
        }

    }
    companion object {
        val AABB_RECT = Rectangle()
    }
}