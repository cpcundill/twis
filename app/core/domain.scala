package core

import org.joda.time.DateTime
import scala.slick.driver.H2Driver.simple._
import com.github.tototoshi.slick.JodaSupport._  // Stay boy!


case class Tweet(id: TweetId, username: Option[String], displayName: Option[String], date: DateTime, text: String, rank: Double = 0)

object Tweets extends Table[Tweet]("TWEETS") {
  def id = column[TweetId]("ID", O.PrimaryKey)
  def username = column[Option[String]]("USERNAME")
  def displayName = column[Option[String]]("DISPLAYNAME")
  def date = column[DateTime]("DATE")
  def text = column[String]("TEXT")
  def rank = column[Double]("RANK")
  def * = id ~ username ~ displayName ~ date ~ text ~ rank <> (Tweet , Tweet.unapply _)
}

case class TweetLink(id: Option[TweetLinkId], tweetId: TweetId, url: String)

object TweetLinks extends Table[TweetLink]("TWEET_LINKS") {
  def id = column[TweetLinkId]("ID", O.PrimaryKey, O.AutoInc)
  def tweetId = column[TweetId]("TWEET_ID")
  def url = column[String]("URL")
  def * = id.? ~ tweetId ~ url <> (TweetLink , TweetLink.unapply _)
  def forInsert = tweetId ~ url <> ({ t => TweetLink(None, t._1, t._2)}, { (tl: TweetLink) => Some((tl.tweetId, tl.url))})
  def tweet = foreignKey("TL_T_FK", tweetId, Tweets)(_.id)
}



