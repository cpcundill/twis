package core

import org.joda.time.DateTime
import scala.slick.driver.H2Driver.simple._
import com.github.tototoshi.slick.JodaSupport._  // Stay boy!


case class Tweet(id: TweetId, date: DateTime, text: String, link: Option[String])

object Tweets extends Table[Tweet]("TWEETS") {
  def id = column[TweetId]("ID", O.PrimaryKey)
  def date = column[DateTime]("DATE")
  def text = column[String]("TEXT")
  def link = column[Option[String]]("LINK")
  def * = id ~ date ~ text ~ link <> (Tweet , Tweet.unapply _)
}



