import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "twist"
  val appVersion      = "1.0-SNAPSHOT"

  val sprayVersion = "1.2-RC1"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "org.mockito" % "mockito-all" % "1.9.5" % "test",
    "com.typesafe.akka" %% "akka-testkit" % "2.2.0" % "test",
    "org.twitter4j" % "twitter4j-core" % "3.0.3",
    "com.typesafe.slick" %% "slick" % "1.0.1",
    "org.slf4j" % "slf4j-nop" % "1.6.4", // Needed for slick
    "io.spray"  % "spray-client" % sprayVersion,
    "com.github.tototoshi" %% "slick-joda-mapper" % "0.4.0",
    "wordpress-java" % "jwordpress" % "0.5.1"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(defaultScalaSettings:_*).settings(
    resolvers ++= Seq(
      "Spray repo" at "http://repo.spray.io",
      "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
      "jwordpress" at "https://wordpress-java.googlecode.com/svn/repo",
      "jboss-3rd-party-releases" at "https://repository.jboss.org/nexus/content/repositories/thirdparty-releases"
    )
  )

}
