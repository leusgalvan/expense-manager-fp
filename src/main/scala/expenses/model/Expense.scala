package expenses.model

import java.time.LocalDateTime

import cats.data.NonEmptyList
import expenses.Amount

case class Expense(payer: Person, recipients: NonEmptyList[Person], amount: Amount, at: LocalDateTime)