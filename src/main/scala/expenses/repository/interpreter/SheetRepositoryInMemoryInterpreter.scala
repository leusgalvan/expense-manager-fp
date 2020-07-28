package expenses.repository.interpreter

import cats.data._
import cats.effect.IO
import expenses.model.Sheet
import expenses.repository.SheetRepository

trait SheetRepositoryInMemoryInterpreter extends SheetRepository {
  var sheetsByName: Map[String, Sheet] = Map.empty[String, Sheet]

  def get(name: String): SheetOp[Sheet] = {
    EitherT(IO(sheetsByName.get(name).toRight(s"Sheet not found. Name: $name")))
  }

  def store(sheet: Sheet): SheetOp[Unit] = {
    EitherT(IO(Right{ sheetsByName = sheetsByName + (sheet.name -> sheet) }))
  }
}