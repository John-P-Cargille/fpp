package fpp.compiler.codegen

import fpp.compiler.analysis.*
import fpp.compiler.ast.*
import fpp.compiler.codegen
import fpp.compiler.util.*

/** Write C++ string classes of given sizees */
case class StringCppWriter(
  /** CppWriterState */
  s: CppWriterState,
  /** Enclosing class name, including any qualifiers */
  enclosingClassQualified: Option[String] = None
) extends CppWriterLineUtils {

  /** Get max string size */
  def getSize(str: Type.String): Int = str.size match {
    case Some(typeNode) => s.a.valueMap(typeNode.id) match {
      case Value.EnumConstant(value, _) => value._2.toInt
      case Value.PrimitiveInt(value, _) => value.toInt
      case Value.Integer(value) => value.toInt
      case _ => s.defaultStringSize
    }
    case None => s.defaultStringSize
  }

  /** Compute the string class name from a String type */
  def getClassName(str: Type.String): String = s"StringSize${getSize(str)}"

  /** Compute the string class name from a given size */
  def getClassName(size: Int): String = s"StringSize$size"

  /** Writes the C++ string classes */
  def write(strTypes: List[Type.String]): List[CppDoc.Member] = {
    strTypes.map(getSize).distinct.flatMap(size => {
      val name = getClassName(size)
      List(
        CppDoc.Member.Lines(
          CppDoc.Lines(
            CppDocWriter.writeBannerComment(s"$name class"),
            CppDoc.Lines.Both
          )
        ),
        CppDoc.Member.Class(
          writeClass(size, false)
        )
      )
    })
  }

  /** Writes the C++ string classes as nested classes */
  def writeNested(strTypes: List[Type.String]): List[CppDoc.Class.Member] = {
    CppDoc.Class.Member.Lines(
      CppDoc.Lines(
        CppDocHppWriter.writeAccessTag("public")
      )
    ) ::
      strTypes.map(getSize).distinct.flatMap(size => {
        val name = getClassName(size)
        List(
          CppDoc.Class.Member.Lines(
            CppDoc.Lines(
              CppDocWriter.writeBannerComment(s"$name class"),
              CppDoc.Lines.Both
            )
          ),
          CppDoc.Class.Member.Class(
            writeClass(size, true)
          )
        )
      })
  }

  def writeClass(size: Int, isNested: Boolean): CppDoc.Class = {
    CppDoc.Class(
      None,
      getClassName(size),
      Some("public Fw::StringBase"),
      getClassMembers(size, isNested)
    )
  }

  private def getClassMembers(size: Int, isNested: Boolean): List[CppDoc.Class.Member] = {
    val name = getClassName(size)
    List(
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(
          List(
            CppDocHppWriter.writeAccessTag("public"),
            addBlankPrefix(
              wrapInEnum(
                List(
                  line("//! The size of the string length plus the size of the string buffer"),
                  line(s"SERIALIZED_SIZE = sizeof(FwBuffSizeType) + $size")
                )
              )
            ),
          ).flatten
        )
      ),
      CppDoc.Class.Member.Constructor(
        CppDoc.Class.Constructor(
          Some("Default constructor"),
          Nil,
          List("StringBase()"),
          lines("this->m_buf[0] = 0;")
        )
      ),
      CppDoc.Class.Member.Constructor(
        CppDoc.Class.Constructor(
          Some("Char array constructor"),
          List(
            CppDoc.Function.Param(
              CppDoc.Type("const char*"),
              "src",
              None
            )
          ),
          List("StringBase()"),
          lines(
            "Fw::StringUtils::string_copy(this->m_buf, src, sizeof(this->m_buf));"
          )
        )
      ),
      CppDoc.Class.Member.Constructor(
        CppDoc.Class.Constructor(
          Some("String base constructor"),
          List(
            CppDoc.Function.Param(
              CppDoc.Type("const Fw::StringBase&"),
              "src",
              None
            )
          ),
          List("StringBase()"),
          lines(
            "Fw::StringUtils::string_copy(this->m_buf, src.toChar(), sizeof(this->m_buf));"
          )
        )
      ),
      CppDoc.Class.Member.Constructor(
        CppDoc.Class.Constructor(
          Some("Copy constructor"),
          List(
            CppDoc.Function.Param(
              CppDoc.Type(s"const $name&"),
              "src",
              None
            )
          ),
          List("StringBase()"),
          lines(
            "Fw::StringUtils::string_copy(this->m_buf, src.toChar(), sizeof(this->m_buf));"
          )
        )
      ),
      CppDoc.Class.Member.Destructor(
        CppDoc.Class.Destructor(
          Some("Destructor"),
          Nil
        )
      ),
      CppDoc.Class.Member.Function(
        CppDoc.Function(
          Some("Copy assignment operator"),
          "operator=",
          List(
            CppDoc.Function.Param(
              CppDoc.Type(s"const $name&"),
              "other",
              None
            )
          ),
          getCppType(s"$name&", isNested),
          List(
            wrapInIf("this == &other", lines("return *this;")),
            List(
              Line.blank,
              line("Fw::StringUtils::string_copy(this->m_buf, other.toChar(), sizeof(this->m_buf));"),
              line("return *this;"),
            ),
          ).flatten
        )
      ),
      CppDoc.Class.Member.Function(
        CppDoc.Function(
          Some("String base assignment operator"),
          "operator=",
          List(
            CppDoc.Function.Param(
              CppDoc.Type("const Fw::StringBase&"),
              "other",
              None
            )
          ),
          getCppType(s"$name&", isNested),
          List(
            wrapInIf("this == &other", lines("return *this;")),
            List(
              Line.blank,
              line("Fw::StringUtils::string_copy(this->m_buf, other.toChar(), sizeof(this->m_buf));"),
              line("return *this;"),
            ),
          ).flatten
        )
      ),
      CppDoc.Class.Member.Function(
        CppDoc.Function(
          Some("char* assignment operator"),
          "operator=",
          List(
            CppDoc.Function.Param(
              CppDoc.Type("const char*"),
              "other",
              None
            )
          ),
          getCppType(s"$name&", isNested),
          List(
            line("Fw::StringUtils::string_copy(this->m_buf, other, sizeof(this->m_buf));"),
            line("return *this;"),
          )
        )
      ),
      CppDoc.Class.Member.Function(
        CppDoc.Function(
          Some("Retrieves char buffer of string"),
          "toChar",
          Nil,
          CppDoc.Type("const char*"),
          lines("return this->m_buf;"),
          CppDoc.Function.NonSV,
          CppDoc.Function.Const
        )
      ),
      CppDoc.Class.Member.Function(
        CppDoc.Function(
          None,
          "getCapacity",
          Nil,
          CppDoc.Type("NATIVE_UINT_TYPE"),
          lines("return sizeof(this->m_buf);"),
          CppDoc.Function.NonSV,
          CppDoc.Function.Const
        )
      ),
      CppDoc.Class.Member.Lines(
        CppDoc.Lines(
          List(
            CppDocHppWriter.writeAccessTag("private"),
            addBlankPrefix(lines(
              s"char m_buf[$size]; //!< Buffer for string storage"
            )),
          ).flatten
        )
      )
    )
  }

  private def getCppType(typeName: String, isNested: Boolean) = {
    if isNested then
      CppDoc.Type(s"$typeName",
        enclosingClassQualified match {
          case Some(qualifier) => Some(s"$qualifier::$typeName")
          case None => None
        }
      )
    else
      CppDoc.Type(s"$typeName")
  }

}
