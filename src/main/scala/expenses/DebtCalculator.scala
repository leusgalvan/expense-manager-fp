package expenses

class DebtCalculator {
  def calculateDebt(spendings: List[Spending]): DebtStore = {
    spendings.foldLeft(DebtStore.empty) { (debtByPayer, spending) =>
      val totalNumberOfPeople = 1 + spending.recipients.size
      val increaseInDebt = spending.amount / totalNumberOfPeople
      spending.recipients.foldLeft(debtByPayer) { (debtByPayer, recipient) =>
        debtByPayer.updateDebt(recipient, spending.payer, _ + increaseInDebt)
      }
    }
  }
}
