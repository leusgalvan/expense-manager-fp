package expenses

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class SpendingsAppSpec extends Specification with Mockito {
  "SpendingsApp.run" should {
    "read spendings and print the report with the results" in {
      val spendings = Set(Spending("Viviana", List("Roberto"), 1000))
      val spendingsReader = mock[SpendingsReader]
      spendingsReader.readSpendings() returns spendings

      val debtStore = DebtStore.empty + Debt("Roberto", "Viviana", 500)
      val debtCalculator = mock[DebtCalculator]
      debtCalculator.calculateDebt(spendings.toList) returns debtStore

      val spendingsReporter = mock[SpendingsReporter]

      SpendingsApp.run(spendingsReader, debtCalculator, spendingsReporter)
      there was one(spendingsReader).readSpendings()
      there was one(debtCalculator).calculateDebt(spendings.toList)
      there was one(spendingsReporter).showReport(debtStore)
    }
  }
}
