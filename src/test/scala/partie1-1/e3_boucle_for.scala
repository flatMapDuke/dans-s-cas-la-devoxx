package premiers_pas

import support.HandsOnSuite

/**
*  Les for, comprendre le 'for' classique et le 'for comprehension'.
*
*  Les for sont applicable sur toutes collections.
*/

class e3_boucle_for extends HandsOnSuiteP1 {

  //for classique

  exercice("les for c’est assez simple") {
    //0, 1, 2, 3, 4, 5, 6, 7, 8, 9
    val quelqueNombres = Range(0, 10)
    // on peut aussi utiliser 'until' et 'to' => 0 until 10 == Range(0,10) == 0 to 9

    // n’hésitez pas à jouer avec l’interpréteur Scala ouvert via
    // une autre console sbt pour aller voir ce qu’est un Range.
    // pour cela lancez un autre handson. Puis pour lancer l’interpréteur Scala, taper 'console'.
    // Vous pouvez ensuite écrire du code Scala !
    var somme = 0
    for (i <- quelqueNombres) {
      somme += i
    }

    somme should equal(__)
  }

  /**
  * On peut rajouter des conditions à l’intérieur des boucles
  */
  exercice("les boucles for peuvent contenir de la logique") {
    val quelqueNombres = 0 until 10
    var somme = 0
    // somme des nombres pairs, seulement !
    for (i <- quelqueNombres) {
      if (i % 2 == 0) {
        somme += i
      }
    }

    somme should equal(__)
  }

  //for comprehensions

  /**
  * Les for comprehensions sont des outils très puissants de haut niveau pour combiner et
  * filtrer des données.
  * Elles combinent des itérateurs ( <- ) et des filtres ( if )
  * Une boucle for peut générer une liste, sur laquelle on peut appliquer des fonctions
  * Le mot clé 'yield' est utilisé pour retourner un élément et passer à l’itération suivante.
  */
  exercice("Les boucles for peuvent produire une liste, peuvent être sommée facilement") {
    val quelqueNombres = 0 until 10

    val uneListe =
      for {
        i <- quelqueNombres
        if ((i % 2) == 0)
      }
      yield i

    //reduceLeft ici permet de calculer la somme de tous les éléments de la liste
    //en utilisant une autre fonction passée en paramètre.
    //En ce qui concerne les fonctions d’ordre supérieur et les lambda expression,
    //un jeu de tests est prévu dans la suite de l’atelier
    uneListe.reduceLeft( (k,l) => k + l) should be(__)
  }


  /**
  * On peut combiner plusieurs itérateurs entre eux
  * Ici on génère toutes les combinaisons possibles entre les éléments de
  * xValues et ceux de yValues.
  */
  exercice("Les boucles for peuvent être imbriquées") {
    val xValues = 1 until 5
    val yValues = 1 until 3
    val coordinates = for {
      x <- xValues
      y <- yValues
    }
    yield (x, y)
    coordinates(4) should be(__)
  }

  /**
  * Voici l’équivalent Java

  * class Tuple() {
  * private final int x;
  * private final int y;
  *
  *   public void Tuple(int x, int y) {
  *     this.x = x;
  *     this.y = y;
  *   }
  *
  * ... on met pas tout...
  *
  * }
  *
  * List<Tuple> coordinates = new ArrayList<Tuple>();
  *
  * for (int x = 1; i < 5; x++) {
  *   for (int y = 1; i < 3; y++) {
  *      coordinates.add(new Tuple(x, y));
  *   }
  * }
  * ... blablabla
  *
  */
}
