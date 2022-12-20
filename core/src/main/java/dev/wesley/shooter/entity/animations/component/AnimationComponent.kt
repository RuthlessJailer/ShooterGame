package dev.wesley.shooter.entity.animations.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import dev.wesley.shooter.entity.animations.enums.AnimationModel
import dev.wesley.shooter.entity.animations.enums.AnimationType

data class AnimationComponent(
    var model : AnimationModel = AnimationModel.UNDEFINED,
    var stateTime : Float = 0f,
    var playMode : Animation.PlayMode = Animation.PlayMode.LOOP) {

    lateinit var animation : Animation<TextureRegionDrawable>
    var nextAnimation : String = NO_ANIMATION

    fun nextAnimation(model : AnimationModel, type : AnimationType) {
        this.model = model;
        this.nextAnimation = "${model.atlasKey}/${type.atlasKey}"
    }

    fun nextAnimation(type : AnimationType) {
        this.nextAnimation = "${model.atlasKey}/${type.atlasKey}"
    }

    val isAnimationDone : Boolean
        get() = animation.isAnimationFinished(stateTime)

    companion object {
        const val NO_ANIMATION = ""
    }
}