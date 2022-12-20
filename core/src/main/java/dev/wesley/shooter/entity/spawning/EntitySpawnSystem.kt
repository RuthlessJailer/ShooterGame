package dev.wesley.shooter.entity.spawning

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.utils.Scaling
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import dev.wesley.shooter.ShooterGame.Companion.UNIT_SCALE
import dev.wesley.shooter.actor.FlipImage
import dev.wesley.shooter.entity.animations.component.AnimationComponent
import dev.wesley.shooter.entity.animations.enums.AnimationModel
import dev.wesley.shooter.entity.animations.enums.AnimationType
import dev.wesley.shooter.entity.attacking.component.AttackComponent
import dev.wesley.shooter.entity.life.component.LifeComponent
import dev.wesley.shooter.entity.movement.component.MoveComponent
import dev.wesley.shooter.entity.spawning.component.SpawnComponent
import dev.wesley.shooter.entity.spawning.component.SpawnComponent.Companion.DEFAULT_ATTACK_DAMAGE
import dev.wesley.shooter.entity.spawning.component.SpawnComponent.Companion.DEFAULT_LIFE
import dev.wesley.shooter.entity.spawning.config.SpawnCfg
import dev.wesley.shooter.entity.state.component.StateComponent
import dev.wesley.shooter.event.MapChangeEvent
import dev.wesley.shooter.physics.collision.component.CollisionComponent
import dev.wesley.shooter.physics.component.PhysicComponent.Companion.physicComponentFromImage
import dev.wesley.shooter.player.components.PlayerComponent
import dev.wesley.shooter.rendering.component.ImageComponent
import dev.wesley.shooter.screen.GameScreen.Companion.phWorld
import ktx.app.gdxError
import ktx.box2d.box
import ktx.math.vec2
import ktx.tiled.layer
import ktx.tiled.type
import ktx.tiled.x
import ktx.tiled.y
import kotlin.math.roundToInt

@AllOf([SpawnComponent::class])
class EntitySpawnSystem(private val spawnCmps : ComponentMapper<SpawnComponent>, private val atlas: TextureAtlas) : EventListener, IteratingSystem() {

    private val cachedCfgs = mutableMapOf<String, SpawnCfg>()
    private val cachedSizes = mutableMapOf<AnimationModel, Vector2>()

    override fun onTickEntity(entity: Entity) {

        with(spawnCmps[entity]) {
            val cfg = spawnCfg(type)
            val relativeSize = size(cfg.model)

            world.entity {
                val imgCmp = add<ImageComponent> {
                    image = FlipImage().apply {
                        setPosition(location.x, location.y)
                        setSize(relativeSize.x, relativeSize.y)
                        setScaling(Scaling.fill)
                    }
                }

                add<AnimationComponent> {
                    nextAnimation(cfg.model, AnimationType.IDLE)
                }

                physicComponentFromImage(phWorld, imgCmp.image, cfg.bodyType) {
                        phCmp, width, height ->

                    val w = width * cfg.physicScaling.x
                    val h = height * cfg.physicScaling.y
                    phCmp.offset.set(cfg.physicOffset)
                    phCmp.size.set(w, h)

                    box(w, h, cfg.physicOffset) {
                        isSensor = cfg.bodyType != BodyDef.BodyType.StaticBody
                        userData = HIT_BOX_SENSOR
                    }

                    if (cfg.bodyType != BodyDef.BodyType.StaticBody) {
                        val colH = h * 0.04f
                        val colOffset = vec2().apply {
                            set(cfg.physicOffset)
                        }
                        colOffset.y -= h * 0.0f - colH * 0.05f

                        box(w, colH * 0.4f, colOffset)
                    }
                }

                if (cfg.speedScaling > 0f) {
                    add<MoveComponent>() {
                        speed = SpawnComponent.DEFAULT_SPEED * cfg.speedScaling
                    }
                }

                if (cfg.canAttack) {
                    add<AttackComponent> {
                        maxDelay = cfg.attackDelay
                        damage = (DEFAULT_ATTACK_DAMAGE * cfg.attackScaling).roundToInt()
                        extraRange = cfg.attackExtraRange
                    }
                }

                if (cfg.lifeScaling > 0f) {
                    add<LifeComponent> {
                        max = DEFAULT_LIFE * cfg.lifeScaling
                        life = max
                    }
                }

                if (type == "PLAYER") {
                    add<PlayerComponent>()
                    add<StateComponent>()
                }

                if (cfg.bodyType != BodyDef.BodyType.StaticBody) {
                    add<CollisionComponent>()
                }

            }
        }
        world.remove(entity)
    }

    private fun size(model : AnimationModel) : Vector2 = cachedSizes.getOrPut(model) {
        val regions = atlas.findRegions("${model.atlasKey}/${AnimationType.IDLE.atlasKey}")

        if (regions.isEmpty){
            gdxError("TThere are no regions for the idle animation of model $model")
        }

        val firstFrame = regions.first()
        vec2(firstFrame.originalWidth * UNIT_SCALE, firstFrame.originalHeight * UNIT_SCALE)
    }

    private fun spawnCfg(type : String) : SpawnCfg = cachedCfgs.getOrPut(type) {
        when(type) {
            "PLAYER" -> SpawnCfg(
                AnimationModel.PLAYER,
                attackExtraRange = 0.6f,
                attackScaling = 1.25f,
                physicScaling = vec2(0.32f, 0.5f),
                physicOffset = vec2(0f, -5.4f * UNIT_SCALE),
                bodyType = BodyDef.BodyType.DynamicBody
            )

            "SLIME" -> SpawnCfg(
                AnimationModel.SLIME,
                lifeScaling = 0.75f,
                physicScaling = vec2(0.3f, 0.3f),
                physicOffset = vec2(0f, 2f * UNIT_SCALE),
                bodyType = BodyDef.BodyType.DynamicBody
            )

            "CHEST" -> SpawnCfg(
                AnimationModel.CHEST,
                speedScaling = 0f,
                bodyType = BodyDef.BodyType.StaticBody,
                lifeScaling = 0f,
                lootable = true,
                canAttack = false)

            else -> gdxError("Type $type has no SpawnCfg setup.")
        }
    }

    override fun handle(event: Event?): Boolean {
         when(event) {
             is MapChangeEvent -> {
                 val entityLayer = event.map.layer("entities")

                 entityLayer.objects?.forEach { mapObject ->
                     val type = mapObject.type ?: gdxError("MapObject $mapObject does not have a type!")

                     world.entity {
                         add<SpawnComponent> {
                             this.type = type
                             this.location.set(mapObject.x * UNIT_SCALE, mapObject.y * UNIT_SCALE)
                         }
                     }

                 }
                 return true;
             }

         }

        return false
    }

    companion object {
        const val HIT_BOX_SENSOR = "Hitbox"
    }
}