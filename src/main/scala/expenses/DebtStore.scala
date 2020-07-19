package expenses

trait DebtStore {
  def getPeople(): List[String]
  def getOwersFor(payer: String): List[String]
  def getDebt(payer: String, ower: String): Double
  def updateDebt(payer: String,
                 ower: String,
                 update: Double => Double): DebtStore
  def +(debt: Debt): DebtStore
}

object DebtStore {
  def empty: DebtStore = new ListDebtStore(Nil)
}

case class Debt(payer: String, ower: String, amount: Double)
class ListDebtStore(private val debts: List[Debt]) extends DebtStore {
  override def getPeople(): List[String] =
    debts.flatMap(d => List(d.ower, d.payer)).distinct.sorted

  override def getOwersFor(payer: String): List[String] = {
    getPeople().filter(_ != payer).filter(ower => getDebt(payer, ower) != 0)
  }

  override def getDebt(payer: String, ower: String): Double = {
    def getDebtFor(payer: String, ower: String): Double =
      debts.filter(d => d.payer == payer && d.ower == ower).foldLeft(0d) {
        (acc, debt) =>
          acc + debt.amount
      }
    getDebtFor(payer, ower) - getDebtFor(ower, payer)
  }

  override def updateDebt(payer: String,
                          ower: String,
                          update: Double => Double): DebtStore = {
    val currentDebt = getDebt(payer, ower)
    val newDebt = update(currentDebt)
    val debtDiff = newDebt - currentDebt
    new ListDebtStore(Debt(payer, ower, debtDiff) :: debts)
  }

  override def +(debt: Debt): DebtStore = new ListDebtStore(debt :: debts)
}

class MapDebtStore(
  private val debtByPerson: Map[String, Map[String, Double]] = Map.empty
) extends DebtStore {
  def getPeople(): List[String] = {
    val payers = debtByPerson.keySet
    val owers = payers.flatMap(
      p => debtByPerson.get(p).map(_.keySet).getOrElse(Set.empty[String])
    )
    val allPeople = payers ++ owers
    allPeople.toList.sorted
  }
  def getOwersFor(payer: String): List[String] = {
    getPeople().filter(_ != payer).filter(ower => getDebt(payer, ower) != 0)
  }
  def getDebt(payer: String, ower: String): Double = {
    def getDebtOpt(payer: String, ower: String): Option[Double] =
      debtByPerson
        .get(payer)
        .flatMap(_.get(ower))
    getDebtOpt(payer, ower).getOrElse(-getDebtOpt(ower, payer).getOrElse(0d))
  }

  def updateDebt(payer: String,
                 ower: String,
                 update: Double => Double): MapDebtStore = {
    val debtByOwer: Map[String, Double] =
      debtByPerson.getOrElse(payer, Map.empty)
    val currentDebt = debtByOwer.getOrElse(ower, 0d)
    val newDebt = update(currentDebt)
    val newDebtByOwer = debtByOwer + (ower -> newDebt)
    val newDebtByPerson = debtByPerson + (payer -> newDebtByOwer)
    new MapDebtStore(newDebtByPerson)
  }

  override def +(debt: Debt): DebtStore =
    updateDebt(debt.payer, debt.ower, _ + debt.amount)

  override def toString(): String = debtByPerson.toString()

  def canEqual(other: Any): Boolean = other.isInstanceOf[MapDebtStore]

  override def equals(other: Any): Boolean = other match {
    case that: MapDebtStore =>
      (that canEqual this) &&
        debtByPerson == that.debtByPerson
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(debtByPerson)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
