package we_need_to_go_deeper

import support.HandsOnSuite
import util.Random
import scala.collection

/*
*   Maintenant que vous êtes un peu plus familier avec la syntaxe et que vous avez vu quelques
*   points clé de Scala, passons aux choses sérieuses avec ce premier exo...
*
*   Il faut implémenter les parties avec des ???
*   mais avant cela il faut compléter les __ des tests en bas !
*
*/
class e0_une_histoire_de_sacs /* ou un sac de sac */ extends HandsOnSuite {

  case class Sac(contenu:Int) {

    /**
     * Certains appellent cette API : Functor , un mélange de terminator et de fonction.
     * Ces gens là ont en général une barbe et font un peu de théorie de catégorie.
     *
     *
     * @param fonction la fonction a appliquer à contenu
     * @return un Sac
     */
    def map(fonction:Int => Int):Sac = ???

   /**  Honnêtement, ce n'est pas le concept le plus simple mais il faut se lancer !!! :)
     *
     *   Les individus cités plus haut ont aussi un nom pour cette API : Bind.
     *   A croire que ces individus ont une recette miracle pour trouver des noms à tout.
     *
     *  Tout d’abord un petit rappel sur la fonction flatten, puisque “flatmap” n’est rien
     *  d’autre que la combinaison des fonctions flatten et map.
     *  L’opération
     *       List(List(1, 3), List(2, 4)).flatten
     *
     *  renvoie la liste List[Int] suivante
     *       List(1, 3, 2, 4)
     *  La méthode flatten s’applique en fait à une liste de listes, et l'applatir en une liste.
     *
     * @param fonction Hum hUm, la fonction à appliquer en fusionnant les contextes d'application
     (ici Sac) entre eux...
     * @return un Sac !
     *
     */
    def flatMap(fonction:Int => Sac):Sac = {
      ???
    }
  }


  exercice("Je peux créer mon sac avec un seul entier, et faire des choses avec mon sac") {

    val monPetitSacDeZero = Sac(0)

    monPetitSacDeZero.contenu should be(0)

    monPetitSacDeZero.copy(1) should be(__)

    def incrémenteUnSac(sac:Sac):Sac = sac.copy(sac.contenu + 1)

    incrémenteUnSac(monPetitSacDeZero).contenu should be(__)

  }

  exercice("je peux appliquer une fonction à l'intérieur de mon sac") {
    /**
     *
     * Pour passer ce test il faut implémenter la fonction Map plus haut
     */
    val incrémente:(Int => Int) = (i:Int) => i + 1
    //l'équivalent avec def est :
    def incrémenteAvecDef(i:Int) = i + 1


    incrémente(0) should be(1)

    val monPetitSacDeZero = Sac(0)

    monPetitSacDeZero.map(incrémente).contenu should be(1)
  }

  exercice("je peux appliquer une expression en for sur mon sac") {
    /**
     * Ce test se base sur la fonction map implémentée précedement
     */

    val monPetitSacDeZero = Sac(0)

    // ici on a rajouté .asInstanceOf[Int]
    // pour des soucis de compilation, il faudra donc remplacer «__.asInstanceOf[Int]»
    val monPetitSacDeUn  = (for (i <- monPetitSacDeZero) yield (i + __.asInstanceOf[Int]))

    /*
     * Le compilateur scala traduit cette boucle for par :
     *
     * monPetitSacDeZero.map(i => i + __.asInstanceOf[Int])
     */

    monPetitSacDeUn.contenu should be(1)

  }

  exercice("je peux appliquer une expression imbriquée dans mes Sac") {

    /**
     * Ce test se base sur la fonction flatMap a implémenter plus haut.
     */


    val monPetitSacDeDeux = Sac(2)
    val monGrosSacDeCent = Sac(100)

    val l_union_de_mes_sac = for (p <- monPetitSacDeDeux; g <- monGrosSacDeCent) yield( p * g)

    /**
     * Le compilateur scala traduit cette boucle for par :
     *
     * monPetitSacDeDeux.flatMap{ p => monGrosSacDeCent.map(g => p *g))
     */

    l_union_de_mes_sac.contenu should be(200)
  }

}