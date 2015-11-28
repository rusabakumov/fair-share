package occasion

import event.Event

trait OccasionRepo {
  def get(id: OccasionId): Option[Occasion]
  def store(id: OccasionId, ev: Event[OccasionEvent]): Boolean
}
