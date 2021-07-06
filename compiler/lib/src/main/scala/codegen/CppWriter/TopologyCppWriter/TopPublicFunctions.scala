package fpp.compiler.codegen

import fpp.compiler.analysis._
import fpp.compiler.ast._
import fpp.compiler.util._

/** Writes out C++ for topology public functions */
case class TopPublicFunctions(
  s: CppWriterState,
  aNode: Ast.Annotated[AstNode[Ast.DefTopology]],
  privateFns: Set[String]
) extends TopologyCppWriterUtils(s, aNode) {

  def getMembers: List[CppDoc.Member] = List(
    getBannerComment,
    getSetupFn,
    getTeardownFn
  )

  private val params = List(
    CppDoc.Function.Param(
      CppDoc.Type("const TopologyState&"),
      "state",
      Some("The topology state")
    )
  )

  private def getBannerComment = CppDoc.Member.Lines(
    CppDoc.Lines(
      CppDocWriter.writeBannerComment("Public interface functions"),
      CppDoc.Lines.Both
    )
  )

  private def writeFnCall(pair: (String, String)): List[Line] = {
    val (name, argument) = pair
    if (privateFns.contains(name))
      lines(s"$name($argument);")
    else Nil
  }

  private def getSetupFn = CppDoc.Member.Function(
    CppDoc.Function(
      Some("Set up the topology"),
      "setup",
      params,
      CppDoc.Type("void"),
      List(
        ("initComponents", "state"),
        ("configComponents", "state"),
        ("setBaseIds", ""),
        ("connectComponents", ""),
        ("regCommands", ""),
        ("loadParameters", ""),
        ("startTasks", "state"),
      ).flatMap(writeFnCall)
    )
  )

  private def getTeardownFn = CppDoc.Member.Function(
    CppDoc.Function(
      Some("Tear down the topology"),
      "teardown",
      params,
      CppDoc.Type("void"),
      List(
        ("stopTasks", "state"),
        ("freeThreads", "state"),
        ("tearDownComponents" ,"state"),
      ).flatMap(writeFnCall)
    )
  )

}
