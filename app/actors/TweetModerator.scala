package actors

import akka.actor.Actor
import core.{TwitterClient, TweetOperations}
import twitter4j.Status
import actors.messsages.{RemoderateTweets, ModerateTweet}
import play.api.libs.ws.WS
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import scala.collection.JavaConversions._
import scala.util.Try

class TweetModerator(val lowQualityUrls: Set[String]) extends Actor with TweetOperations {

  private val client = new TwitterClient

  def receive = {

    case ModerateTweet(s, t) =>
      var rank = 1.0
      rank = rank - retweetPenalty(s)
      urlPenalty(s).map { penalty =>
        val newRank = rank - penalty
        logger.debug(s"Awarding Tweet ${t.id} rank score of $newRank")
        update(t.copy(rank = newRank))
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

  def retweetPenalty(s: Status) = {
    if (s.getRetweetCount == 0) 0.3
    else if (s.getRetweetCount < 5) 0.2
    else if (s.getRetweetCount < 10) 0.1
    else 0.0
  }

  def urlPenalty(s: Status): Future[Double] = {
    val urls = s.getURLEntities.map(_.getExpandedURL).toList
    if (urls.isEmpty) Future(0.3)
    else
      Future.traverse(urls) { url =>
        if (lowQualityUrls.exists(lqu => url.contains(lqu))) Future(0.1)
        WS.url(url).withFollowRedirects(true).get().map { r =>
            if (r.status == 404) 0.2
            else 0.0
        }
      }.map(_.min)
  }


}
