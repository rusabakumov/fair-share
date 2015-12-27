package aggregate

import scalaz._

object BuildFromEvents {
  def apply[A, C, M](events: NonEmptyList[EventXor[A, C, M]])(implicit ch: CreationHandler[A, C], chh: ChangeHandler[A, M]): A = {
    val firstEvent = events.head
    val otherEvents = events.tail

    firstEvent match {
      case -\/(e) =>
        val a = ch.handle(e.fact)
        apply(a, otherEvents.toList)
      case _ => sys.error("Expected first event for the aggregate to be CreationEvent, got ModificationEvent instead.")
    }
  }

  def apply[A, C, M](a: A, events: List[EventXor[A, C, M]])(implicit ch: CreationHandler[A, C], chh: ChangeHandler[A, M]): A = {
    events match {
      case Nil => a
      case h :: t => h match {
        case \/-(e) => apply(chh.handle(a, e.fact), t)
        case _ => sys.error("Tried to create an aggregate more than 1 time.")
      }
    }
  }
}
