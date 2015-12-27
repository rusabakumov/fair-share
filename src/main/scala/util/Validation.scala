package util

import aggregate.Aggregate.syntax._
import aggregate.{ Aggregate, Version }

import scalaz.std.java.throwable._
import scalaz.syntax.either._
import scalaz.{ Show, \/ }

trait Validation {
  type Valid[T] = String \/ T
}

object Validation {
  def validateSuccess[T]: Throwable \/ T => Valid[T] = res => res.fold(
    Show[Throwable].shows(_).left[T],
    _.right[String]
  )

  def validatePresence[A: Aggregate]: Option[A] => Valid[A] = {
    case None => s"Requested ${implicitly[Aggregate[A]].label} not found".left[A]
    case Some(x) => x.right[String]
  }

  def validateVersion[A](version: Version)(implicit agg: Aggregate[A]): A => Valid[A] = a => {
    if (version != a.version)
      s"You're working with an outdated ${agg.label}. Expected version ${a.version}, actual $version".left[A]
    else a.right[String]
  }
}

