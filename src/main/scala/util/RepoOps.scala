package util

import event.Event

import scalaz.Kleisli
import scalaz.Kleisli._
import scalaz.syntax.either._

trait RepoOps {
  implicit def repoToRichRepo[T: NamedAggregate](repo: Repo[T]): RichRepo[T] = RichRepo(repo)

  case class RichRepo[T: NamedAggregate](repo: Repo[T]) {
    def getK: Kleisli[V, Id[T], T] = kleisli[V, Id[T], T](getV)

    def storeK: Kleisli[V, (Id[T], Event[T]), Unit] = kleisli[V, (Id[T], Event[T]), Unit](storeV.tupled)

    def getV: Id[T] => V[T] = id => repo.get(id).fold(
      Validation.exception(_).left[T], {
      case None => Validation.notFound(id).left[T]
      case Some(occasion) => occasion.right[String]
    }
    )

    def storeV: (Id[T], Event[T]) => V[Unit] = {
      case (id, ev) =>
        repo.store(id, ev).leftMap(Validation.exception)
    }
  }

}
