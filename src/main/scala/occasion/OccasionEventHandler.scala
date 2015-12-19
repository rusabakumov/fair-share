package occasion

import account.Account
import event.{ Event, EventHandler }

class OccasionEventHandler extends EventHandler[Occasion] {
  def handle(agg: Occasion, event: Event[Occasion]): Occasion = {
    val Event(payload, metadata) = event

    import OccasionPayload._

    payload match {
      case Created(id) => agg.copy(id = id, version = metadata.version)
      case DescriptionChanged(id, description) => agg.copy(description = Some(description), version = metadata.version)
      case AccChangedToTransfer(id, from, to, money) =>
        val fromAccount = Account.empty.give(money)
        val toAccount = Account.empty.receive(money)
        val newAccounts = Map(
          from.id -> fromAccount,
          to.id -> toAccount
        )

        agg.copy(accounts = newAccounts)
      case AccChangedToSplit(id, split) =>
        val newAccounts = split.map {
          case (participant, bill) =>
            participant.id -> Account.empty.pay(bill)
        }

        agg.copy(accounts = newAccounts)
    }
  }
}
