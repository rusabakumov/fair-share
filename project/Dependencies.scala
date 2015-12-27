import sbt._

object Dependencies {
  import sbt.Test

  val scalazVersion = "7.2.+"

  val scalazDeps = List(
    "org.scalaz" %% "scalaz-core" % scalazVersion,
    "org.scalaz" %% "scalaz-concurrent" % scalazVersion
  )

  val monocleVersion = "1.2.0"

  val monocleDeps = List(
    "com.github.julien-truffaut"  %%  "monocle-core"    % monocleVersion,
    "com.github.julien-truffaut"  %%  "monocle-generic" % monocleVersion,
    "com.github.julien-truffaut"  %%  "monocle-macro"   % monocleVersion,
    "com.github.julien-truffaut"  %%  "monocle-state"   % monocleVersion,
    "com.github.julien-truffaut"  %%  "monocle-refined" % monocleVersion,
    "com.github.julien-truffaut"  %%  "monocle-law"     % monocleVersion % Test
  )

  val scalatestVersion = "2.2.4"

  val scalatestDeps = List(
    "org.scalatest" %% "scalatest" % scalatestVersion % "test"
  )
}
