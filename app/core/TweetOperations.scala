package core

import play.api.Play.current
import play.api.db.DB
import scala.slick.session.Database
import scala.slick.driver.H2Driver.simple._
import org.joda.time.DateTime
import com.github.tototoshi.slick.JodaSupport._  // Stay boy!

trait TweetOperations {

  def create(t: Tweet) = withDatabaseSession { implicit s: scala.slick.session.Session =>
    Tweets.insert(t)
  }

  def read(id: TweetId) = ???

  def find: List[Tweet] = withDatabaseSession { implicit s: scala.slick.session.Session =>
    Query(Tweets).list
  }

  def find(from: DateTime, to: DateTime): List[Tweet] = withDatabaseSession { implicit s: scala.slick.session.Session =>
    Query(Tweets).filter(_.date >= from).filter(_.date <= to).list
    //for (t <- Tweets if t.date >= from) yield t
  }

  def getMaxId: Long = withDatabaseSession { implicit s: scala.slick.session.Session =>
    Query(Tweets.map(_.id).max).first.getOrElse(0)
  }

  private def withDatabaseSession[T](f: scala.slick.session.Session => T): T =
    Database.forDataSource(DB.getDataSource()) withSession { s => f(s) }

}
