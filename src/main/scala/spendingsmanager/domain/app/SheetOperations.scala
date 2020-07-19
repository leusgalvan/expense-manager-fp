package spendingsmanager
package domain
package app

import cats.free.Free.liftF

trait SheetOperation[A]
case class CreateSheetOperation(name: String) extends SheetOperation[Unit]
case class UpdateSheet(sheetName: String) extends SheetOperation[Unit]
case class CloseSheet(sheetName: String) extends SheetOperation[Unit]

trait SheetOperations {
  def createSheet(name: String): SheetOp[Unit] =
    liftF(CreateSheetOperation(name))

  def updateSheet(sheetName: String): SheetOp[Unit] =
    liftF(UpdateSheet(sheetName))

  def closeSheet(sheetName: String): SheetOp[Unit] =
    liftF(CloseSheet(sheetName))
}
object SheetOperations extends SheetOperations
