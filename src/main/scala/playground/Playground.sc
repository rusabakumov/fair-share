import ledger._

val sa = SuperAccount.empty

val complexOp = for {
  b <- SuperAccount.borrow(50)
  l <- SuperAccount.payback(20)
} yield b ::: l

val (newSA, records) = complexOp(sa)

newSA // should have 30 in cash and 30 in payable
records // should have 4 records
