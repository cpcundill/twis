package actors

import akka.actor.Actor
import core.TweetOperations
import play.api.Logger

class TweetReader extends Actor with TweetOperations {

  def receive = {
    case a => sender ! find
  }

}
