package occasion

import account.{ Bill, Money }
import event.{ Event, Metadata, Version }
import occasion.OccasionPayload._
import participant.Participant
import util._

object OccasionCommandHandler {
  def create(id: OccasionId): Event[Occasion] = {
    val metadata = Metadata(Version.zero)

    Event(Created(id), metadata)
  }

  def changeDescription(occasion: Occasion, newDescription: String): Event[Occasion] = {
    val payload = DescriptionChanged(occasion.id, newDescription)
    val metadata = Metadata(occasion.version.next)

    Event(payload, metadata)
  }

  def mkMoneyTransfer(occasion: Occasion, from: Participant, to: Participant, money: Money): Event[Occasion] = {
    val payload = AccChangedToTransfer(occasion.id, from, to, money)
    val metadata = Metadata(occasion.version.next)

    Event(payload, metadata)
  }

  def mkSplit(occasion: Occasion, split: Map[Participant, Bill]): Event[Occasion] = {
    val payload = AccChangedToSplit(occasion.id, split)
    val metadata = Metadata(occasion.version.next)

    Event(payload, metadata)
  }
}
