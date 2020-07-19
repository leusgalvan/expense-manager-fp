package spendingsmanager
package domain
package service

import cats.data.{EitherT, Kleisli}
import cats.effect.IO
import spendingsmanager.domain.repository.SheetRepository

trait SheetService[Sheet, DebtMap, Expense] {
  type ErrorOr[A] = EitherT[IO, String, A]
  type SheetOp[A] = Kleisli[ErrorOr, SheetRepository, A]

  def create(name: String): SheetOp[Sheet]
  def close(name: String): SheetOp[Sheet]
  def addExpense(sheet: Sheet, expense: Expense): SheetOp[Sheet]
  def computeDebtMap(sheet: Sheet): SheetOp[DebtMap]
}
