package dev.wesley.shooter.player.items.types

import com.badlogic.gdx.scenes.scene2d.Event
import dev.wesley.shooter.event.ItemEntityAttackEvent
import dev.wesley.shooter.player.items.Item
import dev.wesley.shooter.player.items.ItemContext
import dev.wesley.shooter.player.items.library.ItemLibrary
import java.util.function.Consumer
import kotlin.reflect.KClass
import kotlin.reflect.cast

class Sword() : Item<ItemEntityAttackEvent> {

    override val ctx: ItemContext
        get() = ItemContext("Sword", listOf("Testing Sword!"), ItemEntityAttackEvent::class, eventConsumer = { event ->
            when (event) {
                is ItemEntityAttackEvent -> {
                    event.damage += 100
                }
            }
        })

    init {
        ItemLibrary.itemCtxLibrary.add(ctx)
    }
}
