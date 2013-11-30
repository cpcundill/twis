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
import domain._
import domain.TweetLink
import domain.Tweet

trait TweetOperations {

  val logger = Logger(this.getClass)

  def create(t: Tweet) = withDatabaseSession { implicit s =>
    Try(Tweets.insert(t)).recover {
      case e: SQLIntegrityConstraintViolationException =>
        logger.debug("Ignored primary key constraint violation due to expected overlap in processing")
      case other: SQLException => throw other
    }
  }

  def create(l: TweetLink) = withDatabaseSession { implicit s =>
    TweetLinks.insert(l)
  }

  def readLinks(id: TweetId): List[TweetLink] = withDatabaseSession { implicit s =>
    Query(TweetLinks).where(_.tweetId === id).list
  }

  def find: List[Tweet] = withDatabaseSession { implicit s: scala.slick.session.Session =>
    Query(Tweets).sortBy(_.date).list
  }

  def find(from: DateTime, to: DateTime): List[Tweet] = withDatabaseSession { implicit s =>
    Query(Tweets).filter(_.date >= from).filter(_.date <= to)
      .sortBy(_.rank.desc).list
  }

  def find(from: DateTime, to: DateTime, minRank: Double): List[Tweet] = withDatabaseSession { implicit s =>
    Query(Tweets).filter(_.date >= from).filter(_.date <= to).filter(_.rank >= minRank)
      .sortBy(_.rank.desc).list
  }

  def getMaxId: Long = withDatabaseSession { implicit s =>
    Query(Tweets.map(_.id).max).first.getOrElse(0)
  }

  def update(t: Tweet) = withDatabaseSession { implicit s =>
    Query(Tweets).filter(_.id === t.id).update(t)
  }

  private def withDatabaseSession[T](f: scala.slick.session.Session => T): T =
    Database.forDataSource(DB.getDataSource()) withSession { s => f(s) }

}
