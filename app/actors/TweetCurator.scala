package actors

import akka.actor.Actor
import core._
import scala.collection.JavaConversions._
import twitter4j.{URLEntity, Status}
import org.joda.time.DateTime
import scala.Some
import play.libs.Akka
import actors.messsages.ModerateTweet
import domain.{TweetId, TweetLink, Tweet}

class TweetCurator extends Actor with TweetOperations {

  private val client = new TwitterClient
  private val tweetModerator = Akka.system().actorSelection("akka://application/user/tweetModerator")

  def receive = {

    case q: String =>
      logger.info(s"Started curating tweets using search string: '$q'.")
      val result = client.search(q, getMaxId)
      result.getTweets.foreach( s => {
        val tweet = mapStatus(s)
        create(tweet)
        s.getURLEntities.map(mapUrlEntity(tweet.id)).foreach(create)
        tweetModerator ! ModerateTweet(s, tweet)
      })
      logger.info(s"Finished curating tweets.  Got ${result.getCount} tweets.")

    case unknown =>
      logger.warn(s"What to do with $unknown ?")
  }

  private def mapStatus(s: Status): Tweet =
    Tweet(s.getId, Some(s.getUser.getName), Some(s.getUser.getScreenName), new DateTime(s.getCreatedAt), s.getText)

  private def mapUrlEntity(tweetId: TweetId)(ue: URLEntity): TweetLink = TweetLink(None, tweetId, ue.getExpandedURL)

}
