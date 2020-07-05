import akka.actor.ActorRef
import javafx.application.Platform
import javafx.scene.control.Label

object Controller {
  def dispatchHi(actor: ActorRef): Unit = {
    println(actor)
    actor ! Hi
  }

  def setLabel(str: String, label: Label): Unit = Platform.runLater(() => label.setText(str))
}
