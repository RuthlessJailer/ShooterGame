package dev.wesley.shooter.rendering

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.*
import com.github.quillraven.fleks.collection.compareEntity
import dev.wesley.shooter.ShooterGame.Companion.UNIT_SCALE
import dev.wesley.shooter.event.MapChangeEvent
import dev.wesley.shooter.rendering.component.ImageComponent
import ktx.assets.disposeSafely
import ktx.graphics.use
import ktx.tiled.forEachLayer

@AllOf([ImageComponent::class])
class RenderSystem(
    private val gameStage: Stage,
    @Qualifier("uiStage") private val uiStage : Stage,
    private val imageCmps : ComponentMapper<ImageComponent>) : EventListener, IteratingSystem(
    comparator = compareEntity { e1, e2 -> imageCmps[e1].compareTo(imageCmps[e2]) })
{

    private val bgdLayers = mutableListOf<TiledMapTileLayer>()
    private val mapRenderer = OrthogonalTiledMapRenderer(TiledMap(), UNIT_SCALE, gameStage.batch)
    private val gameCamera = gameStage.camera as OrthographicCamera

    override fun onTick() {
        super.onTick()

        with(gameStage) {
            viewport.apply()

            AnimatedTiledMapTile.updateAnimationBaseTime()
            mapRenderer.setView(gameCamera)
            mapRenderer.render()

            if (bgdLayers.isNotEmpty()) {
                gameStage.batch.use(gameCamera.combined) { bgdLayers.forEach { mapRenderer.renderTileLayer(it) } }
            }

            act(deltaTime)
            draw()

            if (fgdLayers.isNotEmpty()) {
                gameStage.batch.use(gameCamera.combined) { fgdLayers.forEach { mapRenderer.renderTileLayer(it) } }
            }
        }
        with (uiStage) {
            viewport.apply()
            act(deltaTime)
            draw()
        }
    }



    override fun onTickEntity(entity: Entity) {
        imageCmps[entity].image.toFront()
    }

    override fun handle(event: Event): Boolean {
        when(event) {
            is MapChangeEvent -> {
                bgdLayers.clear()
                fgdLayers.clear()

                event.map.forEachLayer<TiledMapTileLayer> {  layer ->
                    if (layer.name.startsWith("fgd_")) {
                        fgdLayers.add(layer)
                    } else {
                        bgdLayers.add(layer)
                    }
                }
                return true;
            }
        }
        return false;
    }

    override fun onDispose() {
        mapRenderer.disposeSafely()
    }

    companion object {
        val fgdLayers = mutableListOf<TiledMapTileLayer>()
    }
}