package Domain

import CLI.CLIStrings._
import CLI.{mandatoryCLIArg,unknownArg}
import CLI.CLIArg

sealed trait Error {
  /**
   * Human readable description of error
   */
  def description: ErrorMsg

  def ElemPrinter[A](iter: Iterable[A]): Unit = iter.foreach(println)
}
sealed trait CLIError extends Error

case object ValidationError extends Error {
  def description: ErrorMsg = ErrorMsg(s"${ValidationError.productPrefix}: Not a valid instance")
}

case object NoCLIArgError extends CLIError {
  def description: ErrorMsg = ErrorMsg(s"${NoCLIArgError.productPrefix}: No commands or arguments passed")
}

case class HelpArgError(errMsg: ErrorMsg) extends CLIError {
  override def description: ErrorMsg = this.errMsg.value match {
    case HELP_STRING => ErrorMsg(HELP_STRING)
    case _ => ErrorMsg(s"${this.getClass.getSimpleName}: ${errMsg.value}")
  }
}

case object NoArgumentError extends CLIError {
  def description: ErrorMsg = ErrorMsg(s"${NoCLIArgError.productPrefix}: One or more arguments that require inputs have non")
}

case class MandatoryCLIArgsNotPresentError(argsNotPresent: Set[mandatoryCLIArg]) extends CLIError {
  def description: ErrorMsg = {
    ErrorMsg(s"${this.getClass.getSimpleName}: Below mandatory args not present ${ElemPrinter(argsNotPresent)}")
  }
}

case class UnknownArgError(seqErr: Seq[unknownArg]) extends CLIError {
  def description: ErrorMsg = ErrorMsg(s"${UnknownArgError.productPrefix}: Below arguments aren't understood ${ElemPrinter(seqErr)}")
}

case object UnknownArgError {
  def getUnknownArgs(errSeq: Seq[CLIArg]): UnknownArgError = {
    UnknownArgError(errSeq.collect({case cliArg: unknownArg => cliArg}))
  }

  def checkUnknownErr(CLISeq: Seq[CLIArg]): Either[UnknownArgError,Seq[CLIArg]] = {
    val UnknownSeq = getUnknownArgs(CLISeq)
    if (UnknownSeq.seqErr.isEmpty) Right(CLISeq)
    else Left(UnknownSeq)
  }
}

final case class ErrorMsg(value: String)
object HelpArgError {
  def apply(msg: String): HelpArgError = HelpArgError(ErrorMsg(msg))
}