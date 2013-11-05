package core

import org.joda.time.DateTime
import scala.slick.driver.H2Driver.simple._
import com.github.tototoshi.slick.JodaSupport._  // Stay boy!


case class Tweet(id: TweetId, username: Option[String], displayName: Option[String], date: DateTime, text: String, link: Option[String])

object Tweets extends Table[Tweet]("TWEETS") {
  def id = column[TweetId]("ID", O.PrimaryKey)
  def username = column[Option[String]]("USERNAME")
  def displayName = column[Option[String]]("DISPLAYNAME")
  def date = column[DateTime]("DATE")
  def text = column[String]("TEXT")
  def link = column[Option[String]]("LINK")
  def * = id ~ username ~ displayName ~ date ~ text ~ link <> (Tweet , Tweet.unapply _)
}



