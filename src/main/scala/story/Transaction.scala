package story

//import ledger._
//
//case class Transaction(details: Map[CharacterId, Entry])
//
//object Transaction {
//  def apply(mapping: (CharacterId, Entry)*): Transaction = apply(Map(mapping:_*))
//
////  def lend(lender: CharacterId, debtor: CharacterId, amount: Money): Transaction = Transaction(
////    lender -> Entry.lend(amount),
////    debtor -> Entry.borrow(amount)
////  )
////
////  def repay(debtor: CharacterId, lender: CharacterId, amount: Money): Transaction = Transaction(
////    debtor -> Entry.payback(amount),
////    lender -> Entry.receive(amount)
////  )
//
//  def split(description: Map[CharacterId, (Money, Money)]): Transaction = Transaction(
//    description.mapValues { case (paid, consumed) => Entry.pay(paid, consumed) }
//  )
//}
