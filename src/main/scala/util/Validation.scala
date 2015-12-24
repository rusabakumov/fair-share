package util

import scalaz.{ Show, \/ }
import scalaz.std.java.throwable._

trait Validation {
  type V[T] = String \/ T
}

object Validation {
  def exception(e: Throwable): String = Show[Throwable].shows(e)

  def notFound[T](id: Id[T])(implicit agg: Aggregate[T]): String = {
    s"${agg.name} with id = $id doesn't exist."
  }
}

