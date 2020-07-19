package expenses

case class Spending(payer: String, recipients: List[String], amount: Double)
