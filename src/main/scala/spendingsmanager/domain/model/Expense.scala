package spendingsmanager
package domain
package model

import java.time.LocalDateTime
import cats.data.NonEmptyList

case class Expense(payer: Person, recipients: NonEmptyList[Person], amount: Amount, at: LocalDateTime)