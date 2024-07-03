package CLI

import scala.annotation.tailrec
import Domain.MandatoryCLIArgsNotPresentError

sealed trait CLIArg
sealed trait mandatoryCLIArg extends CLIArg
sealed trait optionalCLIArg extends CLIArg
case object helpArg extends optionalCLIArg
final case class unknownArg(unknownVal: String) extends CLIArg
final case class configArg(configPath: String) extends optionalCLIArg
final case class modeArg(mode: String) extends mandatoryCLIArg

object CLIArg {
  @tailrec
  def getCLIArg(args: Seq[String], CLIArgs: Seq[CLIArg] = Seq[CLIArg]): Seq[CLIArg] = {

    if (args.isEmpty) CLIArgs
    else {
      args.head match {
        case "-c" | "--config" => getCLIArg(args.drop(2), CLIArgs :+ configArg(args.tail.head))
        case "-m" | "--mode" => getCLIArg(args.drop(2), CLIArgs :+ modeArg(args.tail.head))
        case unknownVal => getCLIArg(args.tail, CLIArgs :+ unknownArg(args.head))
      }
    }
  }
  }
}