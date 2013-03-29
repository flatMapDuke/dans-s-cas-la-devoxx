package support

import scala.language.reflectiveCalls
import org.scalatest.events.Event
import org.scalatest.Stopper
import org.scalatest.events.{TestPending, TestFailed, TestIgnored, Event, InfoProvided}


object CustomStopper extends Stopper{
  var oneTestFailed = false
  override def apply() = oneTestFailed

  def testFailed : Unit = {
    oneTestFailed = true
    ()
    //meditationMessage(event)
  }

/*  private def meditationMessage(event: Event) = {
    event match {
      case e: TestIgnored => "Ce n'est pas bien d'ignorer le test «"+ e.testName + "»     \nde la suite «" + e.suiteName + "»     "
      case e: TestFailed => s"Le test «${e.testName}»     \nde la suite «${e.suiteName}» a echoue:     "
      case e: TestPending => s"Le test «${e.testName}»     \nde la suite «${e.suiteName}» est en attente d'une implémentation     "
      case _ => ""
    }
  }
*/
}