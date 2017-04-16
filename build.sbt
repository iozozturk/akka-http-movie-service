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
    "com.typesafe.akka" %% "akka-http" % "10.0.0",
    "de.heikoseeberger" %% "akka-http-circe" % "1.15.0", // for play-json lib support
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.0"
  ),
  javaOptions in run ++= Seq("-Xms128m", "-Xmx1024m"),
  Keys.fork in run := true,
  mainClass in(Compile, run) := Some("Application")
)

Revolver.settings