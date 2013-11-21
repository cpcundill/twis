package core

import play.api.Play.current
import play.api.db.DB
import scala.slick.session.Database
import scala.slick.driver.H2Driver.simple._
import domain._

trait UserOperations {

  def read(userId: UserId): Option[UserWithPermissions] = withDatabaseSession { implicit s =>
    val q = for {
      (u, p) <- Users where (_.id === userId) innerJoin UserPermissions
    } yield (u, p)
    val bar = q.list
    bar.headOption map { a =>
      (a._1, bar.map(_._2))
    }
  }

  private def withDatabaseSession[T](f: scala.slick.session.Session => T): T =
    Database.forDataSource(DB.getDataSource()) withSession { s => f(s) }

}
