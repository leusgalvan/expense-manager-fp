package expenses.app.interpreter

import cats.effect.IO
import cats.~>
import expenses.app.SheetOperation
import spendingsmanager.domain.app.{CloseSheet, CreateSheetOperation, SheetOp, SheetOperation, UpdateSheet}

trait SheetOperationsConsoleInterpreter extends SheetOperationsInterpreter {
  private def createSheet(name: String): IO[Unit] = {

  }

  val step: SheetOperation ~> IO = new (SheetOperation ~> IO) {
    override def apply[A](fa: SheetOperation[A]): IO[A] = fa match {
      case CreateSheetOperation(name) => createSheet(name)
      case UpdateSheet(sheetName) => updateSheet(name)
      case CloseSheet(sheetName) =>closeSheet(name)
    }
  }
  override def apply[A](fa: SheetOp[A]): IO[A] = fa.foldMap(step)
}
