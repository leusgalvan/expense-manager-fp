package expenses

import org.specs2.mutable.Specification

class DebtCalculatorSpec extends Specification {
  "DebtCalculator.calculateDebt" should {
    "calculate debt given one spending" in {
      val spendings = List(Spending("Carl", List("Zoe", "Gino"), 1500))
      val debtCalculator = new DebtCalculator
      val debtStore = debtCalculator.calculateDebt(spendings)
      debtStore.getDebt("Zoe", "Carl") shouldEqual 500
      debtStore.getDebt("Zoe", "Gino") shouldEqual 0
      debtStore.getDebt("Gino", "Carl") shouldEqual 500
      debtStore.getDebt("Gino", "Zoe") shouldEqual 0
      debtStore.getDebt("Carl", "Zoe") shouldEqual -500
      debtStore.getDebt("Carl", "Gino") shouldEqual -500
    }

    "calculate debt given two spendings" in {
      val spendings = List(
        Spending("Alice", List("Bob", "Chris"), 1200),
        Spending("Bob", List("Dale"), 250)
      )
      val debtCalculator = new DebtCalculator
      val debtStore = debtCalculator.calculateDebt(spendings)
      debtStore.getDebt("Alice", "Bob") shouldEqual -400
      debtStore.getDebt("Alice", "Chris") shouldEqual -400
      debtStore.getDebt("Alice", "Dale") shouldEqual 0
      debtStore.getDebt("Bob", "Alice") shouldEqual 400
      debtStore.getDebt("Bob", "Chris") shouldEqual 0
      debtStore.getDebt("Bob", "Dale") shouldEqual -125
      debtStore.getDebt("Chris", "Alice") shouldEqual 400
      debtStore.getDebt("Chris", "Bob") shouldEqual 0
      debtStore.getDebt("Chris", "Dale") shouldEqual 0
      debtStore.getDebt("Dale", "Alice") shouldEqual 0
      debtStore.getDebt("Dale", "Bob") shouldEqual 125
      debtStore.getDebt("Dale", "Chris") shouldEqual 0
    }
  }
}
