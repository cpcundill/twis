import actors.{TweetModerator, TweetReader, TweetCurator}
import akka.actor.Props
import com.typesafe.config.ConfigFactory
import play.api.libs.concurrent.Akka
import play.Logger
import scala.concurrent.duration._
import play.api.{Application, GlobalSettings}
import play.api.Play.current
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import scala.collection.JavaConversions._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    super.onStart(app)

    val tweetCurator = Akka.system.actorOf(Props[TweetCurator], "tweetCurator")
    Akka.system.scheduler.schedule(0 seconds, 5 minutes, tweetCurator, "#Scala OR #Akka OR #Scalaz OR #Playframework exclude:retweets")

    val lowQualityUrls = ConfigFactory.load().getStringList("tweet.moderation.low-quality-urls").toSet
    Akka.system.actorOf(Props(classOf[TweetModerator], lowQualityUrls), "tweetModerator")

    Akka.system.actorOf(Props[TweetReader], "tweetReader")

    Logger.info("Finished onStart()")
  }
}
