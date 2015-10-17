package ledger

import ledger.Account.{Goods, Payable, Receivable, Cash}
import ledger.Record.{Debit, Credit}
import org.scalatest.{Matchers, FunSpec}

class EntryTest extends FunSpec with Matchers {

  describe("pay") {
    it("when overpaid: debits goods, debits receivable, credits cash") {
      val actual = Entry.pay(100, 70)
      val expected = Entry(Debit(Goods, 70), Debit(Receivable, 30), Credit(Cash, 100))
      actual shouldEqual expected
    }

    it("when underpaid: debits goods, credits payable, credits cash") {
      val actual = Entry.pay(100, 190)
      val expected = Entry(Debit(Goods, 190), Credit(Payable, 90), Credit(Cash, 100))
      actual shouldEqual expected
    }
  }

  describe("lend") {
    it("credits cash and debits receivable") {
      val actual = Entry.lend(5)
      val expected = Entry(Debit(Receivable, 5), Credit(Cash, 5))
      actual shouldEqual expected
    }
  }

  describe("borrow") {
    it("debits cash and credits payable") {
      val actual = Entry.borrow(9)
      val expected = Entry(Debit(Cash, 9), Credit(Payable, 9))
      actual shouldEqual expected
    }
  }

  describe("payback") {
    it("debits payable and credits cash") {
      val actual = Entry.payback(42)
      val expected = Entry(Debit(Payable, 42), Credit(Cash, 42))
      actual shouldEqual expected
    }
  }

  describe("receive") {
    it("debits cash and credits receivable") {
      val actual = Entry.receive(2)
      val expected = Entry(Debit(Cash, 2), Credit(Receivable, 2))
      actual shouldEqual expected
    }
  }

}
