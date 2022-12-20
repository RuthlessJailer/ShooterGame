package dev.wesley.shooter.player.items

import com.badlogic.gdx.scenes.scene2d.Event
import java.util.function.Consumer
import kotlin.reflect.KClass

data class ItemContext(val name : String, val description : List<String>, val event : KClass<*>, val eventConsumer : Consumer<Event>)

interface Item<E : Event> {
    val ctx : ItemContext

}