package dev.wesley.shooter.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import dev.wesley.shooter.entity.animations.enums.AnimationModel

fun Stage.fire(event : Event) {
    this.root.fire(event)
}
data class MapChangeEvent(val map : TiledMap) : Event()
data class CollisionDespawnEvent(val cell : Cell) : Event()
data class EntityAttackEvent(val entity : Entity, val model : AnimationModel) : Event()
data class EntityLootEvent(val model : AnimationModel) : Event()
data class EntityDeathEvent(val model : AnimationModel) : Event()

data class ItemEntityAttackEvent(val entity : Entity, var damage : Int) : Event()