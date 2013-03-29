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

    def headerFail =    "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n               TEST FAILED                 \n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    def footerFail =    "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    def headerPending = "*******************************************\n               TEST PENDING                \n*******************************************"
    def footerPending = "*******************************************"

    def sendInfo(header: String, suite: String, test: String, location: Option[String], message: Option[String], context: Option[String], footer: String) {
      header.split("\n").foreach(info(_))

      info( "Suite    : " + suite.replace("\n","") )

      info( "Test     : " + test.replace("\n","") )

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

    def sendFail(e:MyException, suite:String, test:String) = {
      sendInfo(headerFail
          , suite
          , test
          , e.fileNameAndLineNumber
          , Option(e.getMessage)
          , e.context
          , footerFail
        )
    }

    def sendPending(e:MyException, suite:String, test:String, mess:Option[String]) = {
      sendInfo(headerPending
          , suite
          , test
          , e.fileNameAndLineNumber
          , mess
          , e.context
          , footerPending
        )
    }

    def apply(event: Event) {
      event match {
        case e: TestFailed => {
          e.throwable match {
      //pour les erreurs d'assertions => sans stacktrace
            case Some(failure: MyTestFailedException) =>
              sendFail(failure, e.suiteName, e.testName)
      //pour les __ => avec context
            case Some(pending: MyTestPendingException) =>
              sendPending(pending, e.suiteName, e.testName, Some("Vous devez remplacer les __ par les valeurs correctes"))
      //pour les ??? => sans context
            case Some(pending: MyNotImplException) =>
              sendPending(pending, e.suiteName, e.testName, Some("Vous devez remplacer les ??? par les implémentations correctes"))
      //pour les autres erreurs => avec stacktrace
            case Some(failure: MyException) =>
              sendFail(failure, e.suiteName, e.testName)
      //ça ne devrait pas arriver
            case Some(e) =>
              println("something went wrong")
      //ça non plus, un TestFailed a normalement une excepetion attachée
            case None =>
              sendInfo(headerFail
                , e.suiteName
                , e.testName
                , None
                , None
                , None
                ,
                footerFail
              )
          }
        }
        case e: TestPending =>
          sendInfo(headerPending
            , e.suiteName
            , e.testName
            , None
            , Some("pending")
            , None
            , footerPending)
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
