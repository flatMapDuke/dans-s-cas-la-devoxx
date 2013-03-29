package we_need_to_go_deeper

import support.HandsOnSuite

class e1_un_sac_comme_generique  extends  HandsOnSuite {

  case class Sac[A](contenu:A) {

    def map[B](fonction: A => B):Sac[B] = ???

    def flatMap[B](fonction: A => Sac[B]):Sac[B] = ???

  }


  exercice("Un peu comme avant, l'application de fonction dans le conteneur") {
    val petitSacDeZero = Sac(0)

    petitSacDeZero.map(x => x + 1).contenu should be(1)

  }

  exercice("La combinaison de Sac") {

    val petitSacDeZero = Sac(0)

    val grandSacDeA = Sac("A")

    val combinaison = for (p <- petitSacDeZero; g <- grandSacDeA) yield { p.toString + g}


    combinaison.contenu should be("0A")
  }

}
