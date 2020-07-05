import akka.actor.ActorRef
import javafx.event.ActionEvent
import javafx.scene.control.{Button, Label}

object Model {
  val button = new Button("Show Time")
  val label = new Label("")
  var frameActor: ActorRef = _

  button.setOnAction((_: ActionEvent) => {
    println("btn pressed")
    Controller.dispatchHi(frameActor)
  })

  def setFrameActor(actorRef: ActorRef): Unit = frameActor = actorRef
}