package dev.wesley.shooter.entity.death

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import dev.wesley.shooter.entity.animations.component.AnimationComponent
import dev.wesley.shooter.entity.death.component.DeathComponent
import dev.wesley.shooter.entity.life.component.LifeComponent
import dev.wesley.shooter.event.EntityDeathEvent
import dev.wesley.shooter.event.fire

@AllOf([DeathComponent::class])
class DeathSystem(
    private val deadCmps : ComponentMapper<DeathComponent>,
    private val lifeCmps : ComponentMapper<LifeComponent>,
    private val gameStage : Stage,
    private val animCmps : ComponentMapper<AnimationComponent>,
    ) : IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        val deadCmp = deadCmps[entity]

        if (deadCmp.reviveTime == 0f) {
            gameStage.fire(EntityDeathEvent(animCmps[entity].model))
            world.remove(entity)
            return
        }

        deadCmp.reviveTime -= deltaTime
        if (deadCmp.reviveTime <= 0f) {
            with(lifeCmps[entity]) {
                life = max
            }

            configureEntity(entity) {
                deadCmps.remove(entity)
            }
        }
    }
}