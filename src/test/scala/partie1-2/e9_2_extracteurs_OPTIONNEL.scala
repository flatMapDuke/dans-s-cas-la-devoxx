package pas_suivant

import support.HandsOnSuite

/**
* On passe aux extracteurs
*/

class e9_2_extracteurs extends HandsOnSuite {
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

  /**
  * le pattern matching peut être utilisé avec des extracteurs
  */
  exercice("le pattern matching peut être utilisé avec des extracteurs") {
    case class A(val a:String="A")
    val a:A = new A(a="c")

    val actual = a match {
      case A("a") => "stringA"
      case A("b") => "stringB"
      case A("c") => "stringC"
      case _ => "DEFAULT"
    }

    (actual) should be (__)
  }

  exercice("le pattern matching peut être utilisé avec des extracteurs pour capturer des valeurs") {
    case class A(val a:String, val b:String)

    val a:A = new A(a="string", b="B")

    val actual = a match {
      case A(a,b) => a+b
      case _ => "DEFAULT"
    }

    (actual) should be (__)
  }

}