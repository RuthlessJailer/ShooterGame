package main

import com.squins.gdx.backends.bytecoder.BytecoderApplication
import com.squins.gdx.backends.bytecoder.graphics.BytecoderGL20
import com.squins.gdx.backends.bytecoder.api.web.HTMLDivElement
import com.squins.gdx.backends.bytecoder.api.web.LibgdxAppCanvas
import com.squins.gdx.backends.bytecoder.preloader.Preloader
import com.squins.gdx.backends.bytecoder.preloader.PreloaderBundleGenerator
import com.squins.gdx.backends.bytecoder.preloader.PreloaderCallback
import com.squins.gdx.backends.bytecoder.preloader.PreloaderState
import de.mirkosertic.bytecoder.api.web.HTMLDocument
import de.mirkosertic.bytecoder.api.web.Window
import dev.wesley.shooter.ShooterGame
import main.examples.webgl.LibGDXBytecoderGL20SampleWebGlShaders
import java.lang.invoke.MethodHandles
import java.util.jar.JarFile
import java.util.zip.ZipFile

class Main {

    private val window: Window
    private val document: HTMLDocument
    val scale: Float
    private val app: HTMLDivElement
    private val libgdxAppCanvas: LibgdxAppCanvas

    // TODO: move this to external class, only used when running runSimpleGlExampleNoLibgdx

    init {
        println("assign window")
        window = Window.window()!! as Window
        println("assign document")
        document = window.document()
        println("assign scale")
        scale = window.devicePixelRatio()
        println("assign app")
        app = (document.getElementById("app") as HTMLDivElement)
        println("assign libgdxAppCanvas")
        libgdxAppCanvas = document.querySelector("#canvas1") as LibgdxAppCanvas
        println("app.style")
        app.style("float:left; width:100%; height:100%;")
    }

    private fun thingsToKeep() {
        val preloaderBundleGenerator:PreloaderBundleGenerator? = null
    }

    private fun runLibGdxExample() {
        println("runLibGdxExample()")
        BytecoderApplication(ShooterGame(), libgdxAppCanvas)
    }

    private fun justPreload() {
        println("justPreload()")
        val baseUrl = libgdxAppCanvas.assetBaseUrl()
        println("assetBaseUrl: $baseUrl")

        println("creating preloader")
        val preloader = Preloader(baseUrl)

        val assetFileUrl = "$baseUrl/assets.txt"

        println("calling preloader.preload()")
        preloader.preload(assetFileUrl, object : PreloaderCallback {
            override fun update(state: PreloaderState) {
                println("update not implemented")
            }

            override fun error(file: String) {
                println("error not implemented")
            }

        })
    }

    private fun runSimpleGlExampleSimpleApp(){
        println("runSimpleGlExampleNoLibgdx()")
        val gl = libgdxAppCanvas.getContext("webgl")
//        BytecoderSampleWebGlShaders(app, libgdxAppCanvas, gl).run()
//        BytecoderSampleAudio(libgdxAppCanvas).run()
        LibGDXBytecoderGL20SampleWebGlShaders(app, libgdxAppCanvas, BytecoderGL20(gl)).run()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>?) {
            println("Kotlin entry point")

            Main().runLibGdxExample()
        }
    }
}

