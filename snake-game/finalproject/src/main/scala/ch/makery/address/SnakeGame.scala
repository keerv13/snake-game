package ch.makery.address

import ch.makery.address.view.{GameOverDialogController, MenuController, SnakeGameController}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.AnchorPane
import javafx.{scene => jfxs}
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._
import scalafx.stage.{Modality, Stage, StageStyle}

object SnakeGame extends JFXApp {
  showMenu()
  def showMenu(){
    val menuResource = getClass.getResource("view/Menu.fxml")
    val menuLoader = new FXMLLoader(menuResource, NoDependencyResolver)
    menuLoader.load()

    // Retrieve the root node from Menu.fxml
    val menuRoot: AnchorPane = menuLoader.getRoot[jfxs.layout.AnchorPane]

    // Initialize the stage with the Menu scene
    stage = new PrimaryStage {
      title = "Snake Game"
      scene = new Scene(menuRoot){
        stylesheets += getClass.getResource("view/stylesheet.css").toString
      }

    }
  }

  def showGame(): Unit = {

    try {
      val gameResource = getClass.getResource("view/Game.fxml")
      if (gameResource == null) {
        throw new RuntimeException("Game.fxml not found")
      }

      val gameLoader = new FXMLLoader(gameResource, NoDependencyResolver)
      gameLoader.load() // This should trigger the `initialize()` method.

      val gameRoot: AnchorPane = gameLoader.getRoot[jfxs.layout.AnchorPane]

      //Retrieve the controller from Game.fxml
      val gameController = gameLoader.getController[SnakeGameController#Controller]
      gameController.initialize()
      //gameController.handleExitButtonAction()

      //Update the scene with the new root node from Game.fxml
      stage.scene = new Scene(gameRoot){
        stylesheets += getClass.getResource("view/stylesheet.css").toString
      }
      //println("Game scene set")
    } catch {
      case e: Exception =>
        e.printStackTrace()
        println("FXML loading failed")
    }

  }


  def showGameOverDialog(): Unit = {
    try {
      // Load the GameOverDialog FXML and controller
      val gameResource = getClass.getResource("view/GameOverDialog.fxml")

      val gameLoader = new FXMLLoader(gameResource, NoDependencyResolver)
      gameLoader.load()

      val gameRoot: AnchorPane = gameLoader.getRoot[jfxs.layout.AnchorPane]

      val dialogController = gameLoader.getController[GameOverDialogController#Controller]
      dialogController.handleOk()

      stage.scene = new Scene(gameRoot){
        stylesheets += getClass.getResource("view/stylesheet.css").toString
      }

    } catch {
      case e: Exception =>
        e.printStackTrace()
        println("Failed to load the game over dialog.")
    }
  }
}

