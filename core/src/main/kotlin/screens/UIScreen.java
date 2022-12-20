package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.VisUI;
import dev.wesley.shooter.screens.UIKt;
import ktx.app.KtxScreen;
import ktx.scene2d.Scene2DSkin;

/**
 * @author vadim
 */
public class UIScreen implements KtxScreen {

	Stage stage;
	Table table;

	public UIScreen(){
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

//		table = new Table();
//		table.setFillParent(true);
//		stage.addActor(table);
		VisUI.load();
		Scene2DSkin.INSTANCE.setDefaultSkin(VisUI.getSkin());

//		table.setDebug(true);

		UIKt.INSTANCE.createMenu(stage);
	}

	@Override
	public void dispose() {
		VisUI.dispose();
		stage.dispose();
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(deltaTime);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void show() {

	}
}
