package spendingsmanager
package domain
package model

import cats.Monoid
import cats.instances.map._

class DebtMap private (val debtsByPerson: Map[Person, Map[Person, Amount]]) {

}

object DebtMap {
  val empty: DebtMap = new DebtMap(Map.empty[Person, Map[Person, Amount]])

  implicit val debtMapMonoid: Monoid[DebtMap] = new Monoid[DebtMap] {
    override def empty: DebtMap = DebtMap.empty
    override def combine(x: DebtMap, y: DebtMap): DebtMap = {
      val mapMonoid = Monoid[Map[Person, Map[Person, Amount]]]
      new DebtMap(mapMonoid.combine(x.debtsByPerson, y.debtsByPerson))
    }
  }
}
