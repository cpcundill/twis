package actors

import akka.actor.Actor
import core.{Tweet, TweetOperations, TwitterClient}
import scala.collection.JavaConversions._
import twitter4j.Status
import org.joda.time.DateTime
import play.api.Logger

class TweetCurator extends Actor with TweetOperations {

  val client = new TwitterClient
  val logger = Logger(this.getClass)

  def receive = {

    case q: String =>
      logger.info(s"Started curating of tweets with search string: '$q'.")
      val result = client.search(q, getMaxId)
      result.getTweets.map(mapStatus).foreach(create)
      logger.info(s"Finished curating tweets.  Got ${result.getCount} tweets.")

    case unknown =>
      logger.warn(s"What to do with $unknown ?")
  }

  private def mapStatus(s: Status): Tweet = {
    Tweet(s.getId, Some(s.getUser.getName), Some(s.getUser.getScreenName), new DateTime(s.getCreatedAt), s.getText, None)
  }

}
