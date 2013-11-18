package core

import play.api.Play.current
import play.api.db.DB
import scala.slick.session.Database
import scala.slick.driver.H2Driver.simple._
import org.joda.time.DateTime
import com.github.tototoshi.slick.JodaSupport._
import scala.util.Try
import java.sql.{SQLException, SQLIntegrityConstraintViolationException}
import play.api.Logger
import core.TweetLinks

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

  /*def read(id: TweetId): Option[TweetWithLinks] = withDatabaseSession { implicit s: scala.slick.session.Session =>
    val query = for {
        (t, l) <- Tweets where (_.id === id) leftJoin TweetLinks on (_.id === _.tweetId)
      } yield (t, l.?)
    val results: List[(Tweet, Option[TweetLink])] = Query(query).list
    if (results.isEmpty) None
    else {
      val foo = results.flatMap(_._2)
      Some((results.head._1, foo))
    }
  } */

  def readLinks(id: TweetId): List[TweetLink] = withDatabaseSession { implicit s: scala.slick.session.Session =>
    Query(TweetLinks).where(_.tweetId === id).list
  }

  def find: List[Tweet] = withDatabaseSession { implicit s: scala.slick.session.Session =>
    Query(Tweets).list
  }

  def find(from: DateTime, to: DateTime): List[Tweet] = withDatabaseSession { implicit s: scala.slick.session.Session =>
    Query(Tweets).filter(_.date >= from).filter(_.date <= to)
      .sortBy(_.rank).list
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
