package pas_suivant

import support.HandsOnSuite

/**
*   Une Option comme sont nom l’indique représente une valeur optionnelle.
*   Une instance de Option est soit une instance de Some soit de None
*   (None dans le cas où il n’y a aucune valeur).
*
*   Exemple :
*   Imaginons que l’on veuille trouver une personne dans une BDD. Elle n’existe peut-être pas.
*   Dans ce cas en Scala on écrira :
*
*       def findPerson(key: Int): Option[Person]
*
*   Ca permet de dire que si la personne n’est pas trouvée, alors on renvoie None et non un 'null'
*   comme dans d’autres langages.
*   Le type Option permet donc de traiter des cas pour lesquels aucun résultat n’existe,
*   et de se prémunir des NullPointerException/absence de valeur.
*
*   Quelques liens :
*     - http://www.scala-lang.org/api/current/index.html#scala.Option
*/

class e7_option extends HandsOnSuite {
  /**
  * Quelques tests rapides
  */
  exercice("None est égal à...None") {
   None should be(__)
  }

  exercice("None est identique à None") {
    val a = None
    // ici on dénote le fait d’être identique, et == l’égalité
    a should be(__)
  }

  exercice("None est considéré comme vide") {
    None.isEmpty should be(__)
  }


  exercice("None peut être utilisé avec le type Option, plutôt qu’avec null") {
    val optional: Option[String] = None
    optional.isEmpty should be(__)
    optional should be(__)
  }

  exercice("Some est le complémentaire de None pour le type Option") {
    val optional: Option[String] = Some("Some Value")
    (optional == None) should be(__)
    optional.isEmpty should be(__)
  }

  exercice("Option.getOrElse peut être utilisé pour obtenir une valeur par défaut dans le cas de None") {
    val optional: Option[String] = Some("Some Value")
    val optional2: Option[String] = None
    optional.getOrElse("No Value") should be(__)
    optional2.getOrElse("No Value") should be(__)
  }

}
