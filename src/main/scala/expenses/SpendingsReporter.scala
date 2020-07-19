package expenses

import java.util.{Currency, Locale}

import scala.util.{Properties, Try}

class SpendingsReporter {
  private def formatCurrency(value: Double): String = {
    val moneyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US)
    val color = if (value < 0) Console.GREEN else Console.RED
    color + moneyFormatter.format(math.abs(value)) + Console.RESET
  }

  def showReport(debtStore: DebtStore): String = {
    val nl = Properties.lineSeparator

    def buildDebtRow(payer: String, ower: String): String = {
      val debtAmount = debtStore.getDebt(payer, ower)
      val owe = if (debtAmount > 0) "owes" else "is owed by"
      s" $owe $ower ${formatCurrency(debtAmount)}"
    }

    def buildDebtString(payer: String): String = {
      val owers = debtStore.getOwersFor(payer)
      val rows = s"$payer:" :: owers.map(p => buildDebtRow(payer, p))
      rows.mkString(nl)
    }

    val people = debtStore.getPeople()

    people.map(buildDebtString).mkString(s"$nl$nl")
  }
}
