package aggregate

import monocle.Lens
import Aggregate.syntax._

trait EventSourced[A, C, M] {
  def _uncommittedEvents: Lens[A, Vector[EventXor[A, C, M]]]
}

object EventSourced {
  def apply[A, C, M](l: Lens[A, Vector[EventXor[A, C, M]]]): EventSourced[A, C, M] = new EventSourced[A, C, M] {
    val _uncommittedEvents: Lens[A, Vector[EventXor[A, C, M]]] = l
  }

  object syntax {
    implicit def toEventSourcedOps[A, C, M](a: A)(implicit es: EventSourced[A, C, M]): EventSourcedOps[A, C, M] = new EventSourcedOps(a)

    class EventSourcedOps[A, C, M](a: A)(implicit es: EventSourced[A, C, M]) {
      def uncommittedEvents: Vector[EventXor[A, C, M]] = es._uncommittedEvents.get(a)

      def withEvent(e: EventXor[A, C, M]): A = es._uncommittedEvents.modify(es => es :+ e)(a)

      def withCreate(e: Event[A, C]): A = withEvent(EventXor.create(e))

      def withChange(e: Event[A, M]): A = withEvent(EventXor.change(e))
    }

  }

  object commands {

    import syntax._

    def createCommand[A: Aggregate, C, M](a: A, c: C)(implicit es: EventSourced[A, C, M]): A = {
      a.withCreate(Event.now(a.id, a.version, c))
    }

    def changeCommand[A: Aggregate, C, M](a: A, c: M)(implicit es: EventSourced[A, C, M]): A = {
      val nextA = a.withNextVersion
      nextA.withChange(Event.now(nextA.id, nextA.version, c))
    }
  }

}
