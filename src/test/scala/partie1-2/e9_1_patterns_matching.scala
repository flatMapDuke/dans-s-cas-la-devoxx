package pas_suivant

import support.HandsOnSuite

/**
* On passe aux patterns et au pattern matching
*/

class e9_1_patterns_matching extends HandsOnSuite {
 
 /*  
 *   C’est une généralisation du switch, rencontré dans les langages de programmation Java ou C, 
 *   au hiérachie de classes. 
 *   Côté syntaxe, on utilise le mot-clé match puis case pour distinguer
 *   les différents patterns. 
 *
 *       e match {case p1 => e1 ... case pn => en }, 
 *
 *   où pi représente les patterns et ei la valeur renvoyée dans le cas où le pattern pi match e.
 *
 *   'match' est une expression, donc retourne toujours une valeur. Dans le cas où aucun des 
 *   patterns ne match, l’exception MatchError est renvoyée. 
 *
 */
 /**
  * le pattern matching peut être utilisé sur des chaines
  */
  exercice("le pattern matching peut être utilisé comme un switch/case") {
    val string="B"

    val actual = "B" match {
      case "A" => "stringA"
      case "B" => "stringB"
      case "C" => "stringC"
      // la notation “_” désigne tout ce qui n’a pas besoin d’être nommé
      // Le cas case _ permet de gérer tous les autres cas, sans en laisser passer à l’attrape
      case _ => "DEFAULT"
    }

    (actual) should be (__)

    val nextActual = "E" match {
      case "A" => "stringA"
      case "B" => "stringB"
      case "C" => "stringC"
      case _ => "DEFAULT"
    }

    (nextActual) should be (__)
  }

  /**
  * le pattern matching peut être utilisé sur des types
  */
  exercice("le pattern matching peut être utilisé sur des types") {
    sealed trait Root
    class A(val a:String = "A") extends Root
    class B(val b:String = "B") extends Root
    class C(val c:String = "C") extends Root

    val value:Root=new B()

    val actual = value match {
      case matchedA:A => "string"+matchedA.a
      case matchedB:B => "string"+matchedB.b
      case matchedC:C => "string"+matchedC.c
      case _ => "DEFAULT"
    }

    (actual) should be (__)
  }

  
  exercice("Il n’est pas obligatoire de capturer toutes les valeurs") {
    case class A(val a:String, val b:String)
    val a:A = new A(a="string", b="B")

    val actual = a match {
      case A(a,_) => a
      case _ => "DEFAULT"
    }

    (actual) should be (__)
  }

  exercice("On peut imbriquer des patterns") {
    case class A(val a:String, val b:String)
    case class B(val a:A)

    val a:A = new A(a="string", b="B")
    val b:B = new B(a)

    val actual = b match {
      case B(A(_,b)) => b
      case _ => "DEFAULT"
    }

    (actual) should be (__)
  }

  exercice("Les listes ont différents patterns") {
    val s = Seq("a","b")
    val actual = s match {
      case Seq("a","b") => "ok"
      case _ => "DEFAULT"
    }

    (actual) should be (__)

    val consActual = s match {
      case "a"::Nil => "ko"
      case "a"::"b"::Nil => "ok"
      case head::tail => head
      case _ => "DEFAULT"
    }

    (consActual) should be (__)
  }
}
