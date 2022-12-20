package dev.wesley.shooter.player.items.handler

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import dev.wesley.shooter.player.items.library.ItemLibrary

class ItemHandler : EventListener {
    override fun handle(event: Event?): Boolean {
        if (event == null) return false
            ItemLibrary.itemCtxLibrary.forEach { ctx ->
            if (ctx.event == event::class) {
                ctx.eventConsumer.accept(event)
                return true
            }
        }
        return false
    }
}