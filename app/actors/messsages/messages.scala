package actors.messsages

import org.joda.time.DateTime
import core.Tweet
import twitter4j.Status

case object FindAllTweets
case class FindTweetsInRange(from: DateTime, to: DateTime)

case class ModerateTweet(status: Status, tweet: Tweet)
case class RemoderateTweets(from: DateTime, to: DateTime)

