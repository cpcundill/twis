package actors.messsages

import org.joda.time.DateTime

case object FindAllTweets
case class FindTweetsInRange(from: DateTime, to: DateTime)

