import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "twist"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "org.twitter4j" % "twitter4j-core" % "3.0.3"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
