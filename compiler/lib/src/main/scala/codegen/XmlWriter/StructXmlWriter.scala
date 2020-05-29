package fpp.compiler.codegen

import fpp.compiler.analysis._
import fpp.compiler.ast._
import fpp.compiler.codegen._
import fpp.compiler.util._

/** Write out F Prime XML for structs */
object StructXmlWriter extends AstVisitor with LineUtils {

  override def default(s: XmlWriter.State) = Nil

  override def defStructAnnotatedNode(s: XmlWriter.State, aNode: Ast.Annotated[AstNode[Ast.DefStruct]]) = {
    val node = aNode._2
    val data = node.getData
    val tags = {
      val namespace = s.getNamespace
      val pairs = List(
        if (namespace != "") Some("namespace", namespace) else None,
        Some("name", data.name)
      ).filter(_.isDefined).map(_.get)
      XmlTags.tags("serializable", pairs)
    }
    val body: List[Line] = {
      val comment = AnnotationXmlWriter.multilineComment(aNode)
      val members = {
        val tags = XmlTags.tags("members")
        val ls = data.members.map(structTypeMemberAnnotatedNode(s, _))
        XmlTags.taggedLines(tags)(ls.map(indentIn))
      }
      comment ++ members
    }
    XmlTags.taggedLines(tags)(body.map(indentIn))
  }

  def structTypeMemberAnnotatedNode(
    s: XmlWriter.State,
    aNode: Ast.Annotated[AstNode[Ast.StructTypeMember]]
  ) = {
    val node = aNode._2
    val data = node.getData
    val pairs = ("name", data.name) :: TypeXmlWriter.getPairs(s, data.typeName)
    // TODO: Add comment
    line(XmlTags.openCloseTag("member", pairs))
  }

  type In = XmlWriter.State

  type Out = List[Line]

}