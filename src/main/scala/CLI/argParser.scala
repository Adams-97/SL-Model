package CLI

import Domain._
import CLI.CLIStrings._
import CLI.CLIArg.getCLIArg

object argParser {

  def parseArgs(arrOfArgs: Array[String]): Either[Error,Seq[CLIArg]]= {
    val listArgs = checkForNoArgs(arrOfArgs.toVector)
    val helpcheck = listArgs.flatMap(checkForHelpArg)
  }


  def checkForNoArgs[A](args: Seq[A]): Either[NoCLIArgError.type,Seq[A]] =
    if (args.isEmpty) Left(NoCLIArgError)
    else Right(args)

  def checkForHelpArg[A](args: Seq[String]): Either[HelpArgError,Seq[String]] = {
    val helpArgsWithIndex = args.zipWithIndex.filter(_._1 == "--help")

    helpArgsWithIndex match {
      case _ if helpArgsWithIndex.length > 1 => Left(HelpArgError(TOO_MANY_HELP))
      case _ if helpArgsWithIndex.head._2 != 0 => Left(HelpArgError(BAD_HELP))
      case _ if helpArgsWithIndex.isEmpty => Right(args)
      case _ => Left(HelpArgError(HELP_STRING))
    }
  }
}
