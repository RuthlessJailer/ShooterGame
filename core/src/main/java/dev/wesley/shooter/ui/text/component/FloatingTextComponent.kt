package dev.wesley.shooter.ui.text.component

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier
import ktx.actors.plusAssign
import ktx.math.vec2

class FloatingTextComponent {
    val textLocation = vec2()
    var lifeSpan = 0f
    var time = 0f
    val textTarget = vec2()
    val xIncrementation = MathUtils.random(-1.5f, 1.5f)
    val yIncrementation = 1f
    lateinit var label : Label

    companion object {
        class FloatingTextComponentListener(
            @Qualifier("uiStage") private val uiStage : Stage,
        ) : ComponentListener<FloatingTextComponent> {

            override fun onComponentAdded(entity: Entity, component: FloatingTextComponent) {
                uiStage.addActor(component.label)
                component.label += fadeOut(component.lifeSpan, Interpolation.pow3OutInverse)

                component.textTarget.set(
                    component.textLocation.x + component.xIncrementation,
                    component.textTarget.y + component.yIncrementation
                )

            }

            override fun onComponentRemoved(entity: Entity, component: FloatingTextComponent) {
                uiStage.root.removeActor(component.label)
            }
        }
    }
}