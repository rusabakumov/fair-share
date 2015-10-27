name := """fair-share"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

// Change this to another test framework if you prefer
libraryDependencies ++= List(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalaz" %% "scalaz-core" % "7.1.4"
)

// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

