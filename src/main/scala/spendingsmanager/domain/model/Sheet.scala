package spendingsmanager
package domain
package model

import java.time.LocalDateTime

trait SheetStatus
case object Open extends SheetStatus
case object Closed extends SheetStatus

case class Sheet(name: String, status: SheetStatus, expenses: List[Expense], closedAt: Option[LocalDateTime]) {
  def closed: Sheet = copy(status = Closed)
  def withExpense(expense: Expense): Sheet = copy(expenses = expense :: expenses)
}
object Sheet {
  def empty(name: String): Sheet = Sheet(name, Open, Nil, None)
}
