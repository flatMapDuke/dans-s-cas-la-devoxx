package we_need_to_go_deeper

import support.HandsOnSuite

class e3_un_sac_covariant extends HandsOnSuite {

  /**
   *
   * La covariance est un mécanisme pour dire qu'un type paramêtrée varie de la même façon que son paramêtre.
   *
   * Si A <: B  (cela veut dire que A est une sous classe de B)
   * Et que Sac[A] est covariance sur A (Sac[+A])
   * Alors Sac[A] <: Sac[B]
   *
   * Ce mécanisme est nécessaire ici pour définir le Sac Vide.
   *
   * Quelque soit A dans les types, Nothing <: A
   * SacVide <: Sac[Nothing]
   * alors SacVide <: Sac[A]
   *
   *
   * @tparam A
   */
  sealed trait Sac[+A] {

    def map[B](fonction:A => B):Sac[B]

    def flatMap[B](fonction:A => Sac[B]):Sac[B]

    def filter(fonction:A => Boolean):Sac[A]

    def contenuOuSinon[B >: A](replacement:B):B

    def isEmpty:Boolean

  }

  object Sac {
    def apply[A](contenu:A):Sac[A] = SacPlein(contenu)
  }

  case object SacVide extends Sac[Nothing] {

    type A = Nothing

    override def map[B](fonction:A => B):Sac[B]  = ???

    override def flatMap[B](fonction:A => Sac[B]):Sac[B]  = ???

    override def filter(fonction:A => Boolean):Sac[A]  = ???

    override def contenuOuSinon[B >: A](replacement:B):B = replacement

    override val isEmpty: Boolean = true

  }

  case class SacPlein[A](contenu:A) extends Sac[A] {

    override def map[B](fonction:A => B):Sac[B]  = ???

    override def flatMap[B](fonction:A => Sac[B]):Sac[B]  = ???

    override def filter(fonction:A => Boolean):Sac[A]  = ???

    override def contenuOuSinon[B >: A](replacement:B):B = contenu

    override val isEmpty: Boolean = false

  }


  exercice("Un peu comme avant, l'application de fonction dans le conteneur") {
    val petitSacDeZero = Sac(0)

    petitSacDeZero.map(x => x + 1) match {
      case SacPlein(contenu)  =>  contenu should be(1)

      case _ => fail("cela ne doit pas être un sac vide")
    }

  }




  exercice("La combinaison de Sac") {

    val petitSacDeZero = Sac(0)

    val grandSacDeA = Sac("A")

    val combinaison = for (p <- petitSacDeZero; g <- grandSacDeA) yield { p.toString + g}

    combinaison match {
      case SacPlein(contenu) => {
        contenu should be("0A")
      }
      case _ => fail("cela ne doit pas être un sac vide")
    }
  }

  exercice("Le filtrage") {
    val petitSacDeZero = Sac(0)

    assert(petitSacDeZero.filter(x => x > 1).isEmpty)
  }



}
