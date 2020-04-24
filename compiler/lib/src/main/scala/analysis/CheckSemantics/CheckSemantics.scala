package fpp.compiler.analysis

import fpp.compiler.ast._
import fpp.compiler.util._
import fpp.compiler.transform._

/** Check semantics for a list of translation units */
object CheckSemantics {

  def tuList(a: Analysis, tul: List[Ast.TransUnit]): Result.Result[Analysis] = {
    for {
      pair <- ResolveSpecInclude.transformList(
        a,
        tul, 
        ResolveSpecInclude.transUnit
      )
      a <- Right(pair._1)
      tul <- Right(pair._2)
      // TODO: Enter symbols
      // TODO: Check uses
      // TODO: Check use-def cycles
      // TODO: Check types (phase 1)
      // TODO: Evaluate constants
      // TODO: Check types (phase 2)
      // TODO: Check port definitions
      // TODO: Check component definitions
      // TODO: Check component instance definitions
      // TODO: Check topology definitions
      // TODO: Check init specifiers
      // TODO: Check location specifiers
    }
    yield a
  }

}