package controllers

import play.api.mvc.Controller
import play.libs.Akka
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import org.joda.time.{Interval, DateTime}
import org.joda.time.format.ISODateTimeFormat
import scala.concurrent.duration._


import actors.messsages.{RemoderateTweets, FindTweetsInRange, FindAllTweets}
import domain.Tweet
import securesocial.core.SecureSocial
import core.ScalaBlogMan

object Tweets extends Controller with SecureSocial {

  private val tweetReader = Akka.system().actorSelection("akka://application/user/tweetReader")
  private val tweetModerator = Akka.system().actorSelection("akka://application/user/tweetModerator")
  implicit val timeout: Timeout = 2 second

  def index = SecuredAction(ScalaBlogMan).async { implicit request =>
    (tweetReader ? FindAllTweets).mapTo[List[Tweet]]
      .map { tweets => Ok(views.html.tweets.index(tweets))}
  }

  def range(from: String, to: String) = SecuredAction(ScalaBlogMan).async { implicit request =>
    (tweetReader ? FindTweetsInRange(from, to)).mapTo[List[Tweet]]
      .map { tweets => Ok(views.html.tweets.range(tweets, new Interval(from, to)))}
  }

  def remoderate(from: String, to: String) = SecuredAction(ScalaBlogMan) {
    tweetModerator ! RemoderateTweets(from, to)
    Ok("Remoderation started").as(TEXT)
  }

  implicit private def stringToDateTime(isoDateString: String): DateTime =
    ISODateTimeFormat.dateTimeParser().parseDateTime(isoDateString)

}
