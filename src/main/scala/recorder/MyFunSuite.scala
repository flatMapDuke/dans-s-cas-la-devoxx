package recorder

import org.scalatest.{Tag, FunSuite}
import java.io.File
import collection.mutable.ArrayBuffer
import org.scalatest.exceptions.TestFailedException
import annotation.switch

trait MyFunSuite extends FunSuite {


  implicit val anchorRecorder = new AnchorRecorder()


  def testPublic(testName: String)(testFun: => Unit) {
   test(testName)(testFun)
  }

}



object MyFunSuite  {

  def mergeSourceAndAnchor(source:List[(String,Int)], anchorsMessages:List[AnchorValue]): List[(String, Int, Option[String])] = {
    def anchor(line:Int, anchorsMessages:List[AnchorValue]):Option[String] = {
      val mess = anchorsMessages.filter( _.line == line).map( a => a.name + " => " + a.value).mkString("\n")
      if(mess.trim == "")None else Some(mess)
    }
    source match {
      case shead :: stail  =>
          (shead._1, shead._2, anchor(shead._2, anchorsMessages)) :: mergeSourceAndAnchor(stail, anchorsMessages)
      case _ => Nil
    }
  }

  def prettyShow(source:Array[(String,Int)], errorLine:Int, anchorsMessages:List[AnchorValue]): List[String] = {
    def intLen(i:Int) = i.toString.length

    val len:Int = 4

    def completewithspace(i:Int):String = {
      (" " * (len - intLen(i)))  + i.toString
    }
    def spacehead(s:String):String = {
      val space = "(\\s*).*".r
      s match {
        case space(spaces) => spaces
        case _ => ""
      }
    }
    mergeSourceAndAnchor(source.toList, anchorsMessages.reverse).map(
      {
        case (line, number, Some(anchor)) =>
          val prefix: String = if(number == errorLine) " ->" else "   "
          println(anchor)
          prefix + completewithspace(number) + " |" + line +  anchor.split("\n").map( i => "\n         " +spacehead(line) + i).mkString
        case (line, number, _ ) =>
          val prefix: String = if(number == errorLine) " ->" else "   "
          prefix + completewithspace(number) + " |" + line
      }
    )
  }

  def sourceProcessor(source:Array[String]):Array[(String,Int)] = {
    source.zipWithIndex.map( t => (t._1, t._2 +1))
  }


  def testBody(testName: String, testSuite: MyFunSuite, anchorRecorder: AnchorRecorder)(testFun: => Unit)(context: TestContext) {

    val suite = testSuite


    suite.testPublic(testName)({

      val testExpressionLineStart = context.testStartLine
      val testExpressionLineEnd = context.testEndLine
      lazy val content = context.source

      lazy val testSourceFile: Array[(String, Int)] = {


        MyFunSuite.sourceProcessor(content)
      }

      //lazy val fileTotalNumberLine = testSourceFile.size

      anchorRecorder.reset()


      def anchorsToMessages = {
        "\n\n" + anchorRecorder.records.map(_.toMessage).mkString("\n")
      }


      def ctx(errorLine: Int): String = {
        MyFunSuite.prettyShow(testSourceFile.drop(testExpressionLineStart - 1).take(testExpressionLineEnd - testExpressionLineStart + 2), errorLine, anchorRecorder.records).mkString("\n") // + anchorsToMessages
      }
      def errorCtx(errorLine: Int): String = {
        MyFunSuite.prettyShow(testSourceFile.drop(errorLine - 2).take(3), errorLine, anchorRecorder.records).mkString("\n")
      }

      def completeContext(errorLine: Option[Int]): String = {
        errorLine.map(i => errorCtx(i) + "\n     ...\n" + ctx(i)).getOrElse("") // + anchorsToMessages
      }

      val suitePackage = suite.getClass.getPackage.toString

      try {
        testFun
      } catch {
        case e: TestFailedException => {
          val mes = Option(e.getMessage).getOrElse("")



          val failedCtx = e.failedCodeLineNumber.map(ctx) //completeContext(e.failedCodeLineNumber)

          val location = e.failedCodeFileNameAndLineNumberString.map(suitePackage + java.io.File.separator + _)
          throw new MyTestFailedException(mes, failedCtx, e, location)

        }
        case e: NotImplementedError => {


          val mes = Option(e.getMessage).getOrElse("")
          mes match {
            case "__" =>
              val notimpl = e.getStackTrace()(2)
              val location = suitePackage + java.io.File.separator + notimpl.getFileName + ":" + notimpl.getLineNumber
              throw new MyTestPendingException(mes,
                Some(ctx(notimpl.getLineNumber))
                , e, Some(location))
            case _ =>
              val notimpl = e.getStackTrace()(1)
              val secondLocation = e.getStackTrace()(2).getLineNumber
              val location = suitePackage + java.io.File.separator + notimpl.getFileName + ":" + notimpl.getLineNumber
              throw new MyNotImplException(mes, Some("\n     ...\n" + errorCtx(notimpl.getLineNumber) + "\n     ...\n" + ctx(secondLocation)), e, Some(location))
          }
        }
        case e: Throwable => {

          val firstGoodStackTrace = e.getStackTrace.find(
            st => st.getClassName.contains(suite.getClass.getName)
          )

          val location = firstGoodStackTrace.map(st => suitePackage + java.io.File.separator + st.getFileName + ":" + st.getLineNumber)
          val failContext = firstGoodStackTrace.map(st => ctx(st.getLineNumber) + "\n\n")
          val myctx = failContext.getOrElse("") + e.getStackTrace.take(7).mkString("\n")
          val mes = e.toString + location.map("\n    at " + _)
          throw new MyException(mes, Some(myctx), e, location)

        }
      }

    })
  }
}

class TestContext( laZysource: => String , val testStartLine:Int,val testEndLine:Int)   {
  lazy val source:Array[String] = {

    val text = laZysource

    val lineBuf = new ArrayBuffer[String]()

    var charBuf = new ArrayBuffer[Char]()

    var previousChar:Char = 'a'
    import RecorderMacro._
    for (c <- text.toCharArray) {
      def closeLine(){
        lineBuf.append(charBuf.mkString)
        charBuf = new ArrayBuffer[Char]()
      }

      (c: @switch) match {
        case CR => closeLine()
        case LF => if (previousChar != CR) {closeLine()}
        case FF|SU  => closeLine()
        case _  => charBuf.append(c)
      }

      previousChar = c
    }

    lineBuf.toArray

  }
}