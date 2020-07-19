package spendingsmanager.domain.app

import cats.effect.{ExitCode, IO, IOApp}

object App extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    for {
      selectedOption <- displayOptions()
      _ <- executeOption(selectedOption)
    }
  }
}
