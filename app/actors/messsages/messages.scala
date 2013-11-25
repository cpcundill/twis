package actors.messsages

import org.joda.time.DateTime
import twitter4j.Status
import domain.Tweet

case object FindAllTweets
case class FindTweetsInRange(from: DateTime, to: DateTime)
case class FindTweetsInRangeWithRank(from: DateTime, to: DateTime, rank: Double)

case class ModerateTweet(status: Status, tweet: Tweet)
case class RemoderateTweets(from: DateTime, to: DateTime)

