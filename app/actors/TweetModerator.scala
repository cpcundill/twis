package actors

import akka.actor.Actor
import core.{LanguageDetectionClient, TwitterClient, TweetOperations}
import twitter4j.Status
import actors.messsages.{RemoderateTweets, ModerateTweet}
import play.api.libs.ws.WS
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import scala.collection.JavaConversions._
import scala.util.Try
import com.typesafe.config.ConfigFactory

trait TweetModerationOps {

  private val langDetectionClient = new LanguageDetectionClient
  private lazy val lowQualityDomains = ConfigFactory.load().getStringList("tweet.moderation.low-quality-domains").toSet

  def withCalculatedRank[T](s: Status)(f: Double => T): Future[T] = {
    var rank = 1.0
    rank = rank - retweetPenalty(s)
    val futurePenalty = for {
      a <- urlPenalty(s)
      b <- languagePenalty(s)
    } yield (a + b)
    futurePenalty.map { penalty =>
      val newRank = (rank - penalty).max(0)
      f(newRank)
    }
  }

  def retweetPenalty(s: Status) = {
    if (s.getRetweetCount == 0) 0.3
    else if (s.getRetweetCount < 5) 0.2
    else if (s.getRetweetCount < 10) 0.1
    else 0.0
  }

  def urlPenalty(s: Status): Future[Double] = {
    val urls = s.getURLEntities.map(_.getExpandedURL).toList
    if (urls.isEmpty) Future(1.0)
    else
      Future.traverse(urls) { url =>
        WS.url(url).withFollowRedirects(true).get().map { r =>
          if (r.status == 200) {
            val ultimateUrl = r.getAHCResponse.getUri.toURL.toString
            if (lowQualityDomains.exists(lqu => ultimateUrl.contains(lqu)))
              1.0
            else
              0.0
          } else 0.3
        } recover {
          case _ => 1.0
        }
      }.map(_.min)
  }

  def languagePenalty(s: Status): Future[Double] = {
    langDetectionClient.detect(s.getText).map { response =>
      if (response.language == "en") 0.0
      else 1.0
    }
  }

}

class TweetModerator extends Actor with TweetModerationOps with TweetOperations {

  private val client = new TwitterClient

  def receive = {

    case ModerateTweet(s, t) =>
      withCalculatedRank(s){ rank: Double =>
        logger.debug(s"Awarding Tweet ${t.id} rank score of $rank")
        update(t.copy(rank = rank))
      }

    case RemoderateTweets(from, to) =>
      logger.info("Remoderation started")
      find(from, to).foreach { tweet =>
        Try(client.read(tweet.id)).map { status =>
          self ! ModerateTweet(status, tweet)
        }
      }
      logger.info("Remoderation finishing")

    case unknown =>
      logger.warn(s"What to do with $unknown ?")
  }


}
