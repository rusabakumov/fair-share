package occasion

import account.{ Bill, Money }
import event.{ Event, Metadata, Version }
import occasion.OccasionEvent._
import participant.Participant

object OccasionOps {
  def create(id: OccasionId): Event[OccasionEvent] = {
    val metadata = Metadata(Version.zero)

    Event(Created(id), metadata)
  }

  def changeDescription(occasion: Occasion, newDescription: String): Event[OccasionEvent] = {
    val payload = DescriptionChanged(newDescription)
    val metadata = Metadata(occasion.version.next)

    Event(payload, metadata)
  }

  def mkMoneyTransfer(occasion: Occasion, from: Participant, to: Participant, money: Money): Event[OccasionEvent] = {
    val payload = AccChangedToTransfer(from, to, money)
    val metadata = Metadata(occasion.version.next)

    Event(payload, metadata)
  }

  def mkSplit(occasion: Occasion, split: Map[Participant, Bill]): Event[OccasionEvent] = {
    val payload = AccChangedToSplit(split)
    val metadata = Metadata(occasion.version.next)

    Event(payload, metadata)
  }
}
