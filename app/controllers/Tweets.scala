package controllers

import play.api.mvc.{Action, Controller}
import play.libs.Akka
import akka.pattern.ask
import core.Tweet
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import org.joda.time.{Interval, DateTime}
import org.joda.time.format.ISODateTimeFormat
import scala.concurrent.duration._


import actors.messsages.{FindTweetsInRange, FindAllTweets}

object Tweets extends Controller {

  private val tweetReader = Akka.system().actorSelection("akka://application/user/tweetReader")
  implicit val timeout: Timeout = 2 second

  def index = Action.async { implicit request =>
    (tweetReader ? FindAllTweets).mapTo[List[Tweet]]
      .map { tweets => Ok(views.html.tweets.index(tweets))}
  }

  def range(from: String, to: String) = Action.async { implicit request =>
    (tweetReader ? FindTweetsInRange(from, to)).mapTo[List[Tweet]]
      .map { tweets => Ok(views.html.tweets.range(tweets, new Interval(from, to)))}
  }

  implicit private def stringToDateTime(isoDateString: String): DateTime =
    ISODateTimeFormat.dateTimeParser().parseDateTime(isoDateString)

}
