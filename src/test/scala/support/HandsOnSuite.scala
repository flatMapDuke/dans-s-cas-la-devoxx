package support

import org.scalatest.{Tag, FunSuite}
import org.scalatest.{Tracker, Stopper, Reporter, FunSuite}
import org.scalatest.matchers.{Matcher, ShouldMatchers}
import org.scalatest.events.{TestPending, TestFailed, TestIgnored, Event, InfoProvided}
import org.scalatest.exceptions.{TestPendingException}

import recorder._

import language.experimental.macros
import scala.Some


trait HandsOnSuite extends MyFunSuite with ShouldMatchers {
  def __ : Matcher[Any] = {
    throw new NotImplementedError("__")
  }

  implicit val suite:MyFunSuite = this



  def anchor[A](a:A):Unit = macro RecorderMacro.anchor[A]

  def exercice(testName:String)(testFun: Unit)(implicit suite: MyFunSuite, anchorRecorder: AnchorRecorder):Unit = macro RecorderMacro.apply



  /*override protected def test(testName: String, tags: Tag*)(testFun: => Unit):Unit


  = macro RecorderMacro.apply  */

  private class ReportToTheStopper(other: Reporter) extends Reporter {
    var failed = false

    //def headerFail = "/!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\\n                 TEST FAILED                 \n/!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\"
    //def footerFail = "/!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\ /!\\"
    def headerFail =    "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n               TEST FAILED                 \n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    def footerFail =    "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    def headerPending = "*******************************************\n               TEST PENDING                \n*******************************************"
    def footerPending = "*******************************************"

    def sendInfo(header: String, suite: Option[String], test: Option[String], location: Option[String], message: Option[String], context: Option[String], footer: String) {
      header.split("\n").foreach(info(_))
      suite.collect({ case s =>
        info( "Suite    : " + s.replace("\n","") )
      })
      test.collect({ case t =>
        info( "Test     : " + t.replace("\n","") )
      })
      location.collect({ case f =>
        info( "fichier  : " + f.replace("\n","") )
      })
      message.collect({ case m =>
        info("")
        m.split("\n").foreach( info(_) )
      })
      context.collect({ case c =>
        info("")
        c.split("\n").foreach( info(_) )
      })
      info("")
      footer.split("\n").foreach(info(_))
      CustomStopper.testFailed

    }

    def apply(event: Event) {
      event match {
        case e: TestFailed => {
          e.throwable match {
	    //pour les erreurs d'assertions => sans stacktrace
            case Some(failure: MyTestFailedException) =>
              val message = Option(failure.getMessage)
              sendInfo(headerFail, Some(e.suiteName), Some(e.testName), failure.fileNameAndLineNumber, message, failure.context, footerFail)
	    //pour les __ => avec context
            case Some(pending: MyTestPendingException) =>
              sendInfo(headerPending, Some(e.suiteName), Some(e.testName), pending.fileNameAndLineNumber, Some("Vous devez remplacer les __ par les valeurs correctes"), pending.context, footerPending)
	    //pour les ??? => sans context
            case Some(pending: MyNotImplException) =>
              sendInfo(headerPending, Some(e.suiteName), Some(e.testName), pending.fileNameAndLineNumber, Some("Vous devez remplacer les ??? par les implémentations correctes"), pending.context, footerPending)
	    //pour les autres erreurs => avec stacktrace
            case Some(failure: MyException) =>
              val context:String =  failure.getStackTrace.take(5).mkString("\n")
              sendInfo(headerFail, Some(e.suiteName), Some(e.testName), failure.fileNameAndLineNumber, Option(failure.getMessage), failure.context, footerFail)
	    //ça ne devrait pas arriver
            case Some(e) =>
	      println("something went wrong")
	    //ça non plus, un TestFailed a normalement une excepetion attachée
            case None =>
              sendInfo(headerFail, Some(e.suiteName), Some(e.testName), None, None, None, footerFail)
          }
        }
        case e: TestPending => sendInfo(headerPending, Some(e.suiteName), Some(e.testName), None, Some("pending"), None, footerPending)
        case _ => other(event)
      }

    }
  }

  protected override def runTest(testName: String, reporter: Reporter, stopper: Stopper, configMap: Map[String, Any], tracker: Tracker) {
    if (!CustomStopper.oneTestFailed) {
      super.runTest(testName, new ReportToTheStopper(reporter), CustomStopper, configMap, tracker)
    }
  }


}



object HandsOnSuite {
  object partie1 extends Tag("support.partie1")
  object partie2 extends Tag("support.partie2")
}
