package occasion

import account.{ Bill, Money }
import event.Payload
import participant.Participant
import util._

sealed trait OccasionPayload extends Payload[Occasion]

object OccasionPayload {
  case class Created(id: OccasionId) extends OccasionPayload

  case class DescriptionChanged(id: OccasionId, description: String) extends OccasionPayload

  case class AccChangedToTransfer(id: OccasionId, from: Participant, to: Participant, money: Money) extends OccasionPayload

  case class AccChangedToSplit(id: OccasionId, split: Map[Participant, Bill]) extends OccasionPayload
}

