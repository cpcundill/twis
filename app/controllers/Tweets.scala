package controllers

import play.api.mvc.{Action, Controller}
import play.libs.Akka
import akka.pattern.ask
import core.Tweet
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global // stay boy!

object Tweets extends Controller {

  private val tweetReader = Akka.system().actorFor("akka://application/user/tweetReader")
  implicit val timeout: Timeout = 5

  def index = Action { Async {
    (tweetReader ? "go-find-em").mapTo[List[Tweet]]
      .map { a => Ok(views.html.tweets.index(a))}
    }
  }

}
