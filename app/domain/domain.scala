package domain

import org.joda.time.DateTime
import scala.slick.driver.H2Driver.simple._
import com.github.tototoshi.slick.JodaSupport._
import scala.Some

case class Tweet(id: TweetId, username: Option[String], displayName: Option[String], date: DateTime, text: String, rank: Double = 0)

object Tweets extends Table[Tweet]("TWEETS") {
  def id = column[TweetId]("ID", O.PrimaryKey)
  def username = column[Option[String]]("USERNAME")
  def displayName = column[Option[String]]("DISPLAYNAME")
  def date = column[DateTime]("DATE")
  def text = column[String]("TEXT")
  def rank = column[Double]("RANK")
  def * = id ~ username ~ displayName ~ date ~ text ~ rank <> (Tweet , Tweet.unapply _)
}

case class TweetLink(id: Option[TweetLinkId], tweetId: TweetId, url: String)

object TweetLinks extends Table[TweetLink]("TWEET_LINKS") {
  def id = column[TweetLinkId]("ID", O.PrimaryKey, O.AutoInc)
  def tweetId = column[TweetId]("TWEET_ID")
  def url = column[String]("URL")
  def * = id.? ~ tweetId ~ url <> (TweetLink , TweetLink.unapply _)
  def forInsert = tweetId ~ url <> ({ t => TweetLink(None, t._1, t._2)}, { (tl: TweetLink) => Some((tl.tweetId, tl.url))})
  def tweet = foreignKey("TL_T_FK", tweetId, Tweets)(_.id)
}

case class User(id: UserId, password: String)

object Users extends Table[User]("USERS") {
  def id = column[UserId]("ID", O.PrimaryKey)
  def password = column[String]("PASSWORD")
  def * = id ~ password <> (User , User.unapply _)
}


sealed trait Permission
case object Basic extends Permission
case object Admin extends Permission

trait PermissionMapping {

  implicit val boolTypeMapper = MappedTypeMapper.base[Permission, String](
  { permission => permission match {
    case Basic => "BASIC"
    case Admin => "ADMIN"
  }}, 
  { str => str match {
    case "BASIC" => Basic
    case "ADMIN" => Admin
  }})
}

case class UserPermission(id: Option[UserPermissionId], userId: UserId, permission: Permission)

object UserPermissions extends Table[UserPermission]("USER_PERMISSIONS") with PermissionMapping {
  def id = column[UserPermissionId]("ID", O.PrimaryKey, O.AutoInc)
  def userId = column[UserId]("USER_ID")
  def permission = column[Permission]("PERMISSION")
  def * = id.? ~ userId ~ permission <> (UserPermission , UserPermission.unapply _)
  def forInsert = userId ~ permission <> ({ t => UserPermission(None, t._1, t._2)}, { (tl: UserPermission) => Some((tl.userId, tl.permission))})
  def user = foreignKey("UP_P_FK", userId, Users)(_.id)
}




