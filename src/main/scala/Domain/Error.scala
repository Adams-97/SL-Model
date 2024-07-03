package Domain

import CLI.CLIStrings._
import CLI.mandatoryCLIArg

sealed trait Error {
  /**
   * Human readable description of error
   */
  def description: ErrorMsg
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
    ErrorMsg(s"${this.getClass.getSimpleName}: Below mandatory args not present ${seqElemPrinter(argsNotPresent)}")
  }

  private def seqElemPrinter[A](seq: Set[A]): Unit = seq.foreach(println)
}

final case class ErrorMsg(value: String)
object HelpArgError {
  def apply(msg: String): HelpArgError = HelpArgError(ErrorMsg(msg))
}