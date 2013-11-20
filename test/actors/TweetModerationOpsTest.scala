package actors

import org.specs2.mock.Mockito
import org.specs2.mutable._
import twitter4j.{URLEntity, Status}
import scala.Array
import scala.concurrent.duration._
import org.specs2.time.NoTimeConversions

class TweetModerationOpsTest extends Specification with Mockito with NoTimeConversions {

  val moderator = new TweetModerationOps {}

  "The retweetPenalty operation of TweetModerationOps" should {

    "calculate a 0.3 penalty for zero retweets" in {
      val status = mock[Status]
      status.getRetweetCount returns 0
      moderator.retweetPenalty(status) must be equalTo 0.3
    }

    "calculate a 0.2 penalty for less than 5 retweets" in {
      val status = mock[Status]
      status.getRetweetCount returns 4
      moderator.retweetPenalty(status) must be equalTo 0.2
    }

    "calculate a 0.1 penalty for 5 to 9 retweets" in {
      val status = mock[Status]
      status.getRetweetCount returns 5 thenReturn 9
      moderator.retweetPenalty(status) must be equalTo 0.1
      moderator.retweetPenalty(status) must be equalTo 0.1
    }

    "calculate a zero penalty for 10 or more retweets" in {
      val status = mock[Status]
      status.getRetweetCount returns 10
      moderator.retweetPenalty(status) must be equalTo 0.0
    }

  }

  "The languagePenalty operation of TweetModerationOps" should {

    "calculate a 1.0 penalty for foreign language text" in {
      val status = mock[Status]
      status.getText returns "@functional2ch: Scalaスレ 2013/11/19(火) 16:02:28.23 \\n FXで死んだらあり金溶かすだけじゃ済まないけどな \\n2ch to RSS http://bit.ly/17GQOST #scala #2ch"
      moderator.languagePenalty(status) must be_==(1.0).await(retries = 2, timeout = 2 seconds)
    }

    "calculate a zero penalty for English language text" in {
      val status = mock[Status]
      status.getText returns "@sadovnikov: Completed week 2 assignment of #coursera Reactive Programming in #scala. These assignments do not get easier..."
      moderator.languagePenalty(status) must be_==(0.0).await(retries = 2, timeout = 2 seconds)
    }

  }

  "The urlPenalty operation of TweetModerationOps" should {

    "calculate a 1.0 penalty for a no URLs" in {
      val status = mock[Status]
      status.getURLEntities returns Array.empty
      moderator.urlPenalty(status) must be_==(1.0).await
    }

    "calculate a 1.0 penalty for a bad URL" in {
      val status = mock[Status]
      val url = mock[URLEntity]
      url.getExpandedURL returns "http://www.badhost.cc"
      status.getURLEntities returns Array(url)
      moderator.urlPenalty(status) must be_==(1.0).await(retries = 2, timeout = 2 seconds)
    }

    "calculate a 0.3 penalty for an invalid URL" in {
      val status = mock[Status]
      val url = mock[URLEntity]
      url.getExpandedURL returns "http://www.google.co.uk/invalid"
      status.getURLEntities returns Array(url)
      moderator.urlPenalty(status) must be_==(0.3).await(retries = 2, timeout = 2 seconds)
    }

    "calculate a zero penalty for a valid URL" in {
      val status = mock[Status]
      val url = mock[URLEntity]
      url.getExpandedURL returns "http://www.google.co.uk"
      status.getURLEntities returns Array(url)
      moderator.urlPenalty(status) must be_==(0.0).await(retries = 2, timeout = 2 seconds)
    }

    "calculate a 1.0 penalty for a low quality URL" in {
      val status = mock[Status]
      val url = mock[URLEntity]
      url.getExpandedURL returns "http://www.stackoverflow.com"
      status.getURLEntities returns Array(url)
      moderator.urlPenalty(status) must be_==(1.0).await(retries = 2, timeout = 2 seconds)
    }

    "calculate a zero penalty for a valid URL alongside a low quality URL" in {
      val status = mock[Status]
      val url = mock[URLEntity]
      url.getExpandedURL returns "http://www.google.co.uk"
      val lowQualityUrl = mock[URLEntity]
      lowQualityUrl.getExpandedURL returns "http://www.stackoverflow.com"
      status.getURLEntities returns Array(url, lowQualityUrl)
      moderator.urlPenalty(status) must be_==(0.0).await(retries = 2, timeout = 2 seconds)
    }

  }

  "The withCalculatedRank operation of TweetModerationOps" should {

    "calculate a 0 score for an invalid URL and foreign language text" in {
      val status = mock[Status]
      status.getText returns "@functional2ch: Scalaスレ 2013/11/19(火) 16:02:28.23 \\n FXで死んだらあり金溶かすだけじゃ済まないけどな \\n2ch to RSS http://bit.ly/17GQOST #scala #2ch"
      val url = mock[URLEntity]
      url.getExpandedURL returns "http://www.google.co.uk/invalid"
      status.getURLEntities returns Array(url)
      moderator.withCalculatedRank(status){score => score} must be_==(0.0).await(retries = 2, timeout = 2 seconds)
    }

    "calculate a 1 score for a valid URL, English language text and over 10 retweets" in {
      val status = mock[Status]
      status.getRetweetCount returns 12
      status.getText returns "@sadovnikov: Completed week 2 assignment of #coursera Reactive Programming in #scala. These assignments do not get easier... https://www.coursera.org/course/reactive"
      val url = mock[URLEntity]
      url.getExpandedURL returns "https://www.coursera.org/course/reactive"
      status.getURLEntities returns Array(url)
      moderator.withCalculatedRank(status){score => score} must be_==(1.0).await(retries = 2, timeout = 2 seconds)
    }

  }

}
