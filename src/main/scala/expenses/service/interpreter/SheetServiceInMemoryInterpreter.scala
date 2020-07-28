package expenses.service.interpreter

import cats.data.Kleisli
import expenses.model.{DebtMap, Expense, Sheet}
import expenses.repository.SheetRepository
import expenses.service.SheetService

trait SheetServiceInMemoryInterpreter extends SheetService[Sheet, DebtMap, Expense] {
  def create(sheetName: String): SheetOp[Sheet] = Kleisli { (sheetRepository: SheetRepository) =>
    val sheet = Sheet.empty(sheetName)
    sheetRepository.store(sheet).map(_ => sheet)
  }

  def close(sheetName: String): SheetOp[Sheet] = Kleisli { (sheetRepository: SheetRepository) =>
    for {
      sheet <- sheetRepository.get(sheetName)
      closedSheet = sheet.closed
      _ <- sheetRepository.store(closedSheet)
    } yield closedSheet
  }

  def addExpense(sheetName: String, expense: Expense): SheetOp[Sheet] = Kleisli { (sheetRepository: SheetRepository) =>
    for {
      sheet <- sheetRepository.get(sheetName)
      updatedSheet = sheet.withExpense(expense)
      _ <- sheetRepository.store(updatedSheet)
    } yield updatedSheet
  }

  def computeDebtMap(sheetName: String): SheetOp[DebtMap] = ???

  /*def computeDebtMap(sheetName: String): SheetOp[DebtMap] = Kleisli { (sheetRepository: SheetRepository) =>
    for {
      sheet <- sheetRepository.get(sheetName)
      _ <- sheet.expenses.fold
    } yield updatedSheet
  }*/
}