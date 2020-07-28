package expenses.app.interpreter

import cats.effect.IO
import spendingsmanager.domain.app.SheetOp

trait SheetOperationsInterpreter {
  def apply[A](operation: SheetOp[A]): IO[A]
}
