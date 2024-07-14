package Domain

import org.scalatest.funspec.AnyFunSpec
import Domain.UnknownArgError._
import CLI._

class ErrorTest extends AnyFunSpec {
  describe("UnknownArgError") {

    it("accurately collect unknown args and only return seq if non present") {
      val noUnknownArgs = Seq(modeArg(""),configArg(""))
      assert(checkUnknownErr(noUnknownArgs) == Right(noUnknownArgs))

      val UnknownArgs = Seq(unknownArg("unknown1"), unknownArg("unknown2"))
      assert(checkUnknownErr(noUnknownArgs ++ UnknownArgs) == Left(UnknownArgError(UnknownArgs)))
    }
  }
}
