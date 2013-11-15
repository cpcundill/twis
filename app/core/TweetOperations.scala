package core

import play.api.Play.current
import play.api.db.DB
import scala.slick.session.Database
import scala.slick.driver.H2Driver.simple._
import org.joda.time.DateTime
import com.github.tototoshi.slick.JodaSupport._
import scala.util.Try
import scala.util.Success
import java.sql.{SQLException, SQLIntegrityConstraintViolationException}
import play.api.Logger

// Stay boy!

trait TweetOperations {

  val logger = Logger(this.getClass)

  def create(t: Tweet) = withDatabaseSession { implicit s: scala.slick.session.Session =>
    Try(Tweets.insert(t)).recover {
      case e: SQLIntegrityConstraintViolationException =>
        logger.debug("Ignored primary key constraint violation due to expected overlap in processing")
      case other: SQLException => throw other
    }
  }

  def create(l: TweetLink) = withDatabaseSession { implicit s: scala.slick.session.Session =>
    TweetLinks.insert(l)
  }

  def read(id: TweetId): TweetWithLinks = withDatabaseSession { implicit s: scala.slick.session.Session =>
    val foo = for {
      l <- TweetLinks if l.tweetId === id
      t <- l.tweet
    } yield (t, l)
    val results = foo.list
    (results.head._1, results.map(_._2))
  }

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

  def update(t: Tweet) = withDatabaseSession { implicit s: scala.slick.session.Session =>
    Query(Tweets).filter(_.id === t.id).update(t)
  }

  private def withDatabaseSession[T](f: scala.slick.session.Session => T): T =
    Database.forDataSource(DB.getDataSource()) withSession { s => f(s) }

}
