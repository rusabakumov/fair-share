import scalariform.formatter.preferences._

name := """fair-share"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8", // yes, this is 2 args
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code", // N.B. doesn't work well with the ??? hole
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused-import" // 2.11 only
)

libraryDependencies ++= List(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalaz" %% "scalaz-core" % "7.2.+",
  "org.scalaz" %% "scalaz-concurrent" % "7.2.+"
)

scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(DoubleIndentClassDeclaration, true)

val formatCode = taskKey[Unit]("Format all code")

formatCode := {
  ScalariformKeys.format.in(Compile).value
  ScalariformKeys.format.in(Test).value
}
