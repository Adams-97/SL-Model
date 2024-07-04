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

case object ValidationError extends Error {
  def description: ErrorMsg = ErrorMsg(s"${ValidationError.productPrefix}: Not a valid instance")
}

case object NoCLIArgError extends Error {
  def description: ErrorMsg = ErrorMsg(s"${NoCLIArgError.productPrefix}: No commands or arguments passed")
}

case class HelpArgError(errMsg: ErrorMsg) extends Error {
  override def description: ErrorMsg = this.errMsg.value match {
    case HELP_STRING => ErrorMsg(HELP_STRING)
    case _ => ErrorMsg(s"${this.getClass.getSimpleName}: ${errMsg.value}")
  }
}

case class MandatoryCLIArgsNotPresentError(argsNotPresent: Set[mandatoryCLIArg]) extends Error {
  def description: ErrorMsg = {
    ErrorMsg(s"${this.getClass.getSimpleName}: Below mandatory args not present ${ElemPrinter(argsNotPresent)}")
  }
}

case class UnknownArgError(seqErr: Seq[unknownArg]) extends Error {
  def description: ErrorMsg = ErrorMsg(s"${UnknownArgError.productPrefix}: Below arguments aren't understood ${ElemPrinter(errSeq)}")
}

case object UnknownArgError {
  def getUnknownArgs(errSeq: Seq[CLIArg]): UnknownArgError = {
    UnknownArgError(errSeq.collect({ case CLIArg: unknownArg => CLIArg }))
  }

  def checkUnknownErr(CLISeq: Seq[CLIArg]): Either[UnknownArgError,Seq[CLIArg]] = {
    val UnknownSeq = getUnknownArgs(CLISeq)
    if (UnknownSeq.seqErr.isEmpty) Left(UnknownSeq)
    else Right(CLISeq)
  }
}

final case class ErrorMsg(value: String)
object HelpArgError {
  def apply(msg: String): HelpArgError = HelpArgError(ErrorMsg(msg))
}