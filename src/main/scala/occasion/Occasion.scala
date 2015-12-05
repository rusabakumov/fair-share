package occasion

import java.util.UUID

import account.Account
import event.{ Event, Version }
import participant.ParticipantId

case class OccasionId(id: UUID) extends AnyVal

case class Occasion(
  id: OccasionId,
  version: Version,
  description: Option[String],
  accounts: Map[ParticipantId, Account]
)

object Occasion {
  def empty = Occasion(OccasionId(UUID.randomUUID()), Version.zero, None, Map())

  def foldLeft(events: List[Event[OccasionEvent]]): Occasion = {
    events.foldLeft(Occasion.empty) { (occasion, event) =>
      val Event(payload, metadata) = event

      import OccasionEvent._

      payload match {
        case Created(id) => occasion.copy(id = id, version = metadata.version)
        case DescriptionChanged(description) => occasion.copy(description = Some(description), version = metadata.version)
        case AccChangedToTransfer(from, to, money) =>
          val fromAccount = Account.empty.give(money)
          val toAccount = Account.empty.receive(money)
          val newAccounts = Map(
            from.id -> fromAccount,
            to.id -> toAccount
          )

          occasion.copy(accounts = newAccounts)
        case AccChangedToSplit(split) =>
          val newAccounts = split.map {
            case (participant, bill) =>
              participant.id -> Account.empty.pay(bill)
          }

          occasion.copy(accounts = newAccounts)
      }
    }
  }
}
