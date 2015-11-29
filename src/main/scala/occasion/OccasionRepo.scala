package occasion

import event.Event
import repo.InteractionResult

trait OccasionRepo {
  def get(id: OccasionId): InteractionResult[Option[Occasion]]
  def store(id: OccasionId, ev: Event[OccasionEvent]): InteractionResult[Unit]
}

/*
Command             Query
|                     ^
|                     |
|                     |
_                     |
write -------------- view

 */

