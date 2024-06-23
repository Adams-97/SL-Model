package CLI

import Domain._
import CLI.CLIStrings._


object argParser {

  def parseArgs(arrOfArgs: Array[String]): Either[Error,List[Option[CLIArg]]]= {
    val listArgs = checkForNoArgs(arrOfArgs.toList)
    val helpcheck = listArgs.flatMap(checkForHelpArg)
  }


  def checkForNoArgs(listOfArgs: List[String]): Either[NoCLIArgError.type,List[String]] =
    if (listOfArgs.isEmpty) Left(NoCLIArgError)
    else Right(listOfArgs)

  def checkForHelpArg(listOfArgs: List[String]): Either[HelpArgError,List[String]] = {
    val helpArgsWithIndex = listOfArgs.zipWithIndex.filter(_._1 == "--help")

    helpArgsWithIndex match {
      case _ if helpArgsWithIndex.length > 1 => Left(HelpArgError(TOO_MANY_HELP))
      case _ if helpArgsWithIndex.head._2 != 0 => Left(HelpArgError(BAD_HELP))
      case _ if helpArgsWithIndex.isEmpty => Right(listOfArgs)
      case _ => Left(HelpArgError(HELP_STRING))
    }
  }
}
