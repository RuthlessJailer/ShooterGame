package dev.wesley.shooter.screens

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.actors.onClick
import ktx.scene2d.actors
import ktx.scene2d.vis.*

/**
 * @author vadim
 */
object UIKt {
	fun createMenu(stage: Stage){
		stage.actors {
			visTable {
				menuBar { cell ->
					cell.top().growX().expandY().row()
					menu("File") {
						menuItem("New Game") {
							setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.N)
							onClick {
								println("new")
							}
						}
						menuItem("Load Game") {
							subMenu {
								menuItem("Open File") {
									setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.O)
									onClick {
										println("file")
									}
								}
								menuItem("Recents"){
									subMenu {
										menuItem("Save1")
										menuItem("Save2")
										menuItem("Save3")
									}
								}
							}
						}
					}
				}
				setFillParent(true)
			}
		}
	}

}