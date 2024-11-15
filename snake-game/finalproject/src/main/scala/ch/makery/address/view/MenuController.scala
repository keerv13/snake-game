package ch.makery.address.view

import ch.makery.address.SnakeGame
import scalafx.scene.control.Button
import scalafxml.core.macros.sfxml

@sfxml
class MenuController(val playButton: Button) {

  //When play button is clicked
  def handlePlayButtonAction(): Unit = {
    SnakeGame.showGame()

  }
}

