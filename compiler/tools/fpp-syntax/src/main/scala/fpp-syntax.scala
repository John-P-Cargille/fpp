package fpp.compiler

import fpp.compiler.ast._
import fpp.compiler.codegen._
import fpp.compiler.syntax._
import fpp.compiler.util._
import scopt.OParser

object FPPSyntax {

  case class Options(
    ast: Boolean = false,
    include: Boolean = false,
    files: List[File] = List()
  )

  def command(options: Options) = {
    Error.setTool(Tool(name))
    val files = options.files match {
      case Nil => List(File.StdIn)
      case _ => options.files
    }
    val result = Result.seq(
      Result.map(files, Parser.parseFile (Parser.transUnit) _),
      List(resolveIncludes (options) _, printAst (options) _)
    )
    result match {
      case Left(error) => {
        error.print
        System.exit(1)
      }
      case Right(_) => ()
    }
  }

  def main(args: Array[String]) = {
    val options = OParser.parse(oparser, args, Options())
    options match {
      case Some(options) => command(options)
      case None => ()
    }
  }

  def printAst(options: Options)(tul: List[Ast.TransUnit]): Result.Result[List[Ast.TransUnit]] = {
    options.ast match {
      case true => {
        val lines = tul.map(AstWriter.transUnit).flatten
        lines.map(Line.write(Line.stdout) _)
      }
      case false => ()
    }
    Right(tul)
  }

  def resolveIncludes(options: Options)(tul: List[Ast.TransUnit]): Result.Result[List[Ast.TransUnit]] = {
    options.include match {
      case true => {
        System.out.println("TODO: resolve includes")
        Right(tul)
      }
      case false => Right(tul)
    }
  }

  val builder = OParser.builder[Options]

  val name = "fpp-syntax"

  val oparser = {
    import builder._
    OParser.sequence(
      programName(name),
      head(name, "0.1"),
      opt[Unit]('a', "ast")
        .action((_, c) => c.copy(ast = true))
        .text("print the abstract syntax tree (ast)"),
      opt[Unit]('i', "include")
        .action((_, c) => c.copy(include = true))
        .text("resolve include specifiers"),
      help('h', "help").text("print this message and exit"),
      arg[String]("file ...")
        .unbounded()
        .optional()
        .action((f, c) => c.copy(files = File.fromString(f) :: c.files))
        .text("input files"),
    )
  }

}