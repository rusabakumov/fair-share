import ledger.operations.{AccountOperations, RecordableAccountOperations}
import ledger._

val a = CashAccount(0, 0)
val p = PayableAccount(0, 0)

RecordableAccountOperations.debit(a)(7)
RecordableAccountOperations.increase(p)(10)

val superA = SuperAccount(
  CashAccount(0, 0),
  ReceivableAccount(0, 0),
  PayableAccount(0, 0),
  GoodsAccount(0, 0)
)

val manip = for {
  l <- SuperAccount.lend(50)
  p <- SuperAccount.receive(40)
} yield l ::: p

val (manipedSuperA, _) = manip(superA)

manipedSuperA.cash.balance
manipedSuperA.receivable.balance
manipedSuperA.payable.balance
