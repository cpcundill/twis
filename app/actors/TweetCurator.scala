package actors

import akka.actor.Actor
import core.{Tweet, TweetOperations, TwitterClient}
import scala.collection.JavaConversions._
import twitter4j.Status
import org.joda.time.DateTime
import play.api.Logger

class TweetCurator extends Actor with TweetOperations {

  val client = new TwitterClient

  def receive = {

    case a: String =>
      val result = client.search(a)
      result.getTweets.map(mapStatus).foreach(create)

    case unknown => logger.warn(s"What to do with $unknown ?")
  }

  private def mapStatus(s: Status): Tweet = {
    Tweet(s.getId, new DateTime(s.getCreatedAt), s.getText)
  }

}
