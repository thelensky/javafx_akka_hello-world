import akka.actor.Actor

class FrameActor() extends Actor {
  override def receive: Receive = {
    case Hi => Controller.setLabel("hello world", Model.label)
  }
}

case class Hi()