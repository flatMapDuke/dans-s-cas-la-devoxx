package un_sac_avec_des_items

import support.HandsOnSuite


class e2_un_peu_plus_algebrique extends HandsOnSuite {

 /**
   * Un type algébrique (ici Sac) est un type composé ici par l'union disjointe de SacVide et SacPlein.
   * Cela veut dire que Sac est forcement un SacVide ou un SacPlein.
   */

  /*
  *   Alors là vous avez peut-être remarqué les mots sealed et trait :
  *     - trait un le mot-clé utilisé pour définir une l'équivalent d'une interface en Java.
  *       Il est cependant possible d'implémenter des méthodes dans un trait.
  *
  *     - sealed est un mot clé utilisé devant un trait. Le compilateur ne regardera que les case classes
  *       étendant le trait présent dans ce fichier source là, et si des cas manquent lors du Pattern
  *       Matching, un warning est remonté.
  *
  */
  sealed trait Sac {

    def items:Set[String]

    def map(fonction:Int => Int):Sac

    def flatMap(fonction:Int => Sac):Sac

    def filter(fonction:Int => Boolean):Sac

    def valeurOuSinon(replacement:Int):Int

    def addItems(items:Set[String]):Sac

  }

  object Sac {
    def apply(valeur:Int, items:Set[String] = Set.empty):Sac = SacPlein(valeur, items)
  }

  case class SacVide(items:Set[String] = Set.empty) extends Sac {

    override def map(fonction:Int => Int):Sac = ???

    override def flatMap(fonction:Int => Sac):Sac = ???

    override def filter(fonction:Int => Boolean):Sac = ???

    override def valeurOuSinon(replacement:Int):Int = replacement

    def addItems(items: Set[String]): Sac = ???
  }

  case class SacPlein(valeur:Int , items:Set[String] = Set.empty) extends Sac {

    override def map(fonction:Int => Int):Sac = ???

    override def flatMap(fonction:Int => Sac):Sac = ???

    override def filter(fonction:Int => Boolean):Sac = ???

    override def valeurOuSinon(replacement:Int):Int = valeur

    def addItems(items: Set[String]): Sac = ???
  }


  exercice("toujours comme avant, je peux construire mon Sac")  {

    val s0 = Sac(0, Set("un portable"))    // appel de la fonction apply dans l'objet companion de Sac
                                         // un peu comme List(1,2,3)

  }

  exercice("on peut ajouter des items au sac") {

    val s0 = Sac(0)

    s0.addItems(Set("un portable")).items should be(Set("un portable"))

    val v0 = SacVide()

    v0.addItems(Set("un portable")).items should be(Set("un portable"))

  }


  exercice("toujours comme avant, je peux appliquer une fonction à l'intérieur") {

    val sacDeZero = Sac(0, Set("un portable"))


    //Le sealed sur le trait Sac rend ce pattern matching exhaustif
    sacDeZero.map(x => x +1) match {
      case SacPlein(valeur, _) => valeur should be(1)

      case SacVide(_) => fail("Cela ne devrait pas être un Sac Vide")
    }

    val sacVide=SacVide(Set("sac vide"))

    sacVide.map(x=>x+1) match {
      case SacPlein(_,_) => fail("Cela ne devrait pas être un Sac Plein")
      case _ => Unit
    }
  }

  exercice("toujours comme avant, je peux combiner mes sac") {
    val sacDeDeux = Sac(2, Set("un portable"))

    val sacDeCent  = Sac(100, Set("un pc"))

    val combinaison = for (deux <- sacDeDeux; cent <- sacDeCent) yield( deux * cent )

    combinaison match {
      case SacPlein(valeur, items) => {
        valeur should be (200)
        items should be(Set("un portable", "un pc"))
      }

      case _ => fail("ne doit pas être vide")
    }
    val sacVide=SacVide(Set("sac vide"))
    val combinaisonVide = for (deux <- sacVide; cent <- sacDeCent) yield( deux * cent )
    combinaisonVide match {
      case SacPlein(_,_) => fail("ne doit pas être plein")
      case _ => Unit
    }

  }

  exercice("on peut filter le valeur d'un sac") {

    val sacDeDeux = Sac(2, Set("un portable"))

    val sac = sacDeDeux.filter(x => x > 10)

    sac match {
      case SacPlein(_,_) => fail("Cela devrait être une sac vide")
      case _  => {}
    }

    sacDeDeux.filter(x => x > 1) match {
      case SacVide(_) => fail("Cela doit être un sac plein")
      case _ => {}
    }

  }

}
