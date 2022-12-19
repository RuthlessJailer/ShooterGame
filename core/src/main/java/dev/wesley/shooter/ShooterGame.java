package dev.wesley.shooter;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dev.wesley.shooter.screens.UIScreen;
import ktx.app.KtxGame;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ShooterGame extends KtxGame<Screen> {
	private SpriteBatch batch;
	private Texture image;

	@Override
	public void create() {
		batch = new SpriteBatch();
		image = new Texture("libgdx.png");
		addScreen(UIScreen.class, new UIScreen());
		setScreen(UIScreen.class);
		super.create();
	}

	@Override
	public void render() {
		super.render();
//		Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(image, 140, 210);
//		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		image.dispose();
		super.dispose();
	}
}