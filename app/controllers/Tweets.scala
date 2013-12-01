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


import actors.messsages.{FindTweetsInRangeWithRank, RemoderateTweets, FindTweetsInRange, FindAllTweets}
import domain.Tweet
import securesocial.core.SecureSocial
import core.ScalaBlogMan
import akka.actor.ActorRef

class Tweets(tweetReader: ActorRef, tweetModerator: ActorRef) extends Controller with SecureSocial {

  implicit val timeout: Timeout = 2 second

  def index = SecuredAction(ScalaBlogMan).async { implicit request =>
    (tweetReader ? FindAllTweets).mapTo[List[Tweet]]
      .map { tweets => Ok(views.html.tweets.index(tweets))}
  }

  def range(from: String, to: String) = SecuredAction(ScalaBlogMan).async { implicit request =>
    (tweetReader ? FindTweetsInRange(from, to)).mapTo[List[Tweet]]
      .map { tweets => Ok(views.html.tweets.range(tweets, new Interval(from, to), 0))}
  }

  def rangeWithMinRank(from: String, to: String, minRank: Double) = SecuredAction(ScalaBlogMan).async { implicit request =>
    (tweetReader ? FindTweetsInRangeWithRank(from, to, minRank)).mapTo[List[Tweet]]
      .map { tweets => Ok(views.html.tweets.range(tweets, new Interval(from, to), minRank))}
  }

  def remoderate(from: String, to: String) = SecuredAction(ScalaBlogMan) {
    tweetModerator ! RemoderateTweets(from, to)
    Ok("Remoderation started").as(TEXT)
  }

  implicit private def stringToDateTime(isoDateString: String): DateTime =
    ISODateTimeFormat.dateTimeParser().parseDateTime(isoDateString)

}
