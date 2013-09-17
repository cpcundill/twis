package core

import play.api.Play.current
import play.api.db.DB
import scala.slick.session.{Session, Database}
import scala.slick.driver.H2Driver.simple._
import org.joda.time.DateTime
import java.sql.Timestamp

trait TweetOperations extends Logging {

  def create(t: Tweet) = withDatabaseSession { implicit s: scala.slick.session.Session =>
    Tweets.insert(t.id, new Timestamp(t.date.getMillis), t.text)
  }

  def read(id: TweetId) = ???

  def find: List[Tweet] = withDatabaseSession { implicit s: scala.slick.session.Session =>
    Query(Tweets).list.map(a => Tweet(a._1, new DateTime(a._2), a._3))
  }

  def getMaxId: Long = withDatabaseSession { implicit s: scala.slick.session.Session =>
    Query(Tweets.map(_.id).max).first.getOrElse(0)
  }

  def withDatabaseSession[T](f: scala.slick.session.Session => T): T =
    Database.forDataSource(DB.getDataSource()) withSession { s => f(s) }

}
