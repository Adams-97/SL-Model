package CLI

import org.scalatest.funspec.AnyFunSpec
import CLI.CLIArg._
import Domain.{MandatoryCLIArgsNotPresentError, NoArgumentError}

class CLIArgTest extends AnyFunSpec {
  describe("CLIArg object") {
    describe("getCLIArg") {
      it("should return empty CLIArg Seq if empty input args") {
        assert(getCLIArg(Seq[String]()) == Right(Seq[CLIArg]()))
      }

      it("should parse the strings in input Seq and give correct CLIArg") {
        val testSeq = Seq("unknown arg","unknown arg2"
                          ,"--mode","mode1arg","-m","mode2arg"
                          ,"--config","config1arg","-c","config2arg")
        val testCLIArg = getCLIArg(testSeq)

        assert(testCLIArg.map(_.collect({case unknown: unknownArg => unknown})) ==
          Right(Seq(unknownArg("unknown arg"),unknownArg("unknown arg2"))))

        assert(testCLIArg.map(_.collect({case mode: modeArg => mode})) ==
          Right(Seq(modeArg("mode1arg"),modeArg("mode2arg"))))

        assert(testCLIArg.map(_.collect({case config: configArg => config})) ==
          Right(Seq(configArg("config1arg"),configArg("config2arg"))))
      }

      it("should be able to head of empty list exception if no arg present") {
        val noArgInput = Seq("--config")
        assert(getCLIArg(noArgInput) == Left(NoArgumentError))
      }
    }

    describe("checkMandatoryArgs") {
      import CLI.CLIArg.MandatoryCLIArg
      val testMandSeq = Seq(unknownArg("unknown"),configArg("config"))

      it("should return a MandatoryCLIArgsNotPresentError if one or more mandatory arg not present") {
        assert(checkMandatoryArgs(Seq[CLIArg]()) == Left(MandatoryCLIArgsNotPresentError(MandatoryCLIArg)))
        assert(checkMandatoryArgs(testMandSeq) == Left(MandatoryCLIArgsNotPresentError(MandatoryCLIArg)))
      }

      it("should return the CLI Seq if all mandatory are present") {
        assert(checkMandatoryArgs(testMandSeq :+ modeArg("mode")) == Right(testMandSeq :+ modeArg("mode")))
      }
    }
  }
}
