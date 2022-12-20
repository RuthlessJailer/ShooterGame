package dev.wesley.shooter.physics.collision.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import dev.wesley.shooter.event.CollisionDespawnEvent
import dev.wesley.shooter.event.fire
import dev.wesley.shooter.physics.component.TiledComponent

@AllOf([TiledComponent::class])
class CollisionDespawnSystem
    (private val tiledCmps : ComponentMapper<TiledComponent>,
     private val stage : Stage) : IteratingSystem() {
    override fun onTickEntity(entity: Entity) {
        with(tiledCmps[entity]) {
            if (nearbyEntities.isEmpty()) {
                stage.fire(CollisionDespawnEvent(cell))
                world.remove(entity)
            }
        }
    }
}