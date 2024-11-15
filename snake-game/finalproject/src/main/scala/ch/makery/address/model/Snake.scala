package ch.makery.address.model

import javafx.scene.layout.AnchorPane
import scalafx.scene.shape.{Rectangle, Shape}
import scalafx.scene.paint.Color
import scala.collection.mutable.ListBuffer
import ch.makery.address.model.Direction.Direction

class Snake(private val board: AnchorPane) {
  private val snakeParts = ListBuffer[Rectangle]()
  private val movementStep = 10
  var direction: Direction = Direction.RIGHT

  def initializeSnake(): Unit = {
    for (i <- 0 until 3) {
      val part = createSnakePart(200 - (i * 40), 200)
      snakeParts += part
      board.getChildren.add(part)
    }
  }

  private def createSnakePart(x: Double, y: Double): Rectangle = {
    new Rectangle {
      width = 40
      height = 40
      fill = Color.web("#1fff55")
      stroke = Color.Black
      strokeWidth = 1
      layoutX = x.toDouble
      layoutY = y.toDouble
    }
  }

  def move(): Unit = {
    var previousX = snakeParts.head.layoutX.value  // Use .value to get Double
    var previousY = snakeParts.head.layoutY.value  // Use .value to get Double

    direction match {
      case Direction.UP =>
        snakeParts.head.layoutY.value = previousY - movementStep
      case Direction.DOWN =>
        snakeParts.head.layoutY.value = previousY + movementStep
      case Direction.LEFT =>
        snakeParts.head.layoutX.value = previousX - movementStep
      case Direction.RIGHT =>
        snakeParts.head.layoutX.value = previousX + movementStep
    }

    //Move snake body
    for (i <- 1 until snakeParts.length) {
      val part = snakeParts(i)
      val tempX = part.layoutX.value
      val tempY = part.layoutY.value
      part.layoutX.value = previousX
      part.layoutY.value = previousY
      previousX = tempX
      previousY = tempY
    }
  }

  def checkCollision(food: Shape): Boolean = {
    snakeParts.head.getBoundsInParent.intersects(food.getBoundsInParent)
  }

  def isOutOfBounds: Boolean = {
    val head = snakeParts.head
    val headX = head.layoutX()
    val headY = head.layoutY()

    headX < 0 || headX + head.width() > board.getPrefWidth ||
      headY < 0 || headY + head.height() > board.getPrefHeight
  }


  def addSnakePart(): Unit = {
    val lastPart = snakeParts.last

    val newPart = new Rectangle {
      width = snakeParts.head.width()
      height = snakeParts.head.height()
      fill = Color.web("#1fff55")
      stroke = Color.Black
      strokeWidth = 1
      layoutX = lastPart.layoutX.value
      layoutY = lastPart.layoutY.value
    }

    snakeParts += newPart
    board.getChildren.add(newPart)
  }
}


