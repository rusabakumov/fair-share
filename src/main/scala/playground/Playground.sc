import ledger._
import ledger.operations.SuperAccountOperations

val sa = SuperAccount.empty

// Let's construct a state transition
val tr = for {
  b <- SuperAccount.borrow(50)
  l <- SuperAccount.lend(10)
} yield b ::: l

// This is just a description of transition, in order to change account state we need to
// actually apply the monad
tr(sa)

// Here we're getting all the change records, converts them into state monads,
// and combines them
val sc = tr(sa)._2.map(a => SuperAccountOperations.op(a.side, a.label, a.amount)).reduce(
  (s1, s2) => for {
    r1 <- s1
    r2 <- s2
  } yield r1 ::: r2
)

// Here we're applying the new state monad to the account and results match with our
// manually constructed state monad `tr`.
sc(sa)
