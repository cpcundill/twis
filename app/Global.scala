import actors.{TweetReader, TweetCurator}
import akka.actor.Props
import play.api.libs.concurrent.Akka
import play.Logger
import scala.concurrent.duration._
import play.api.{Application, GlobalSettings}
import play.api.Play.current
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    super.onStart(app)
    val tweetCurator = Akka.system.actorOf(Props[TweetCurator], "tweetCurator")
    Akka.system.scheduler.schedule(0.seconds, 5.minutes, tweetCurator, "#Scala OR #Akka OR #Scalaz exclude:retweets")
    Akka.system.actorOf(Props[TweetReader], "tweetReader")
    Logger.info("Finished onStart()")
  }
}
