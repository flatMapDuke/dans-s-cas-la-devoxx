package pas_suivant

import support.HandsOnSuite

/**
* On passe aux patterns et au pattern matching
*/

class e9_extracteurs_et_patterns extends HandsOnSuite {
 /**
  * Un extracteur est l’inverse d’un constructeur.
  * On définit un extracteur en positionnant une méthode unapply sur
  * l’objet compagnon d’un type.
  *
  * Un objet compagnon est un singleton portant le même nom que la classe,
  * et peut être considéré comme une boite à outils statiques d’une classe.
  */
  exercice("Un extracteur est le contraire d’un constructeur") {
    class Email(val value:String)
    object Email { def unapply(email:Email):Option[String]=Option(email.value)}

    val mailstring = "foo@bar.com"
    val email = new Email(mailstring)



    val Email(extractedString) = email
    // Ce que le compilateur fait :  
    // val extractedString = Email.unapply(email).get

    (extractedString == mailstring) should be(__)
  }

 /**
  * Les extracteurs fonctionnent avec plusieurs valeurs
  */
  exercice("les extracteurs fonctionnent aussi avec plusieurs valeurs") {
    class Email(val value:String, val spamRatio:Integer)
    object Email {
      def unapply(email:Email):Option[(String,Integer)] = Option((email.value,email.spamRatio))
    }

    val email = new Email("foo@bar.com",5)
    val Email(extractedString,extractedRatio) = email

    (extractedRatio) should be(__)
    (extractedString) should be(__)
  }

 /**
  * Créer une case classe définit automatiquement un extracteur
  * pour cette case classe
  */
  exercice("Un extracteur est définit automatiquement pour toute case classe") {
    case class Email(val value:String)

    val mailstring = "foo@bar.com"
    val email = new Email(mailstring)
    val Email(extractedString) = email

    (extractedString) should be(__)
  }
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

 

  exercice("le pattern matching peut être utilisé avec des case classes pour capturer des valeurs") {
    case class A(/* le compilateur ajoute 'val' ici  */ a:String
               , /* le compilateur ajoute 'val' ici  */ b:String)

    val a:A = /* On peut faire A(.., ...) à la place de new A(..., ...) 
       car l'object companion de A calculé par le compilateur définit A$.apply(..., ...) */
              A(a="string", b="B")

    val actual = a match {
      case A(a,b) => a+b
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
      case _ => "DEFAULT"
    }

    (consActual) should be (__)

    val headtailActual = s match {
      case head::tail => tail
      case _ => "DEFAULT"
    }

    (headtailActual) should be (__)
  }
  
}
