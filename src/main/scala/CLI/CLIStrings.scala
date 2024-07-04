package CLI


/**
 * Contains all the String to print out when certain CLI args are inputted correctly or incorrectly
 */
object CLIStrings {

  val HELP_STRING =
    """
      |Usage: Calculates financial model for personal finances involving student loans and house purchases
      | example: eg [--help] [-c]
      | options:
      |   --help: Prints out help output seen currently.
      |   [-c, --config]: Provide JSON config file location. Defaults to src/main/resources.
      |   -m, --mode: Select which mode you want. (1= ,2= ,3= )
      |""".stripMargin

  val TOO_MANY_HELP = "Invalid arguments, help must only go at the start"

  val BAD_HELP = "Help option must me only argument at the start"
}
