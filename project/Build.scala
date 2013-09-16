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
    "org.twitter4j" % "twitter4j-core" % "3.0.3",
    "com.typesafe.slick" %% "slick" % "1.0.1",
    "org.slf4j" % "slf4j-nop" % "1.6.4" // Needed for slick
    /*"com.shorrockin" %% "cascal" % "1.3-SNAPSHOT",
      "com.datastax.cassandra"  % "cassandra-driver-core" % "1.0.1"  exclude("org.xerial.snappy", "snappy-java"),
      "org.xerial.snappy"       % "snappy-java"           % "1.0.5" //https://github.com/ptaoussanis/carmine/issues/5*/
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )

}
