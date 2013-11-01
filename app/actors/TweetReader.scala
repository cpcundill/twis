package actors

import akka.actor.Actor
import core.TweetOperations
import play.api.Logger
import actors.messsages.{FindTweetsInRange, FindAllTweets}

class TweetReader extends Actor with TweetOperations {

  def receive = {
    case FindAllTweets => sender ! find
    case FindTweetsInRange(from, to) => sender ! find(from, to)
  }

}
