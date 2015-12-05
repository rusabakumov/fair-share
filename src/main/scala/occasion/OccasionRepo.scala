package occasion

import event.Event
import util._

import scalaz.{ Show, \/, Kleisli }
import scalaz.syntax.either._
import scalaz.std.java.throwable._

trait OccasionRepo {
  def get: Kleisli[V, OccasionId, Occasion] = Kleisli[V, OccasionId, Occasion] { id =>
    val result = get(id).fold(
      Show[Throwable].shows(_).left[Occasion],
      {
        case None => s"Occasion $id does not exist.".left[Occasion]
        case Some(occasion) => occasion.right[String]
      }
    )

    result
  }

  def store: Kleisli[V, (OccasionId, Event[OccasionEvent]), Unit] = Kleisli[V, (OccasionId, Event[OccasionEvent]), Unit] {
    case (id, event) =>
      store(id, event)
  }

  protected def get(id: OccasionId): Throwable \/ Option[Occasion]
  protected def store(id: OccasionId, ev: Event[OccasionEvent]): V[Unit]
}
