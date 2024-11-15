package ch.makery.address.view

import ch.makery.address.SnakeGame
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.image.ImageView
import javafx.stage.{Stage, StageStyle}
import scalafx.application.Platform
import scalafxml.core.macros.sfxml

@sfxml
class GameOverDialogController(@FXML private var okButton: Button,
                               @FXML private var messageLabel: Label,
                               @FXML private var gameOverImageView: ImageView) {

  @FXML
  def handleOk(): Unit = {
    // Close the dialog and show the menu
    SnakeGame.showMenu()
  }
}



