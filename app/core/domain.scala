package core

import org.joda.time.DateTime
import scala.slick.driver.H2Driver.simple._

case class Tweet(id: TweetId, date: DateTime, text: String)

// Definition of the tweets table
object Tweets extends Table[(TweetId, String)]("TWEETS") {
  def id = column[TweetId]("ID", O.PrimaryKey) // This is the primary key column
  def text = column[String]("TEXT")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id ~ text
}
