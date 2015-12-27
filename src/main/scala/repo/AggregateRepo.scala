package repo

import aggregate.EventSourced.syntax._
import aggregate._
import util.Id

import scalaz.{ NonEmptyList, \/ }
import util.Validation._

trait AggregateRepo[A] {
  def getAggregate(id: Id[A]): Throwable \/ Option[A]

  def storeAggregate(a: A): Throwable \/ Unit
}

class EventSourcedAggregateRepo[A, C, M](eventsRepo: EventXorRepo[A, C, M])(implicit es: EventSourced[A, C, M], ch: CreationHandler[A, C], chh: ChangeHandler[A, M]) extends AggregateRepo[A] {
  def getAggregate(id: Id[A]): Throwable \/ Option[A] = eventsRepo.getEvents(id).map {
    case Nil => None
    case h :: t => Some[A](BuildFromEvents(NonEmptyList(h, t: _*)))
  }

  def storeAggregate(a: A): Throwable \/ Unit = eventsRepo.storeEvents(a.uncommittedEvents.toList)
}

object AggregateRepo {

  object syntax {
    implicit def toAggregateRepoOps[A: Aggregate](ar: AggregateRepo[A]): AggregateRepoOps[A] =
      new AggregateRepoOps(ar)

    class AggregateRepoOps[A: Aggregate](repo: AggregateRepo[A]) {
      def getV: Id[A] => String \/ A = id => {
        validateSuccess(repo.getAggregate(id)).flatMap(validatePresence)
      }

      def storeV: A => String \/ Unit = { a =>
        validateSuccess(repo.storeAggregate(a))
      }
    }

  }

}
