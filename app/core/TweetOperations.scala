package core

import play.api.Play.current
import play.api.db.DB
import scala.slick.session.Database
import scala.slick.driver.H2Driver.simple._

// Use the implicit threadLocalSession
import Database.threadLocalSession

trait TweetOperations extends Logging {

  def create(t: Tweet) {
    logger.info(s"Creating...$t")
    Database.forDataSource(DB.getDataSource()) withSession {
      Tweets.insert(t.id, t.text)
    }
  }

  def read(id: TweetId) = ???

  def find() = Database.forDataSource(DB.getDataSource()) withSession {
    Query(Tweets) foreach {
      case (id, text) => logger.info("Found " + id + " " + text)
      case a => logger.info("Found " + a)
    }
  }

  def getMaxId(search: String) = ???

}
