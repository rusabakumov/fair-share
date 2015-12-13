package repos

import event.Event
import occasion.{Occasion, OccasionEvent, OccasionId}
import participant.{Participant, ParticipantId}
import util._

import scalaz.std.java.throwable._
import scalaz.syntax.either._
import scalaz.{Kleisli, Show, \/}

trait Repo {
  def validate[T](item: Throwable \/ Option[T]): V[T] = {
    val result = item.fold(
      Show[Throwable].shows(_).left[T],
      {
        case None => s"Item does not exist.".left[T]
        case Some(it) => it.right[String]
      }
    )
    result
  }
}

trait OccasionRepo extends Repo {
  def get: Kleisli[V, OccasionId, Occasion] = Kleisli[V, OccasionId, Occasion] {
    id => validate(get(id))
  }

  def store: Kleisli[V, (OccasionId, Event[OccasionEvent]), Unit] = Kleisli[V, (OccasionId, Event[OccasionEvent]), Unit] {
    case (id, event) => store(id, event)
  }

  protected def get(id: OccasionId): Throwable \/ Option[Occasion]
  protected def store(id: OccasionId, ev: Event[OccasionEvent]): V[Unit]
}

trait ParticipantRepo extends Repo {
  def get: Kleisli[V, ParticipantId, Participant] = Kleisli[V, ParticipantId, Participant] {
    id => validate(get(id))
  }

  def store: Kleisli[V, (ParticipantId, String), Unit] = Kleisli[V, (ParticipantId, String), Unit] {
    case (id, name) => store(id, name)
  }

  protected def get(id: ParticipantId): Throwable \/ Option[Participant]
  protected def store(id: ParticipantId, name: String): V[Unit]
}
