package core

import org.specs2.mutable.Specification

class TwitterClientTest extends Specification {

  val twitterClient = new TwitterClient

  "The Twitter Client" should {

    "find tweets with hashtag #Scala" in {
      val result = twitterClient.search("#Scala")
      result must not be empty
    }

  }

}
