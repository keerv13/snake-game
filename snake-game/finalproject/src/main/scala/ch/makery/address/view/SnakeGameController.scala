package ch.makery.address.view

import ch.makery.address.SnakeGame
import ch.makery.address.model.{Direction, Snake}
import scalafxml.core.macros.sfxml
import javafx.scene.layout.AnchorPane
import javafx.fxml.FXML
import scalafx.scene.shape.Circle
import scalafx.scene.control.{Alert, Button, ButtonType, Label}
import javafx.event.EventHandler
import javafx.scene.input.{KeyCode, KeyEvent}
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.stage.Stage
import scalafx.util.Duration

@sfxml
class SnakeGameController(@FXML private var board: AnchorPane,
                          @FXML private var food: Circle,
                          @FXML private var scoreLabel: Label,
                          @FXML private var exitButton: Button) {

  private var score: Int = 0
  private var snake: Snake = _
  private var gameRunning = true
  private var frameCounter: Int = 0  // Frame counter to delay collision checks

  private var timeline: Timeline = _ // Define timeline as a class-level variable
  var dialogStage : Stage = null

  @FXML
  def initialize(): Unit = {
    snake = new Snake(board)
    snake.initializeSnake()

    // Ensure the snake is within bounds after initialization
    if (snake.isOutOfBounds) {
      println("Snake initialized out of bounds! Adjust the initial position.")
      return
    }

    moveFoodToRandomPosition()
    setupKeyEventHandling()
    updateScore()
    startGameLoop()
  }

  def moveFoodToRandomPosition(): Unit = {
    val randomX = math.random() * (board.getPrefWidth - food.getRadius * 2)
    val randomY = math.random() * (board.getPrefHeight - food.getRadius * 2)

    food.setLayoutX(randomX + food.getRadius)
    food.setLayoutY(randomY + food.getRadius)
  }

  def setupKeyEventHandling(): Unit = {
    board.sceneProperty().addListener((_, _, newScene) => {
      if (newScene != null) {
        newScene.setOnKeyPressed(new EventHandler[KeyEvent] {
          override def handle(event: KeyEvent): Unit = {
            if (gameRunning) {
              event.getCode match {
                case KeyCode.UP if snake.direction != Direction.DOWN =>
                  snake.direction = Direction.UP
                case KeyCode.DOWN if snake.direction != Direction.UP =>
                  snake.direction = Direction.DOWN
                case KeyCode.LEFT if snake.direction != Direction.RIGHT =>
                  snake.direction = Direction.LEFT
                case KeyCode.RIGHT if snake.direction != Direction.LEFT =>
                  snake.direction = Direction.RIGHT
                case _ => // Do nothing for other keys
              }
            }
          }
        })
      }
    })
  }

  def startGameLoop(): Unit = {
    //referenced from https://chatgpt.com
    timeline = new Timeline {
      cycleCount = Timeline.Indefinite
      keyFrames = Seq(
        KeyFrame(Duration(100), onFinished = _ => {
          if (gameRunning) {
            snake.move()
            frameCounter += 1  // Increment the frame counter

            // Only check for collisions after the first few frames
            if (frameCounter > 5) {
              checkCollision()
              checkBoardCollision()
            }
          }
        })
      )
    }
    timeline.play()
  }

  def checkCollision(): Unit = {
    if (snake.checkCollision(food)) {
      score += 1
      updateScore()
      moveFoodToRandomPosition()
      snake.addSnakePart()
    }
  }

  def checkBoardCollision(): Unit = {
    if (snake.isOutOfBounds) {
      gameRunning = false
      SnakeGame.showGameOverDialog()
    }
  }

  def updateScore(): Unit = {
    scoreLabel.setText(s"Score: $score")
  }

  @FXML
  def handleExitButtonAction(): Unit = {
    //Pause the game
    gameRunning = false
    timeline.pause()

    // Create the confirmation alert
    val alert = new Alert(Alert.AlertType.Confirmation) {
      initOwner(dialogStage) // Use the current stage as the owner
      title = "Confirm Exit Game"
      headerText = "Please confirm if you would like to exit the game."
      contentText = "Your progress will be lost."
    }

    // Show the dialog and wait for a response
    val result = alert.showAndWait()

    result match {
      case Some(ButtonType.OK) =>
        // User chose OK, exit to the menu
        SnakeGame.showMenu()
      case Some(ButtonType.Cancel) | None =>
        // User chose Cancel or closed the dialog, resume the game
        gameRunning = true
        timeline.play()
      case _ =>
    }
  }
}
