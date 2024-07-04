
import CLI._
import Domain.Error

object Main extends App {
  println("Reading Arguments")
  val parsedArgs = argParser.parseArgs(args)

  val CLIMsg = parsedArgs match {
    case Left(err: Error) => err.description.toString
    case Right(CLIs: Seq[CLIArg]) => "Arguments read, program starting."
  }

}
