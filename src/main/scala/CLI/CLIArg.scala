package CLI

sealed trait CLIArg
sealed trait mandatoryCLIArg extends CLIArg
sealed trait optionalCLIArg extends CLIArg
case object helpArg extends optionalCLIArg
final case class unknownArg(unknownVal: String) extends CLIArg
final case class configArg(configPath: String) extends optionalCLIArg
final case class modeArg(mode: Int) extends mandatoryCLIArg

object CLIArg {
  def getCLIArg(s: String): CLIArg = s match {
    case "--help" => helpArg
    case "-c" => ???
    case "--config" => ???
    case "-m" => ???
    case "--mode" => ???
  }
}