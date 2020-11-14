package fpp.compiler.codegen

import fpp.compiler.ast._
import fpp.compiler.util._

/** Write out an FPP AST */
object AstWriter extends AstVisitor with LineUtils {

  def transUnit(tu: Ast.TransUnit): Out = transUnit((), tu)

  override def defAbsTypeAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.DefAbsType]]) = {
    val (_, node, _) = an
    lines("def abs type") ++ ident(node.data.name).map(indentIn)
  }

  override def defArrayAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.DefArray]]) = {
    val (_, node, _) = an
    val da = node.data
    lines("def array") ++
    List(
      ident(da.name),
      addPrefix("size", exprNode) (da.size),
      typeNameNode(da.eltType),
      linesOpt(addPrefix("default", exprNode), da.default),
      linesOpt(addPrefix("format", applyToData(string)), da.format)
    ).flatten.map(indentIn)
  }

  override def defComponentAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.DefComponent]]) = {
    val (_, node, _) = an
    val dc = node.data
    val kind = dc.kind.toString
    lines("def component") ++
    (
      lines("kind " ++ kind) ++ 
      ident(dc.name) ++ 
      dc.members.map(componentMember).flatten
    ).map(indentIn)
  }

  override def defComponentInstanceAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.DefComponentInstance]]) = {
    val (_, node, _) = an
    val dci = node.data
    lines("def component instance") ++
    List(
      ident(dci.name),
      addPrefix("component", qualIdent) (dci.component.data),
      addPrefix("base id", exprNode) (dci.baseId),
      addPrefix("queue size", exprNode) (dci.baseId),
      addPrefix("stack size", exprNode) (dci.baseId),
      addPrefix("priority", exprNode) (dci.baseId),
    ).flatten.map(indentIn)
  }

  override def defConstantAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.DefConstant]]) = {
    val (_, node, _) = an
    val dc = node.data
    lines("def constant") ++
    (ident(dc.name) ++ exprNode(dc.value)).map(indentIn)
  }

  override def defEnumAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.DefEnum]]) = {
    val (_, node, _) = an
    val de = node.data
    lines("def enum") ++
    List(
      ident(de.name),
      linesOpt(typeNameNode, de.typeName),
      de.constants.map(annotateNode(defEnumConstant)).flatten
    ).flatten.map(indentIn)
  }

  override def defModuleAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.DefModule]]) = {
    val (_, node, _) = an
    val dm = node.data
    lines("def module") ++
    (ident(dm.name) ++ dm.members.map(moduleMember).flatten).map(indentIn)
  }

  override def defPortAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.DefPort]]) = {
    val (_, node, _) = an
    val dp = node.data
    lines("def port") ++
    List(
      ident(dp.name),
      formalParamList(dp.params),
      linesOpt(addPrefix("return", typeNameNode), dp.returnType)
    ).flatten.map(indentIn)
  }

  override def defStructAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.DefStruct]]) = {
    val (_, node, _) = an
    val ds = node.data
    lines("def struct") ++ 
    (
      ident(ds.name) ++
      ds.members.map(annotateNode(structTypeMember)).flatten ++ 
      linesOpt(exprNode, ds.default)
    ).map(indentIn) 
  }

  override def defTopologyAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.DefTopology]]) = {
    val (_, node, _) = an
    val dt = node.data
    lines("def topology") ++
    (ident(dt.name) ++ dt.members.map(topologyMember).flatten).map(indentIn)
  }

  override def default(in: Unit) = throw new InternalError("AstWriter: Visitor not implemented")

  override def exprArrayNode(in: Unit, node: AstNode[Ast.Expr], e: Ast.ExprArray) =
    lines("expr array") ++
    e.elts.map(exprNode).flatten.map(indentIn)

  override def exprBinopNode(in: Unit, node: AstNode[Ast.Expr], e: Ast.ExprBinop) =
    lines("expr binop") ++
    (exprNode(e.e1) ++ binop(e.op) ++ exprNode(e.e2)).map(indentIn)
  
  override def exprDotNode(in: Unit, node: AstNode[Ast.Expr], e: Ast.ExprDot) =
    lines("expr dot") ++
    (exprNode(e.e) ++ ident(e.id.data)).map(indentIn)

  override def exprIdentNode(in: Unit, node: AstNode[Ast.Expr], e: Ast.ExprIdent) = 
    ident(e.value)

  override def exprLiteralBoolNode(in: Unit, node: AstNode[Ast.Expr], e: Ast.ExprLiteralBool) = {
    val s = e.value match {
      case Ast.LiteralBool.True => "true"
      case Ast.LiteralBool.False => "false"
    }
    lines("literal bool " ++ s)
  }

  override def exprLiteralFloatNode(in: Unit, node: AstNode[Ast.Expr], e: Ast.ExprLiteralFloat) =
    lines("literal float " ++ e.value)
  
  override def exprLiteralIntNode(in: Unit, node: AstNode[Ast.Expr], e: Ast.ExprLiteralInt) =
    lines("literal int " ++ e.value)

  override def exprLiteralStringNode(in: Unit, node: AstNode[Ast.Expr], e: Ast.ExprLiteralString) =
    addPrefix("literal string", string) (e.value)

  override def exprParenNode(in: Unit, node: AstNode[Ast.Expr], e: Ast.ExprParen) =
    lines("expr paren") ++
    exprNode(e.e).map(indentIn)

  override def exprStructNode(in: Unit, node: AstNode[Ast.Expr], e: Ast.ExprStruct) =
    lines("expr struct") ++
    e.members.map(applyToData(structMember)).flatten.map(indentIn)

  override def exprUnopNode(in: Unit, node: AstNode[Ast.Expr], e: Ast.ExprUnop) =
    lines("expr unop") ++
    (unop(e.op) ++ exprNode(e.e)).map(indentIn)

  override def specCommandAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.SpecCommand]]) = {
    val (_, node, _) = an
    val sc = node.data
    lines("spec command") ++
    List(
      lines(s"kind ${sc.kind.toString}"),
      addPrefix("name", ident) (sc.name),
      formalParamList(sc.params),
      linesOpt(addPrefix("opcode", exprNode), sc.opcode),
      linesOpt(addPrefix("priority", exprNode), sc.priority),
      linesOpt(applyToData(queueFull), sc.queueFull)
    ).flatten.map(indentIn)
  }

  override def specCompInstanceAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.SpecCompInstance]]) =  {
    val (_, node, _) = an
    val ci = node.data
    lines("spec comp instance") ++ (
      lines(visibility(ci.visibility)) ++
      qualIdent(ci.instance.data)
    ).map(indentIn)
  }

  override def specConnectionGraphAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.SpecConnectionGraph]]) =  {
    def direct(g: Ast.SpecConnectionGraph.Direct) = {
      def connection(c: Ast.SpecConnectionGraph.Connection) = {
        lines("connection") ++ (
          addPrefix("from port", portInstanceIdentifier) (c.fromPort.data) ++
          linesOpt(addPrefix("index", exprNode), c.fromIndex) ++
          addPrefix("to port", portInstanceIdentifier) (c.toPort.data) ++
          linesOpt(addPrefix("index", exprNode), c.toIndex)
        ).map(indentIn)
      }
      lines("spec connection graph direct") ++ (
        ident(g.name) ++
        g.connections.map(connection).flatten
      ).map(indentIn)
    }
    def pattern(g: Ast.SpecConnectionGraph.Pattern) = {
      def target(qid: AstNode[Ast.QualIdent]) = addPrefix("target", qualIdent) (qid.data)
      lines("spec connection graph pattern") ++ (
        addPrefix("source", qualIdent) (g.source.data) ++
        g.targets.map(target).flatten ++
        addPrefix("pattern", exprNode) (g.pattern)
      ).map(indentIn)
    }
    val (_, node, _) = an
    node.data match {
      case g : Ast.SpecConnectionGraph.Direct => direct(g)
      case g : Ast.SpecConnectionGraph.Pattern => pattern(g)
    }
  }

  override def specEventAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.SpecEvent]]) = {
    val (_, node, _) = an
    val se = node.data
    lines("spec event") ++
    List(
      ident(se.name),
      formalParamList(se.params),
      lines(s"severity ${se.severity.toString}"),
      linesOpt(addPrefix("id", exprNode), se.id),
      linesOpt(addPrefix("format", applyToData(string)), se.format),
      linesOpt(addPrefix("throttle", exprNode), se.throttle),
    ).flatten.map(indentIn)
  }

  override def specIncludeAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.SpecInclude]]) =  {
    val (_, node, _) = an
    val si = node.data
    lines("spec include") ++ fileString(si.file.data).map(indentIn)
  }

  override def specInitAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.SpecInit]]) = {
    val (_, node, _) = an
    val si = node.data
    lines("spec init") ++
    List(
      addPrefix("instance", qualIdent) (si.instance.data),
      addPrefix("phase", exprNode) (si.phase),
      addPrefix("code", string) (si.code)
    ).flatten.map(indentIn)
  }

  override def specInternalPortAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.SpecInternalPort]]) = {
    val (_, node, _) = an
    val sip = node.data
    lines("spec internal port") ++
    List(
      ident(sip.name),
      formalParamList(sip.params),
      linesOpt(addPrefix("priority", exprNode), sip.priority),
      linesOpt(queueFull, sip.queueFull)
    ).flatten.map(indentIn)
  }

  override def specLocAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.SpecLoc]]) = {
    val (_, node, _) = an
    val sl = node.data
    val kind = sl.kind.toString
    lines("spec loc") ++
    (
      lines("kind " ++ kind) ++
      addPrefix("symbol", qualIdent) (sl.symbol.data) ++ 
      fileString(sl.file.data)
    ).map(indentIn)
  }

  override def specParamAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.SpecParam]]) = {
    val (_, node, _) = an
    val sp = node.data
    lines("spec param") ++
    List(
      ident(sp.name),
      typeNameNode(sp.typeName),
      linesOpt(addPrefix("default", exprNode), sp.default),
      linesOpt(addPrefix("id", exprNode), sp.id),
      linesOpt(addPrefix("set opcode", exprNode), sp.setOpcode),
      linesOpt(addPrefix("save opcode", exprNode), sp.saveOpcode),
    ).flatten.map(indentIn)
  }

  override def specPortInstanceAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.SpecPortInstance]]) = {
    val (_, node, _) = an
    def general(i: Ast.SpecPortInstance.General) = {
      val kind = lines(s"kind ${i.kind.toString}")
      lines("spec port instance general") ++
      List(
        kind,
        ident(i.name),
        linesOpt(addPrefix("array size", exprNode), i.size),
        linesOpt(addPrefix("port type", applyToData(qualIdent)), i.port),
        linesOpt(addPrefix("priority", exprNode), i.priority),
        linesOpt(applyToData(queueFull), i.queueFull)
      ).flatten.map(indentIn)
    }
    def special(i: Ast.SpecPortInstance.Special) = {
      val kind = lines(s"kind ${i.kind.toString}")
      lines("spec port instance special") ++
      (kind ++ ident(i.name)).map(indentIn)
    }
    node.data match {
      case i : Ast.SpecPortInstance.General => general(i)
      case i : Ast.SpecPortInstance.Special => special(i)
    }
  }

  override def specTlmChannelAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.SpecTlmChannel]]) = {
    val (_, node, _) = an
    def update(u: Ast.SpecTlmChannel.Update) =
      lines(s"update ${u.toString}")
    def kind(k: Ast.SpecTlmChannel.LimitKind) =
      lines(k.toString)
    def limit(l: Ast.SpecTlmChannel.Limit) = {
      val (k, en) = l
      lines("limit") ++ (
        kind(k.data) ++
        exprNode(en)
      ).map(indentIn)
    }
    def limits(name: String, ls: List[Ast.SpecTlmChannel.Limit]) =
      ls.map(addPrefixNoIndent(name, limit))
    val tc = node.data
    lines("spec tlm channel") ++
    List(
      ident(tc.name),
      typeNameNode(tc.typeName),
      linesOpt(addPrefix("id", exprNode), tc.id),
      linesOpt(update, tc.update),
      linesOpt(addPrefix("format", applyToData(string)), tc.format),
      limits("low", tc.low).flatten,
      limits("high", tc.high).flatten,
    ).flatten.map(indentIn)
  }

  override def specTopImportAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.SpecTopImport]]) =  {
    val (_, node, _) = an
    val ti = node.data
    lines("spec top import") ++
    qualIdent(ti.top.data).map(indentIn)
  }

  override def specUnusedPortsAnnotatedNode(in: Unit, an: Ast.Annotated[AstNode[Ast.SpecUnusedPorts]]) =  {
    val (_, node, _) = an
    val up = node.data
    lines("spec unused ports") ++
    up.ports.map(applyToData(portInstanceIdentifier)).flatten.map(indentIn)
  }

  override def transUnit(in: Unit, tu: Ast.TransUnit) = tu.members.map(tuMember).flatten

  override def typeNameBoolNode(in: Unit, node: AstNode[Ast.TypeName]) = lines("bool")

  override def typeNameFloatNode(in: Unit, node: AstNode[Ast.TypeName], tn: Ast.TypeNameFloat) =
    lines(tn.name.toString)

  override def typeNameIntNode(in: Unit, node: AstNode[Ast.TypeName], tn: Ast.TypeNameInt) =
    lines(tn.name.toString)

  override def typeNameQualIdentNode(in: Unit, node: AstNode[Ast.TypeName], tn: Ast.TypeNameQualIdent) = 
    qualIdent(tn.name.data)

  override def typeNameStringNode(in: Unit, node: AstNode[Ast.TypeName], tn: Ast.TypeNameString) =
    lines("string") ++ linesOpt(addPrefix("size", exprNode), tn.size).map(indentIn)

  private def addPrefixNoIndent[T](s: String, f: T => List[Line]): T => List[Line] =
    (t: T) => Line.joinLists (Line.NoIndent) (lines(s)) (" ") (f(t))

  private def addPrefix[T](s: String, f: T => List[Line]): T => List[Line] =
    (t: T) => Line.joinLists (Line.Indent) (lines(s)) (" ") (f(t))

  private def annotate(pre: List[String], lines: List[Line], post: List[String]) = {
    def preLine(s: String) = line("@ " ++ s)
    val pre1 = pre.map(preLine)
    def postLine(s: String) = line("@< " ++ s)
    val post1 = post.map(postLine)
    pre1 ++ lines ++ post1
  }

  private def annotateNode[T](f: T => List[Line]): Ast.Annotated[AstNode[T]] => List[Line] =
    (ana: Ast.Annotated[AstNode[T]]) => {
      val (a1, node, a2) = ana
      annotate(a1, f(node.data), a2)
    }

  private def applyToData[A,B](f: A => B): AstNode[A] => B = 
    (a: AstNode[A]) => f(a.data)

  private def binop(op: Ast.Binop) = lines(s"binop ${op.toString}")

  private def componentMember(member: Ast.ComponentMember) = {
    val (a1, _, a2) = member.node
    val l = matchComponentMember((), member)
    annotate(a1, l, a2)
  }

  private def defEnumConstant(dec: Ast.DefEnumConstant) =
    lines("def enum constant") ++
    List(
      ident(dec.name),
      linesOpt(exprNode, dec.value)
    ).flatten.map(indentIn)

  private def exprNode(node: AstNode[Ast.Expr]): List[Line] = matchExprNode((), node)

  private def fileString(s: String) = lines("file " ++ s)

  private def formalParam(fp: Ast.FormalParam) = {
    def kind(k: Ast.FormalParam.Kind) = {
      val s = k match {
        case Ast.FormalParam.Ref => "ref"
        case Ast.FormalParam.Value => "value"
      }
      "kind " ++ s
    }
    lines("formal param") ++
    List(
      lines(kind(fp.kind)),
      ident(fp.name),
      typeNameNode(fp.typeName),
    ).flatten.map(indentIn)
  }

  private def formalParamList(params: Ast.FormalParamList) =
    params.map(annotateNode(formalParam)).flatten

  private def ident(s: String) = lines("ident " ++ s)

  private def moduleMember(member: Ast.ModuleMember) = {
    val (a1, _, a2) = member.node
    val l = matchModuleMember((), member)
    annotate(a1, l, a2)
  }

  private def portInstanceIdentifier(piid: Ast.PortInstanceIdentifier): List[Line] = {
    val qid = Ast.QualIdent.Qualified(piid.componentInstance, piid.portName)
    qualIdent(qid)
  }

  private def qualIdent(qid: Ast.QualIdent): List[Line] =
    lines("qual ident " ++ qualIdentString(qid))

  private def qualIdentString(qid: Ast.QualIdent): String =
    qid match {
      case Ast.QualIdent.Unqualified(name) => name
      case Ast.QualIdent.Qualified(qualifier, name) => 
        qualIdentString(qualifier.data) ++ "." ++ name.data
    }

  private def queueFull(qf: Ast.QueueFull) = {
    val s = qf.toString
    lines(s"queue full $s")
  }

  private def string(s: String) = s.split('\n').map(line).toList

  private def structMember(sm: Ast.StructMember) =
    lines("struct member") ++ 
    (ident(sm.name) ++ exprNode(sm.value)).map(indentIn)

  private def structTypeMember(stm: Ast.StructTypeMember) = {
    lines("struct type member") ++ 
    List(
      ident(stm.name),
      typeNameNode(stm.typeName),
      linesOpt(addPrefix("format", applyToData(string)), stm.format)
    ).flatten.map(indentIn)
  }

  private def todo = lines("TODO")

  private def topologyMember(tm: Ast.TopologyMember) = {
    val l = matchTopologyMember((), tm)
    val (a1, _, a2) = tm.node
    annotate(a1, l, a2)
  }

  private def tuMember(tum: Ast.TUMember) = moduleMember(tum)

  private def typeNameNode(node: AstNode[Ast.TypeName]) =
    addPrefix("type name", matchTypeNameNode((), _)) (node)

  private def unop(op: Ast.Unop) = lines(s"unop ${op.toString}")

  private def visibility(v: Ast.Visibility) = v.toString

  type In = Unit

  type Out = List[Line]

}
