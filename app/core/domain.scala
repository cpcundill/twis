package core

import org.joda.time.DateTime
import scala.slick.driver.H2Driver.simple._
import java.sql.Timestamp

case class Tweet(id: TweetId, date: DateTime, text: String)

object Tweets extends Table[(TweetId, Timestamp, String)]("TWEETS") {
  def id = column[TweetId]("ID", O.PrimaryKey)
  def date = column[Timestamp]("DATE")
  def text = column[String]("TEXT")
  def * = id ~ date ~ text
}
