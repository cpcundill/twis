package core

import org.joda.time.{DateTimeZone, DateTime}
import scala.slick.driver.H2Driver.simple._
import scala.slick.lifted.MappedTypeMapper.base
import java.sql.Timestamp


case class Tweet(id: TweetId, date: DateTime, text: String, link: Option[String])


object Tweets extends Table[Tweet]("TWEETS") {
  def id = column[TweetId]("ID", O.PrimaryKey)
  def date = column[DateTime]("DATE")
  def text = column[String]("TEXT")
  def link = column[Option[String]]("LINK")
  def * = id ~ date ~ text ~ link <> (Tweet , Tweet.unapply _)

  implicit val DateTimeMapper: TypeMapper[DateTime] =
    base[DateTime, Timestamp](
      d => new Timestamp(d.getMillis),
      t => new DateTime(t getTime, DateTimeZone.UTC))
}



