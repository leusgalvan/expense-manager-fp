package spendingsmanager.domain.repository.interpreter

import spendingsmanager.domain.model.Sheet
import spendingsmanager.domain.repository.SheetRepository
import cats._
import cats.implicits._
import cats.data._
import cats.effect.IO

trait SheetRepositoryInMemoryInterpreter extends SheetRepository {
  var sheetsByName: Map[String, Sheet] = Map.empty[String, Sheet]

  def get(name: String): SheetOp[Sheet] = {
    EitherT(IO(sheetsByName.get(name).toRight(s"Sheet not found. Name: $name")))
  }

  def store(sheet: Sheet): SheetOp[Unit] = {
    EitherT(IO(Right{ sheetsByName = sheetsByName + (sheet.name -> sheet) }))
  }
}
object SheetRepositoryInMemoryInterpreter extends SheetRepositoryInMemoryInterpreter