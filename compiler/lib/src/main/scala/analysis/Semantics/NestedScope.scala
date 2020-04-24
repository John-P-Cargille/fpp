package fpp.compiler.analysis

import fpp.compiler.ast._
import fpp.compiler.util._

/** A stack of scopes */
sealed trait NestedScope {

  /** Push a new scope onto the stack */
  def push(scope: Scope): NestedScope

  /** Pop a scope off the stack */
  def pop: NestedScope

  /** Put a name and symbol into the map. */
  def put (nameGroup: NameGroup) (name: Name.Unqualified, symbol: Symbol): Result.Result[NestedScope]

  /** Get a symbol from the map. Throw an InternalError if the name is not there.*/
  def get (nameGroup: NameGroup) (name: Name.Unqualified): Symbol

  /** Get a symbol from the map. Return none if the name is not there. */
  def getOpt (nameGroup: NameGroup) (name: Name.Unqualified): Option[Symbol]

}

object NestedScope {

  /** Create an empty NestedScope */
  def empty: NestedScope = NestedScopeImpl()

}

private case class NestedScopeImpl(scopes: List[Scope] = List(Scope.empty))
  extends NestedScope
{

  def splitScopes = scopes match {
    case head :: tail => (head, tail)
    case _ => throw new InternalError("empty scope stack")
  }

  def push(scope: Scope) = NestedScopeImpl(scope :: this.scopes)

  def pop = {
    val (_, tail) = splitScopes
    NestedScopeImpl(tail)
  }

  def put (nameGroup: NameGroup) (name: Name.Unqualified, symbol: Symbol) = {
    val (head, tail) = splitScopes
    for (scope <- head.put(nameGroup)(name, symbol)) 
      yield NestedScopeImpl(scope :: tail)
  }

  def get (nameGroup: NameGroup) (name: Name.Unqualified) = getOpt(nameGroup)(name) match {
    case Some(symbol) => symbol
    case _ => throw new InternalError(s"could not find symbol for name ${name}")
  }

  def getOpt (nameGroup: NameGroup) (name: Name.Unqualified) = {
    def helper(scopes: List[Scope]): Option[Symbol] =
      scopes match {
        case Nil => None
        case head :: tail => head.getOpt(nameGroup)(name) match {
          case s @ Some(_) => s
          case None => helper(tail)
        }
      }
    helper(this.scopes)
  }

}