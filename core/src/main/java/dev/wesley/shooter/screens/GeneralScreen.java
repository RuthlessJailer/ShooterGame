package dev.wesley.shooter.screens;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.quillraven.fleks.World;
import ktx.assets.DisposablesKt;
import ktx.log.Logger;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import ktx.app.KtxScreen;
import lombok.val;

import java.util.Arrays;
import java.util.EventListener;

public class GeneralScreen implements KtxScreen {

    private final Logger logger = new Logger("Main", "[DEBUG]", "[INFO]", "[ERROR]");
    private final TextureAtlas textureAtlas = new TextureAtlas("graphics/game.atlas");
    private final TiledMap tiledMap = new TmxMapLoader().load("maps/testing.tmx");
    private final Stage gameStage = new Stage(new ExtendViewport(16f, 9f));
    private final com.badlogic.gdx.physics.box2d.World phWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(), false);
    private final Stage uiStage = new Stage(new ExtendViewport(1280f, 720f));

    private final World world = new World(cfg -> {
        cfg.inject("gameStage", gameStage);
        cfg.inject("uiStage", uiStage);
        cfg.inject("physicsWorld", phWorld);
        return null;
    });

    public GeneralScreen() {
        phWorld.setAutoClearForces(false);
    }

    @Override
    public void dispose() {
        DisposablesKt.disposeSafely(gameStage);
        DisposablesKt.disposeSafely(textureAtlas);
        DisposablesKt.disposeSafely(tiledMap);
        DisposablesKt.disposeSafely(phWorld);
        DisposablesKt.disposeSafely(uiStage);
    }

    @Override
    public void render(float v) {
        val deltaTime = Math.min(world.getDeltaTime(), 0.25f);
        world.update(deltaTime);
    }

    @Override
    public void resize(int i, int i1) {
        gameStage.act();
        gameStage.getViewport().update(i, i1, true);

        uiStage.act();
        uiStage.getViewport().update(i, i1, true);
    }

    @Override
    public void resume() {}

    @Override
    public void show() {
        logger.debug(() -> {
            return "General Screen is being shown!";
        });

        Arrays.stream(world.getSystems()).forEach(intervalSystem -> {
            if (intervalSystem instanceof EventListener) {
                gameStage.addListener((com.badlogic.gdx.scenes.scene2d.EventListener) intervalSystem);
            }
        });

    }

    @Override
    public void hide() {}
    @Override
    public void pause() {}
}
