package aggregate

import monocle.Lens
import util.Id

trait Aggregate[A] {
  def _id: Lens[A, Id[A]]

  def _version: Lens[A, Version]

  def label: String
}

object Aggregate {
  def apply[T](idLens: Lens[T, Id[T]], versionLens: Lens[T, Version], l: String): Aggregate[T] = new Aggregate[T] {
    val _id = idLens

    val _version = versionLens

    val label = l
  }

  object syntax {
    implicit def toAggregateOps[A: Aggregate](a: A): AggregateOps[A] = new AggregateOps(a)

    class AggregateOps[A](a: A)(implicit agg: Aggregate[A]) {
      def id: Id[A] = agg._id.get(a)

      def version: Version = agg._version.get(a)

      def withVersion(v: Version): A = agg._version.modify(_ => v)(a)

      def withNextVersion: A = agg._version.modify(_.next)(a)
    }
  }
}
