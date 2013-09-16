package actors

import akka.actor.Actor
import core.TweetOperations

class TweetReader extends Actor with TweetOperations {

  def receive = {
    case a =>
      logger.info("TweetReader says hello " + a)
      find()
  }

}
