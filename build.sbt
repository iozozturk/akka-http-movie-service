import sbt.Keys.javaOptions

name := "movie-service"

version := "1.0"

scalaVersion := "2.12.1"

resolvers += Resolver.bintrayRepo("hseeberger", "maven")

val project = Project(
  id = "movie-service",
  base = file(".")
).settings(
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.4.17",
    "com.typesafe.akka" %% "akka-http" % "10.0.0",
    "de.heikoseeberger" %% "akka-http-play-json" % "1.15.0",
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.0",
    "com.google.inject" % "guice" % "4.0",
    "org.mongodb.scala" %% "mongo-scala-driver" % "2.0.0",
    "net.codingwell" %% "scala-guice" % "4.1.0",
    "com.typesafe.akka" %% "akka-testkit" % "2.4.17" % Test,
    "org.scalatest" %% "scalatest" % "3.0.0" % Test,
    "com.typesafe.akka" %% "akka-http-testkit" % "10.0.0" % Test,
    "org.mockito" % "mockito-all" % "1.8.4" % Test
  ),
  javaOptions in run ++= Seq("-Xms128m", "-Xmx1024m"),
  Keys.fork in run := true,
  mainClass in(Compile, run) := Some("Application"),
  fork in Test := true,

  javaOptions in Test += "-Dconfig.resource=test.conf"
)

Revolver.settings