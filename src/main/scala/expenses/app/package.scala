package expenses

import cats.free.Free

package object app {
  type SheetOp[A] = Free[SheetOperation, A]
}
