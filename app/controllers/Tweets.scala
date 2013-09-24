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

  def index = Action {
    val foo = (tweetReader ? "go-find-em").mapTo[List[Tweet]]
    val fooey = foo.map { a => Ok(views.html.tweets.index(a))}

    Async {
      fooey
    }

    /*val tweetsPromise: Promise[List[Tweet]] = Akka.asPromise {
      (tweetReader ? "go-find-em").mapTo[List[Tweet]]
    }
    Async {
      new AkkaPromise(tweetReader ? "go-find-em")
      //tweetsPromise.map{ a => OK(views.html.index.index(a)) }
    } */

  }

}
