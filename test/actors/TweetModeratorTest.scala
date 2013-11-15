package actors

import com.typesafe.config.ConfigFactory
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import scala.collection.JavaConversions._
import twitter4j.Status

class TweetModeratorTest extends Specification with Mockito {

  val lowQualityUrls = ConfigFactory.load().getStringList("tweet.moderation.low-quality-urls").toSet
  val tweetModerator = new TweetModerator(lowQualityUrls)

  "The TweetModerator" should {

    val status = mock[Status]

    "calculate a 0.3 penalty for zero retweets" in {
      status.getRetweetCount returns 0
      tweetModerator.retweetPenalty(status) must be equalTo 0.3
    }

    "calculate a 0.2 penalty for less than 5 retweets" in {
      status.getRetweetCount returns 4
      tweetModerator.retweetPenalty(status) must be equalTo 0.2
    }

    "calculate a 0.1 penalty for 5 to 9 retweets" in {
      status.getRetweetCount returns 5
      tweetModerator.retweetPenalty(status) must be equalTo 0.1
      status.getRetweetCount returns 9
      tweetModerator.retweetPenalty(status) must be equalTo 0.1
    }

    "calculate a zero penalty for 10 or more retweets" in {
      status.getRetweetCount returns 10
      tweetModerator.retweetPenalty(status) must be equalTo 0.0
    }

  }

}
