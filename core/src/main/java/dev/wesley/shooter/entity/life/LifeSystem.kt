package dev.wesley.shooter.entity.life

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.github.quillraven.fleks.*
import dev.wesley.shooter.entity.animations.component.AnimationComponent
import dev.wesley.shooter.entity.animations.enums.AnimationType
import dev.wesley.shooter.entity.death.component.DeathComponent
import dev.wesley.shooter.entity.life.component.LifeComponent
import dev.wesley.shooter.physics.component.PhysicComponent
import dev.wesley.shooter.player.components.PlayerComponent
import dev.wesley.shooter.ui.text.component.FloatingTextComponent
import ktx.assets.disposeSafely

@AllOf([LifeComponent::class])
@NoneOf([DeathComponent::class])
class LifeSystem(
    private val lifeCmps : ComponentMapper<LifeComponent>,
    private val deadCmps : ComponentMapper<DeathComponent>,
    private val playerCmps : ComponentMapper<PlayerComponent>,
    private val physicCmps : ComponentMapper<PhysicComponent>,
    private val aniCmps : ComponentMapper<AnimationComponent>,

    ) : IteratingSystem() {

    private val damageFont = BitmapFont(Gdx.files.internal("damage.fnt"))
    private val floatingTextStyle = LabelStyle(damageFont, Color.WHITE)

    override fun onTickEntity(entity: Entity) {
        val lifeCmp = lifeCmps[entity]
        lifeCmp.life = (lifeCmp.life + lifeCmp.regeneration * deltaTime).coerceAtMost(lifeCmp.max)

        if (lifeCmp.takeDamage > 0f) {
            lifeCmp.life -= lifeCmp.takeDamage
            val physicCmp = physicCmps[entity]
            floatingText(lifeCmp.takeDamage.toInt().toString(), physicCmp.body.position, physicCmp.size)
            lifeCmp.takeDamage = 0f
        }

        if (lifeCmp.isDead) {
            aniCmps.getOrNull(entity)?.let { aniCmp ->
                aniCmp.nextAnimation(AnimationType.DEATH)
                aniCmp.playMode = Animation.PlayMode.NORMAL
            }

            configureEntity(entity) {
                deadCmps.add(it) {
                    if (it in playerCmps) {
                        reviveTime = 7f;
                    }
                }
            }
        }
    }

    override fun onDispose() {
        damageFont.disposeSafely()
    }

    private fun floatingText(text: String, position: Vector2, size: Vector2) {
        world.entity {
            add<FloatingTextComponent> {
                textLocation.set(position.x, position.y - size.y * 0.5f)
                lifeSpan = 1.5f
                label = Label(text, floatingTextStyle)
            }
        }
    }
}