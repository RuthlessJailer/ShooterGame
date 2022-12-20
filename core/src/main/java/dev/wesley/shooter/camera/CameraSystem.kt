package dev.wesley.shooter.camera

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import dev.wesley.shooter.event.MapChangeEvent
import dev.wesley.shooter.player.components.PlayerComponent
import dev.wesley.shooter.rendering.component.ImageComponent
import ktx.tiled.height
import ktx.tiled.width

@AllOf([PlayerComponent::class, ImageComponent::class])
class CameraSystem
    (private val imageCmps : ComponentMapper<ImageComponent>,
     stage: Stage) : EventListener, IteratingSystem() {

    private var maxW = 0f
    private var maxH = 0f
    private val camera = stage.camera

    override fun onTickEntity(entity: Entity) {
        val viewH = camera.viewportHeight * 0.5f
        val viewW = camera.viewportWidth * 0.5f


        with(imageCmps[entity]) {
            camera.position.set(
                image.x.coerceIn(viewW, maxW - viewW),
                image.y.coerceIn(viewH, maxH - viewH),
                camera.position.z
            )

        }

    }

    override fun handle(event: Event?): Boolean {
        when(event) {
            is MapChangeEvent -> {
                maxW = event.map.width.toFloat()
                maxH = event.map.height.toFloat()
                return true
            }
        }
        return false
    }
}