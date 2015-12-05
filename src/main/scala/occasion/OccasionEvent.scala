package occasion

import account.{ Bill, Money }
import participant.Participant

sealed trait OccasionEvent

object OccasionEvent {
  case class Created(id: OccasionId) extends OccasionEvent

  case class DescriptionChanged(description: String) extends OccasionEvent

  case class AccChangedToTransfer(from: Participant, to: Participant, money: Money) extends OccasionEvent

  case class AccChangedToSplit(split: Map[Participant, Bill]) extends OccasionEvent
}

