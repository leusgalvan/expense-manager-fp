package expenses.app

import java.time.LocalDateTime

import cats.effect.{ExitCode, IO, IOApp}
import cats._
import cats.data.{EitherT, Kleisli, NonEmptyList}
import cats.implicits._
import expenses.model.{DebtMap, Expense, Person, Sheet}
import expenses.repository.SheetRepository
import expenses.repository.interpreter.SheetRepositoryInMemoryInterpreter
import expenses.service.SheetService
import expenses.service.interpreter.SheetServiceInMemoryInterpreter

import scala.io.StdIn

object App extends IOApp {
  type ErrorOr[A] = EitherT[IO, String, A]
  type Env = SheetService[Sheet, DebtMap, Expense] with SheetRepository
  type AppOp[A] = Kleisli[ErrorOr, Env, A]

  private def readBigDecimalT(text: String): ErrorOr[BigDecimal] =
    readLine(text)
      .map(BigDecimal.apply)
      .attemptT
      .leftMap(_ => "Incorrect amount format. Please enter a decimal number.")

  private def putLine(line: String): IO[Unit] =
    IO(println(line))

  private def putLineT(line: String): ErrorOr[Unit] =
    EitherT.right(putLine(line))

  private def readLine(text: String): IO[String] =
    putLine(text) >> IO(StdIn.readLine())

  private def readLineT(text: String): ErrorOr[String] =
    EitherT.right(readLine(text))

  private def readOption(): AppOp[Int] = Kleisli { _ =>
    EitherT.right(
      readLine("Your choice: ")
        .map(_.toInt)
        .handleErrorWith { _ =>
          putLine("Invalid option. Try again.") >> readOption()
        }
    )
  }
  private def displayOptions(): AppOp[Unit] = Kleisli { _ =>
    putLineT(
      """Please select an option:
        |
        |(1) Start a new sheet
        |(2) Add expense to existing sheet
        |(3) Close existing sheet
        |(4) Exit
        |
        |""".stripMargin)
  }

  private def startNewSheet: AppOp[Boolean] = Kleisli { (env: Env) =>
    for {
      sheetName <- EitherT.right(readLine("Enter sheet name: "))
      _ <- env.create(sheetName)
    } yield false
  }

  private def readRecipientsNames(): ErrorOr[NonEmptyList[String]] = {
    def loop(acc: List[String]): ErrorOr[NonEmptyList[String]] =
      readLineT("").flatMap { name =>
        if(name.trim.toLowerCase == "done") EitherT.cond(acc.nonEmpty, acc.toNel.get, "Please enter at least one name")
        else loop(name :: acc)
    }
    loop(List[String]())
  }


  private def addExpense(): AppOp[Boolean] = Kleisli { (env: Env) =>
    for {
      sheetName <- readLineT("Enter sheet name: ")
      payer <- readLineT("Enter the name of the person who pays: ")
      amount <- readBigDecimalT("Enter the amount: ")
      _ <- putLineT("Enter, one per line, the names of the people involved in the expense. When done, enter the word 'done'")
      recipients <- readRecipientsNames()
      expense = Expense(Person(payer), recipients.map(Person), amount, LocalDateTime.now())
      _ <- env.addExpense(sheetName, expense)
    } yield false
  }

  private def closeSheet(): AppOp[Boolean] = Kleisli { (env: Env) =>
    for {
      sheetName <- readLineT("Enter sheet name: ")
      _ <- env.close(sheetName)
    } yield false
  }

  private def exit(): AppOp[Boolean] = Kleisli(_ => EitherT.rightT(true))

  private def executeOption(option: Int): AppOp[Boolean] = option match {
    case 1 => startNewSheet()
    case 2 => addExpense()
    case 3 => closeSheet()
    case 4 => exit()
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val env: Env = new SheetServiceInMemoryInterpreter with SheetRepositoryInMemoryInterpreter
    val program: AppOp[ExitCode] = for {
      _ <- displayOptions()
      selectedOption <- readOption()
      shouldExit <- executeOption(selectedOption)
      _ <- if(shouldExit) IO.unit else run(args)
    } yield ExitCode.Success
    program.handleErrorWith(ex => IO {
      println(ex)
      ExitCode.Error
    })
  }
}
