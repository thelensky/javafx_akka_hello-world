import java.util
import java.util.Collections
import java.util.concurrent.{AbstractExecutorService, ExecutorService, ThreadFactory, TimeUnit}

import akka.actor.{ActorSystem, Props}
import akka.dispatch.{DispatcherPrerequisites, ExecutorServiceConfigurator, ExecutorServiceFactory}
import com.typesafe.config.Config
import javafx.application.Application.launch
import javafx.application.{Application, Platform}
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.FlowPane
import javafx.stage.Stage


// Multithreading stuff:start
// First we wrap invokeLater as an ExecutorService
object JavaFxExecutorService extends AbstractExecutorService {
  def execute(command: Runnable): Unit = Platform.runLater(command)

  def shutdown(): Unit = ()

  def shutdownNow(): util.List[Runnable] = Collections.emptyList[Runnable]

  def isShutdown = false

  def isTerminated = false

  def awaitTermination(l: Long, timeUnit: TimeUnit) = true
}

// Then we create an ExecutorServiceConfigurator so that Akka can use our JavaFxExecutorService for the dispatchers
class JavaFxThreadExecutorServiceConfigurator(config: Config, prerequisites: DispatcherPrerequisites) extends ExecutorServiceConfigurator(config, prerequisites) {
  private val f = new ExecutorServiceFactory {
    def createExecutorService: ExecutorService = JavaFxExecutorService
  }

  def createExecutorServiceFactory(id: String, threadFactory: ThreadFactory): ExecutorServiceFactory = f
}

// Multithreading stuff:end

class MainApp extends Application {

  def start(stage: Stage): Unit = {

    val root: FlowPane = new FlowPane
    root.setPadding(new Insets(10))
    root.setHgap(10)
    root.setVgap(10)

    root.getChildren.addAll(Model.button, Model.label)

    stage.setTitle("Java Button")

    val scene: Scene = new Scene(root, 350, 150)
    stage.setScene(scene)
    stage.show()
  }
}

object MainApp {
  def main(args: Array[String]): Unit = {
    val props = Props[FrameActor]
    props.withDispatcher("javafx-dispatcher")
    val frameActor = ActorSystem().actorOf(props, "frame-actor")
    Model.setFrameActor(frameActor)
    launch(classOf[MainApp], args: _*)

  }
}