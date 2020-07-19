package expenses

import org.specs2.mutable.Specification

class SpendingsReaderSpec extends Specification {
  "SpendingReader.readSpendings" should {
    "read a list of spendings" in {
      var n = 0
      val readLine = () => {
        n += 1
        if (n == 1)
          "alice peter bob 200"
        else if (n == 2)
          "john alice bob 300"
        else
          "done"
      }
      val spendingReader = new SpendingsReader(readLine)
      val spendings = spendingReader.readSpendings()
      spendings shouldEqual Set(
        Spending("alice", List("peter", "bob"), 200),
        Spending("john", List("alice", "bob"), 300)
      )
    }
  }
}
