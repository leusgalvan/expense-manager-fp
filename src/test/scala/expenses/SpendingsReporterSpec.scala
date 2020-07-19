package expenses

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class SpendingsReporterSpec extends Specification with Mockito {
  def red(s: String) = Console.RED + s + Console.RESET
  def green(s: String) = Console.GREEN + s + Console.RESET

  "Expense report" should {
    "show a basic report" in {
      val debtStore = DebtStore.empty +
        Debt("Alice", "Bob", 24.0) +
        Debt("Bob", "Chris", 25.30)
      val expenseReport = new SpendingsReporter
      val expectedReport =
        s"""Alice:
          | owes Bob ${red("$24.00")}
          |
          |Bob:
          | is owed by Alice ${green("$24.00")}
          | owes Chris ${red("$25.30")}
          |
          |Chris:
          | is owed by Bob ${green("$25.30")}""".stripMargin
      expenseReport.showReport(debtStore) shouldEqual expectedReport
    }
  }
}
