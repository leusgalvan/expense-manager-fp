package expenses

class SpendingsReader(readLine: () => String) {
  def readSpendings(): Set[Spending] = {
    def parseSpending(line: String): Spending = {
      val words = line.split(" ")
      val payer = words.head
      val owers = words.tail.init.toList
      val amount = words.last.toDouble
      Spending(payer, owers, amount)
    }
    def readLines(acc: Set[String]): Set[String] = {
      val line = readLine()
      if (line == "done") acc
      else readLines(acc + line)
    }
    val lines = readLines(Set.empty[String])
    lines.map(parseSpending)
  }
}
