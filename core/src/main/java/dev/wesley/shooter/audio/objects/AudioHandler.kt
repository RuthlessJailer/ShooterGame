package dev.wesley.shooter.audio.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound


object AudioHandler {
    val soundCache = mutableMapOf<String, Sound>()
    val soundRequests = mutableMapOf<String, Sound>()

    fun queueSound(soundPath : String) {
        if (soundPath in soundRequests) {
            return
        }

        val sound = soundCache.getOrPut(soundPath) {
            Gdx.audio.newSound(Gdx.files.internal(soundPath))
        }

        soundRequests[soundPath] = sound
    }
}
