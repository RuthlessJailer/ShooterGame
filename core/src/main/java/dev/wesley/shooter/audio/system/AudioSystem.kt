package dev.wesley.shooter.audio.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.github.quillraven.fleks.IntervalSystem
import dev.wesley.shooter.audio.objects.AudioHandler.queueSound
import dev.wesley.shooter.audio.objects.AudioHandler.soundCache
import dev.wesley.shooter.audio.objects.AudioHandler.soundRequests
import dev.wesley.shooter.event.EntityAttackEvent
import dev.wesley.shooter.event.EntityDeathEvent
import dev.wesley.shooter.event.MapChangeEvent
import ktx.assets.disposeSafely
import ktx.log.logger
import ktx.tiled.propertyOrNull

class AudioSystem : EventListener, IntervalSystem() {

    private val musicCache = mutableMapOf<String, Music>()

    override fun onTick() {
        if (soundRequests.isEmpty()) {
            return
        }

        soundRequests.values.forEach {
            it.play(1f)
        }

        soundRequests.clear()
    }

    override fun handle(event: Event?): Boolean {
        when (event) {
            is EntityDeathEvent -> queueSound("audio/${event.model.atlasKey}_death.wav")
            is EntityAttackEvent -> queueSound("audio/${event.model.atlasKey}_attack.wav")


            is MapChangeEvent -> {
                event.map.propertyOrNull<String>("music")?.let { path ->
                    log.debug {
                        "Changing Music to: $path"
                    }

                    val music = musicCache.getOrPut(path) {
                        Gdx.audio.newMusic(Gdx.files.internal(path)).apply {
                            isLooping = true
                            volume = 0.2f
                        }
                    }
                    music.play()

                }
                return true
            }
        }
        return false
    }

    override fun onDispose() {
        musicCache.values.forEach {
            it.disposeSafely()
        }

        soundCache.values.forEach {
            it.disposeSafely()
        }
    }

    companion object {
        private val log = logger<AudioSystem>()
    }
}