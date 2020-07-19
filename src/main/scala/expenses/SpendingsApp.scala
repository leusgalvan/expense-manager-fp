package expenses

object SpendingsApp {
  def main(args: Array[String]): Unit = {
    val spendingsReader = new SpendingsReader(scala.io.StdIn.readLine)
    val debtCalculator = new DebtCalculator
    val spendingsReporter = new SpendingsReporter
    run(spendingsReader, debtCalculator, spendingsReporter)
  }

  def run(spendingsReader: SpendingsReader,
          debtCalculator: DebtCalculator,
          spendingsReporter: SpendingsReporter) = {
    val spendings = spendingsReader.readSpendings()
    val debtStore = debtCalculator.calculateDebt(spendings.toList)
    val report = spendingsReporter.showReport(debtStore)
    println(report)
  }
}
