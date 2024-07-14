package CLI

import org.scalatest.funspec.AnyFunSpec
import CLI.argParser.{checkForHelpArg, checkForNoArgs}
import Domain.{NoCLIArgError,HelpArgError}
import CLI.CLIStrings._

class argParserTest extends AnyFunSpec {
  describe("argParser Methods") {
    describe("checkForNoArgs") {
      val testSeq: Seq[String] = Seq("arg1","arg2","arg3")

      it("should return a NoCLIArgError if there are no args in input Seq") {
        assert(checkForNoArgs(Seq()) == Left(NoCLIArgError))
      }

      it("should return the seq if there are input arguments") {
        assert(checkForNoArgs(testSeq) == Right(testSeq))
      }

      it("should be able to handle different types of Seq") {
        assert(checkForNoArgs(List()) == Left(NoCLIArgError))
        assert(checkForNoArgs(testSeq.toList) == Right(testSeq.toList))
        assert(checkForNoArgs(Vector()) == Left(NoCLIArgError))
        assert(checkForNoArgs(testSeq.toVector) == Right(testSeq.toVector))
      }
    }

    describe("checkForHelpArg") {

      it("should return too many help if there are more than one help arg") {
        assert(checkForHelpArg(Seq("--help","--help")) == Left(HelpArgError(TOO_MANY_HELP)))
        assert(checkForHelpArg(Seq("arg2","--help","--help")) == Left(HelpArgError(TOO_MANY_HELP)))
        assert(checkForHelpArg(Seq("arg2","--help","--help","arg4")) == Left(HelpArgError(TOO_MANY_HELP)))
      }

      it("should return bad help if help arguments are not at the start") {
        assert(checkForHelpArg(Seq("arg1","--help","arg3")) == Left(HelpArgError(BAD_HELP)))
        assert(checkForHelpArg(Seq("arg1","arg2","--help")) == Left(HelpArgError(BAD_HELP)))
      }

      it("should return the args if no help") {
        val successSeq = Seq("arg1","arg2","arg3")
        assert(checkForHelpArg(successSeq) == Right(successSeq))
      }

      it("should return the help string if just help argument submitted on it's own") {
        assert(checkForHelpArg(Seq("--help")) == Left(HelpArgError(HELP_STRING)))
      }
    }

    describe("parseArgs Integration") {
      val mockInputs: Map[String,Array[String]] =  Map(
        "No Args" -> Array[String]()
        ,"Help String" -> Array("--help")
        ,"Too many help" -> Array("--help","--mode","modearg","--help")
        ,"Bad help position" -> Array("--mode","modearg","--help")
        ,"Not all mandatory args" -> Array("--config","configarg")
        ,"Unknown args" -> Array("--unknown","--mode")
      )
    }
  }

}
