package dev.wesley.shooter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.quillraven.fleks.World
import dev.wesley.shooter.audio.system.AudioSystem
import dev.wesley.shooter.camera.CameraSystem
import dev.wesley.shooter.debug.DebugSystem
import dev.wesley.shooter.entity.animations.AnimationSystem
import dev.wesley.shooter.entity.attacking.AttackSystem
import dev.wesley.shooter.entity.death.DeathSystem
import dev.wesley.shooter.entity.life.LifeSystem
import dev.wesley.shooter.entity.movement.MoveSystem
import dev.wesley.shooter.entity.spawning.EntitySpawnSystem
import dev.wesley.shooter.entity.state.StateSystem
import dev.wesley.shooter.entity.state.component.StateComponent
import dev.wesley.shooter.event.MapChangeEvent
import dev.wesley.shooter.event.fire
import dev.wesley.shooter.input.PlayerKeyboardInputProcessor
import dev.wesley.shooter.physics.PhysicSystem
import dev.wesley.shooter.physics.collision.system.CollisionDespawnSystem
import dev.wesley.shooter.physics.collision.system.CollisionSpawnSystem
import dev.wesley.shooter.physics.component.PhysicComponent
import dev.wesley.shooter.player.items.handler.ItemHandler
import dev.wesley.shooter.player.items.types.Sword
import dev.wesley.shooter.rendering.RenderSystem
import dev.wesley.shooter.rendering.component.ImageComponent
import dev.wesley.shooter.ui.text.component.FloatingTextComponent
import dev.wesley.shooter.ui.text.system.FloatingTextSystem
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2

class GameScreen : KtxScreen {

    override fun show() {
        log.debug { "GameScreen gets shown!" }

        world.systems.forEach { system ->
            if (system is EventListener) {
                gameStage.addListener(system)
            }
        }

        gameStage.fire(MapChangeEvent(tiledMap))
        PlayerKeyboardInputProcessor(world)
        gameStage.addListener(ItemHandler())

    }

    override fun resize(width: Int, height: Int) {
        gameStage.act()
        gameStage.viewport.update(width, height, true)
        uiStage.act()
        uiStage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        world.update(delta.coerceAtMost(0.25f))
    }

    override fun dispose() {
        gameStage.disposeSafely()
        textureAtlas.disposeSafely()
        world.dispose()
        tiledMap.disposeSafely()
        phWorld.disposeSafely()
        uiStage.disposeSafely()
    }

    companion object {
        private val textureAtlas = TextureAtlas("graphics/game.atlas")
        private var tiledMap : TiledMap = TmxMapLoader().load("maps/testing.tmx")
        private val gameStage : Stage = Stage(ExtendViewport(16f, 9f))
        private val log = logger<GameScreen>()
        private val uiStage : Stage = Stage(ExtendViewport(1280f, 720f))

        val phWorld = createWorld(gravity = vec2()).apply {
            autoClearForces = false
        }

        val world : World = World {
            inject(gameStage)
            inject(textureAtlas)
            inject(phWorld)
            inject("uiStage", uiStage)

            componentListener<ImageComponent.Companion.ImageComponentListener>()
            componentListener<PhysicComponent.PhysicComponentListener>()
            componentListener<FloatingTextComponent.Companion.FloatingTextComponentListener>()
            componentListener<StateComponent.Companion.StateComponentListener>()

            system<EntitySpawnSystem>()
            system<CollisionSpawnSystem>()
            system<CollisionDespawnSystem>()
            system<MoveSystem>()
            system<AttackSystem>()
            system<DeathSystem>()
            system<LifeSystem>()
            system<PhysicSystem>()
            system<AnimationSystem>()
            system<StateSystem>()
            system<CameraSystem>()
            system<FloatingTextSystem>()
            system<RenderSystem>()
            system<AudioSystem>()
            system<DebugSystem>()

            Sword()
        }
    }

}