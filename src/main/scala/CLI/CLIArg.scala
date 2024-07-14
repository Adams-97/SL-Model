package CLI

import scala.annotation.tailrec
import Domain.{MandatoryCLIArgsNotPresentError, NoArgumentError}

import scala.util.{Failure, Success, Try}

sealed trait CLIArg
sealed trait mandatoryCLIArg extends CLIArg
sealed trait optionalCLIArg extends CLIArg
sealed trait requiresCLIArg
final case class unknownArg(unknownVal: String) extends CLIArg
final case class configArg(configPath: String) extends optionalCLIArg with requiresCLIArg
final case class modeArg(mode: String) extends mandatoryCLIArg with requiresCLIArg

object CLIArg {
  def getCLIArg(args: Seq[String], CLIArgs: Seq[CLIArg] = Seq()): Either[NoArgumentError.type,Seq[CLIArg]] = {

    @tailrec
    def inner(argInput: Seq[String], CLIInput: Seq[CLIArg] = Seq()): Seq[CLIArg] = {
      if (argInput.isEmpty) CLIInput
      else {
        argInput.head match {
          case "-c" | "--config" => inner(argInput.drop(2), CLIInput :+ configArg(argInput.tail.head))
          case "-m" | "--mode" => inner(argInput.drop(2), CLIInput :+ modeArg(argInput.tail.head))
          case unknownVal => inner(argInput.tail, CLIInput :+ unknownArg(argInput.head))
        }
      }
    }

    Try(inner(args)) match {
      case Failure(noElementException) => Left(NoArgumentError)
      case Success(value) => Right(value)
    }
  }

  implicit val MandatoryCLIArg: Set[mandatoryCLIArg] = Set(modeArg(""))
  def checkMandatoryArgs(CLIArgs: Seq[CLIArg])(implicit MandatoryCLIArg: Set[mandatoryCLIArg]):
  Either[MandatoryCLIArgsNotPresentError,Seq[CLIArg]] = {

    @tailrec
    def convertCLIToSet(arg: Seq[CLIArg]
                        ,mandArgSet: Set[mandatoryCLIArg] = Set[mandatoryCLIArg]()): Set[mandatoryCLIArg] = {
      if (arg.isEmpty) mandArgSet
      else
        arg.head match {
          case elem: modeArg => convertCLIToSet(arg.tail,mandArgSet + modeArg(""))
          case _ => convertCLIToSet(arg.tail, mandArgSet)
        }
    }
    val crossReferencedSeq = MandatoryCLIArg.diff(convertCLIToSet(CLIArgs))

    if (crossReferencedSeq.isEmpty) Right(CLIArgs)
    else Left(MandatoryCLIArgsNotPresentError(crossReferencedSeq))
  }
}