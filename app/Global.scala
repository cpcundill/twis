import actors.{TweetModerator, TweetReader, TweetCurator}
import akka.actor.Props
import controllers.{Tweets, Wordpress}
import play.api.libs.concurrent.Akka
import play.Logger
import scala.collection.mutable
import scala.concurrent.duration._
import play.api.{Application, GlobalSettings}
import play.api.Play.current
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

object Global extends GlobalSettings {

  private val controllers = mutable.Map.empty[Class[T] forSome { type T}, Any]

  override def onStart(app: Application) {
    super.onStart(app)

    val tweetModerator = Akka.system.actorOf(Props[TweetModerator], "tweetModerator")

    val tweetCurator = Akka.system.actorOf(Props(classOf[TweetCurator], tweetModerator), "tweetCurator")
    Akka.system.scheduler.schedule(0 seconds, 30 minutes, tweetCurator, "#Scala OR #Akka OR #Scalaz OR #Playframework exclude:retweets")

    val tweetReader = Akka.system.actorOf(Props[TweetReader], "tweetReader")

    controllers put (classOf[Tweets], new Tweets(tweetReader, tweetModerator))
    controllers put (classOf[Wordpress], new Wordpress(tweetReader))

    Logger.info("Finished onStart()")
  }

  override def getControllerInstance[A](controllerClass: Class[A]): A =
     controllers.getOrElse(controllerClass, super.getControllerInstance(controllerClass)).asInstanceOf[A]

}
