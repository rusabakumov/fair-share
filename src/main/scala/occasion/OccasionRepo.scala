package occasion

import event.Event
import util._

import scalaz.Kleisli

trait OccasionRepo {
  def get: Kleisli[V, OccasionId, Option[Occasion]] = Kleisli[V, OccasionId, Option[Occasion]] { id =>
    get(id)
  }

  def store: Kleisli[V, (OccasionId, Event[OccasionEvent]), Unit] = Kleisli[V, (OccasionId, Event[OccasionEvent]), Unit] {
    case (id, event) =>
      store(id, event)
  }

  protected def get(id: OccasionId): V[Option[Occasion]]
  protected def store(id: OccasionId, ev: Event[OccasionEvent]): V[Unit]
}
