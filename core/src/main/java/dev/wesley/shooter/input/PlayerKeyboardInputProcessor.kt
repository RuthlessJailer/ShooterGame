package dev.wesley.shooter.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.World
import dev.wesley.shooter.entity.attacking.component.AttackComponent
import dev.wesley.shooter.entity.movement.component.MoveComponent
import dev.wesley.shooter.player.components.PlayerComponent
import ktx.app.KtxInputAdapter

class PlayerKeyboardInputProcessor(
    private val world : World,
    private val moveCmps : ComponentMapper<MoveComponent> = world.mapper(),
    private val attackCmps : ComponentMapper<AttackComponent> = world.mapper(),
) : KtxInputAdapter {

    private var playerSine = 0f
    private var playerCosine = 0f
    private val playerEntities = world.family(allOf = arrayOf((PlayerComponent::class)))
    private val pressedKeys = mutableSetOf<Int>()

    init {
        Gdx.input.inputProcessor = this
    }

    private fun Int.isMovementKey(): Boolean {
        return this == W
                || this == A || this == S || this == D
    }

    private fun isPressed(keycode: Int): Boolean = keycode in pressedKeys

    private fun updatePlayerMovement() {
        playerEntities.forEach {
            player ->
                with(moveCmps[player]) {
                    cos = playerCosine
                    sine = playerSine
                }

        }
    }

    override fun keyDown(keycode: Int): Boolean {
        pressedKeys += keycode
        if (keycode.isMovementKey()) {

            when (keycode) {
                W -> {
                    playerSine = 1f
                }
                S -> {
                    playerSine = -1f
                }
                D -> {
                    playerCosine = 1f
                }
                A -> {
                    playerCosine = -1f
                }
            }
            updatePlayerMovement()
            return true
        }
        return false
    }


    override fun keyUp(keycode: Int): Boolean {
        pressedKeys -= keycode
        println(toString(keycode) + "Cos: $playerCosine" + "Sine: $playerSine")
            if (keycode.isMovementKey()) {
            when (keycode) {
                W -> playerSine = if (isPressed(W)) -1f else 0f
                S -> playerSine = if (isPressed(S)) 1f else 0f
                D -> playerCosine = if (isPressed(D)) -1f else 0f
                A -> playerCosine = if (isPressed(A)) 1f else 0f
            }
            updatePlayerMovement()
            return true
        } else if (keycode == SPACE) {
            playerEntities.forEach {
                with(attackCmps[it]) {
                    doAttack = true
                }
            }
            return true;
        }
        return false
    }
}