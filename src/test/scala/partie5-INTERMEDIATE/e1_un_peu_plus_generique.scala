package un_sac_avec_des_items

import support.HandsOnSuite

class e1_un_peu_plus_generique  extends  HandsOnSuite {

  case class Sac[A](valeur:A, items:Set[String]) {

    def map[B](fonction: A => B):Sac[B] = ???

    def flatMap[B](fonction: A => Sac[B]):Sac[B] = {
      ???
    }

  }


  exercice("Un peu comme avant, l'application de fonction dans le conteneur") {
    val petitSacDeZero = Sac(0,Set("un portable"))

    petitSacDeZero.map(x => x + 1).valeur should be(1)

  }

  exercice("La combinaison de Sac") {

    val petitSacDeZero = Sac(0,Set("un portable"))

    val grandSacDeA = Sac("A", Set("un pc"))

    val combinaison = for (p <- petitSacDeZero; g <- grandSacDeA) yield { p.toString + g}


    combinaison.valeur should be("0A")
    combinaison.items should be(Set("un portable", "un pc"))
  }

}
