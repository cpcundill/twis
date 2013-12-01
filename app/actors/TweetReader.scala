package actors

import akka.actor.Actor
import core.{TwitterClient, TweetOperations}
import play.api.Logger
import actors.messsages.{FindTweetsInRangeWithRank, FindTweetsInRange, FindAllTweets}

class TweetReader extends Actor with TweetOperations {

  def receive = {
    case FindAllTweets => sender ! find
    case FindTweetsInRange(from, to) => sender ! find(from, to)
    case FindTweetsInRangeWithRank(from, to, rank) => sender ! find(from, to, rank)
  }

}
