package spendingsmanager
package domain
package repository

import cats.data.EitherT
import cats.effect.IO
import spendingsmanager.domain.model.Sheet

trait SheetRepository {
  type SheetOp[A] = EitherT[IO, String, A]

  def get(name: String): SheetOp[Sheet]
  def store(sheet: Sheet): SheetOp[Unit]
}
