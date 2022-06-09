package fpp.compiler.codegen

import fpp.compiler.analysis._
import fpp.compiler.ast._
import fpp.compiler.util._

/** Writes out C++ for enum definitions */
case class EnumCppWriter(
  s: CppWriterState,
  aNode: Ast.Annotated[AstNode[Ast.DefEnum]]
) extends CppWriterLineUtils {

  private val node = aNode._2

  private val data = node.data

  private val symbol = Symbol.Enum(aNode)

  private val name = s.getName(symbol)

  private val fileName = ComputeCppFiles.FileNames.getEnum(name)

  private val enumType @ Type.Enum(_, _, _) = s.a.typeMap(node.id)

  private val defaultValue = ValueCppWriter.write(s, enumType.getDefaultValue.get).
    replaceAll("^.*::", "")

  private val namespaceIdentList = s.getNamespaceIdentList(symbol)

  private val typeCppWriter = TypeCppWriter(s)

  private val repTypeName = typeCppWriter.write(enumType.repType)

  private val numConstants = data.constants.size

  /** The set of enumerated constants, expressed as a list of
   *  closed intervals. For example, the set of enumerated constants
   *  { 0, 1, 3 } yields the list { [ 0, 1 ], [ 3, 3 ] }. */
  private val intervals = {
    val values = data.constants.map(aNode => {
      val Value.EnumConstant(value, _) = s.a.valueMap(aNode._2.id)
      value._2
    }).sorted
    val state = values.foldLeft (EnumCppWriter.IntervalState()) ((s, v) => {
        s.lastInterval match {
          case None => s.copy(lastInterval = Some(v,v))
          case Some(lower, upper) =>
            if (v == upper + 1) s.copy(lastInterval = Some(lower, v))
            else s.copy(
              intervals = (lower, upper) :: s.intervals,
              lastInterval = Some(v,v)
            )
        }
    })
    (state.lastInterval.get :: state.intervals).reverse
  }

  def write: CppDoc = {
    val includeGuard = s.includeGuardFromQualifiedName(symbol, fileName)
    CppWriter.createCppDoc(
      s"$name enum",
      fileName,
      includeGuard,
      getMembers
    )
  }

  private def getMembers: List[CppDoc.Member] = {
    val hppIncludes = getHppIncludes
    val cppIncludes = getCppIncludes
    val cls = CppDoc.Member.Class(
      CppDoc.Class(
        AnnotationCppWriter.asStringOpt(aNode),
        name,
        Some("public Fw::Serializable"),
        getClassMembers
      )
    )
    List(
      List(hppIncludes, cppIncludes),
      CppWriter.wrapInNamespaces(namespaceIdentList, List(cls))
    ).flatten
  }

  private def getHppIncludes: CppDoc.Member = {
    val strings = List(
      "Fw/Types/BasicTypes.hpp",
      "Fw/Types/Serializable.hpp",
      "Fw/Types/String.hpp"
    )
    CppWriter.linesMember(
      Line.blank ::
      strings.map(CppWriter.headerString).map(line)
    )
  }

  private def getCppIncludes: CppDoc.Member = {
    val systemStrings = List("cstring", "limits")
    val strings = List(
      "Fw/Types/Assert.hpp",
      s"${s.getRelativePath(fileName).toString}.hpp"
    )
    CppWriter.linesMember(
      List(
        List(Line.blank),
        systemStrings.map(CppWriter.systemHeaderString).map(line),
        List(Line.blank),
        strings.map(CppWriter.headerString).map(line)
      ).flatten,
      CppDoc.Lines.Cpp
    )
  }

  private def getClassMembers: List[CppDoc.Class.Member] =
    List(
      getTypeMembers,
      getConstantMembers,
      getConstructorMembers,
      getOperatorMembers,
      getMemberFunctionMembers,
      getMemberVariableMembers
    ).flatten

  private def getConstantMembers: List[CppDoc.Class.Member] =
    List(
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(
          CppDocHppWriter.writeAccessTag("public") ++
          CppDocWriter.writeBannerComment("Constants") ++
          addBlankPrefix(
            wrapInEnum(
              lines(
                s"""|//! The serialized size of each enumerated constant
                    |SERIALIZED_SIZE = sizeof(SerialType),
                    |//! The number of enumerated constants
                    |NUM_CONSTANTS = $numConstants,"""
              )
            )
          )
        )
      )
    )

  private def getTypeMembers: List[CppDoc.Class.Member] = {
    List(
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(
          List(
            CppDocHppWriter.writeAccessTag("public"),
            CppDocWriter.writeBannerComment("Types"),
            lines(
              s"""|
                  |//! The serial representation type
                  |typedef $repTypeName SerialType;
                  |
                  |//! The raw enum type"""
            ),
            wrapInScope(
              "typedef enum {",
              data.constants.flatMap(aNode => {
                val node = aNode._2
                val Value.EnumConstant(value, _) = s.a.valueMap(node.id)
                val valueString = value._2.toString
                val name = node.data.name
                AnnotationCppWriter.writePreComment(aNode) ++
                lines(s"$name = $valueString,")
              }),
              "} t;"
            )
          ).flatten
        )
      )
    )
  }

  private def getConstructorMembers: List[CppDoc.Class.Member] =
    List(
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(
          CppDocHppWriter.writeAccessTag("public")
        )
      ),
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(
          CppDocWriter.writeBannerComment("Constructors"),
          CppDoc.Lines.Both
        )
      ),
      CppDoc.Class.Member.Constructor(
        CppDoc.Class.Constructor(
          Some(s"Constructor (default initialization)"),
          Nil,
          Nil,
          lines(s"this->e = $defaultValue;")
        )
      ),
      CppDoc.Class.Member.Constructor(
        CppDoc.Class.Constructor(
          Some(s"Constructor (user-provided initialization)"),
          List(
            CppDoc.Function.Param(
              CppDoc.Type("const t"),
              "e",
              Some("The enum value")
            )
          ),
          Nil,
          lines("this->e = e;")
        )
      ),
      CppDoc.Class.Member.Constructor(
        CppDoc.Class.Constructor(
          Some(s"Copy constructor"),
          List(
            CppDoc.Function.Param(
              CppDoc.Type(s"const $name&"),
              "other",
              Some("The other object")
            )
          ),
          Nil,
          lines("this->e = other.e;")
        )
      ),
    )

  private def getOperatorMembers: List[CppDoc.Class.Member] =
    List(
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(CppDocHppWriter.writeAccessTag("public"))
      ),
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(
          CppDocWriter.writeBannerComment("Operators"),
          CppDoc.Lines.Both
        )
      ),
      CppDoc.Class.Member.Function(
        CppDoc.Function(
          Some(s"Copy assignment operator (object)"),
          "operator=",
          List(
            CppDoc.Function.Param(
              CppDoc.Type(s"const $name&"),
              "other",
              Some("The other object"),
            ),
          ),
          CppDoc.Type(s"$name&"),
          List(
            line("this->e = other.e;"),
            line("return *this;"),
          )
        )
      ),
      CppDoc.Class.Member.Function(
        CppDoc.Function(
          Some(s"Copy assignment operator (raw enum)"),
          "operator=",
          List(
            CppDoc.Function.Param(
              CppDoc.Type("t"),
              "e",
              Some("The enum value"),
            ),
          ),
          CppDoc.Type(s"$name&"),
          List(
            line("this->e = e;"),
            line("return *this;"),
          )
        )
      ),
      CppDoc.Class.Member.Function(
        CppDoc.Function(
          Some(s"Conversion operator"),
          s"operator t",
          Nil,
          CppDoc.Type(""),
          List(
            line("return this->e;"),
          ),
          CppDoc.Function.NonSV,
          CppDoc.Function.Const
        )
      ),
      CppDoc.Class.Member.Function(
        CppDoc.Function(
          Some(s"Equality operator"),
          "operator==",
          List(
            CppDoc.Function.Param(
              CppDoc.Type(s"const $name&"),
              "other",
              Some("The other object"),
            ),
          ),
          CppDoc.Type("bool"),
          List(
            line("return this->e == other.e;"),
          ),
          CppDoc.Function.NonSV,
          CppDoc.Function.Const
        )
      ),
      CppDoc.Class.Member.Function(
        CppDoc.Function(
          Some(s"Inequality operator"),
          "operator!=",
          List(
            CppDoc.Function.Param(
              CppDoc.Type(s"const $name&"),
              "other",
              Some("The other object"),
            ),
          ),
          CppDoc.Type("bool"),
          List(
            line("return !(*this == other);"),
          ),
          CppDoc.Function.NonSV,
          CppDoc.Function.Const
        )
      ),
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(
          lines("\n#ifdef BUILD_UT"),
          CppDoc.Lines.Both
        )
      ),
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(
          lines(
            s"""|//! Ostream operator
                |friend std::ostream& operator<<(
                |    std::ostream& os, //!< The ostream
                |    const $name& obj //!< The object
                |);"""
            )
        )
      ),
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(
          wrapInScope(
            s"std::ostream& operator<<(std::ostream& os, const $name& obj) {",
            List(
              lines(
                s"""|os << "$name::";
                    |const $name::t e = obj.e;"""
              ),
              wrapInScope(
                "switch (e) {",
                data.constants.flatMap(aNode => {
                  val enumName = aNode._2.data.name
                  lines(
                    s"""|case $name::$enumName:
                        |  os << "$enumName";
                        |  break;"""
                  )
                }) ++
                lines(
                  """|default:
                     |  os << "[invalid]";
                     |  break;"""
                ),
                "}"
              ),
              lines(
                """|os << " (" << e << ")";
                   |return os;"""
              )
            ).flatten,
            "}"
          ),
          CppDoc.Lines.Cpp
        )
      ),
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(
          lines("#endif"),
          CppDoc.Lines.Both
        )
      ),
    )

  private def getMemberFunctionMembers: List[CppDoc.Class.Member] =
    List(
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(CppDocHppWriter.writeAccessTag("public"))
      ),
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(
          CppDocWriter.writeBannerComment("Member functions"),
          CppDoc.Lines.Both
        )
      ),
      CppDoc.Class.Member.Function(
        CppDoc.Function(
          Some(s"Check raw enum value for validity"),
          "isValid",
          Nil,
          CppDoc.Type("bool"),
          Line.addPrefixAndSuffix(
            "return ",
            writeIntervals(intervals),
            ";"
          ),
          CppDoc.Function.NonSV,
          CppDoc.Function.Const
        )
      ),
    )

  private def getMemberVariableMembers: List[CppDoc.Class.Member] =
    List(
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(CppDocHppWriter.writeAccessTag("public"))
      ),
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(
          CppDocWriter.writeBannerComment("Member variables") ++
          addBlankPrefix(
            List(
              "//! The raw enum value",
              "t e;"
            ).map(line)
          )
        )
      )
    )

  private def writeInterval(c: EnumCppWriter.Interval) = {
    val (lower, upper) = c
    s"((e >= ${lower.toString}) && (e <= ${upper.toString}))"
  }

  private def writeIntervals(cs: List[EnumCppWriter.Interval]) =
    line(writeInterval(cs.head)) ::
    cs.tail.map(c => line(s"|| ${writeInterval(c)}")).map(indentIn)

}

object EnumCppWriter {

  private type Interval = (BigInt, BigInt)

  private case class IntervalState(
    /** The current list of intervals */
    intervals: List[Interval] = Nil,
    /** The last interval computed */
    lastInterval: Option[Interval] = None,
  )

}
