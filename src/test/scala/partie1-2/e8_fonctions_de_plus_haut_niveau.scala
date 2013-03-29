package pas_suivant

import premiers_pas.HandsOnSuiteP1

/**
*   Les fonctions de plus haut niveau.
*
*   Les fonctions d’ordre supérieur sont des fonctions qui peuvent prendre des fonctions
*   comme paramètre, et/ou peuvent retourner des fonctions.
*
*   Par exemple : filter, map, flatmap...
*
*   Un peu de syntaxe sur les fonctions en général:
*     - pour définir une fonction on utilise très souvent le mot clé def
*     - on peut spécifier ou non le type de retour de la fonction en écrivant : puis le type de retour
*
*           def addition(a: Int, b: Int): Int = a + b
*
*     - pas besoin de return en Scala puisque la dernière expression est retournée par défaut !
*
*/
class e8_fonctions_de_plus_haut_niveau extends HandsOnSuiteP1 {

  /**
  *   Une variable peut faire référence à une fonction dite anonyme.
  *
  *   Remarque : on peut aussi écrire la fonction lambda de cette façon avec les accolades,
  *   ou s’en passer.
  *
  *   val lambda = {
  *     x: Int => x + 1
  *   }
  */
  exercice("Une fonction anonyme comme variable") {
    val lambda = (x: Int) => x + 1
    def result = List(1, 2, 3) map lambda
    // le compilateur Scala fait de l’inférence de type donc on peut se passer de préciser le type
    // des variables que l’on définit
    result should be(__)
  }

  /**
  * ça marche encore avec une 'vraie' fonction comme variable,
  * c’est justement une des particularité des fonctions de plus haut niveau
  */
  exercice("Variable qui fait référence à une fonction") {
    val lambda = new Function1[Int, Int] {
      def apply(v1: Int) = v1 + 1
    }
    def result = List(1, 2, 3) map lambda
    result should be(__)
  }

  /**
  * Avec une autre façon de définir la fonction lambda, passée en paramètre de map
  */
  exercice("Encore une autre façon") {
    val lambda = new (Int => Int) {
      def apply(v1: Int) = v1 + 1
    }

    def result = List(1, 2, 3) map lambda
    result should be(__)
  }

  /**
  * ou plus simplement
  */
  exercice("simplement") {
    def result = List(1, 2, 3) map ( x => x + 1 )
    result should be(__)

    def encorePlusSimple = List(1, 2, 3) map ( _ + 1 )
    encorePlusSimple should be(__)
  }



  /**
  *  Les fonctions de plus haut niveau peuvent retourner des fonctions
  */
  exercice("Fonction retournant une autre fonction") {
    def addWithoutSyntaxSugar(x: Int) = {
      new Function1[Int, Int]() {
        def apply(y: Int): Int = x + y
      }
    }

    addWithoutSyntaxSugar(1)(2) should be(__)

    //ou plus simplement
    def add(x: Int) = (y: Int) => x + y
    add(2)(3) should be(__)

    def fiveAdder = add(5)
    fiveAdder(5) should be(__)
  }

  /**
  * Les fonctions de plus haut niveau peuvent prendre une fonction en paramètre
  */
  exercice("Fonction prenant en paramètre une autre fonction. ça aide dans la composition de fonctions") {
    def makeUpper(xs: List[String]) = xs map {
      _.toUpperCase
    }
    def makeWhatEverYouLike(xs: List[String], sideEffect: String => String) = {
      xs map sideEffect
    }
    makeUpper(List("abc", "xyz", "123")) should be(List("ABC", "XYZ", "123"))

    makeWhatEverYouLike(List("ABC", "XYZ", "123"), {
      x => x.toLowerCase
    }) should be(__)

    //using it inline
    List("Scala", "Erlang", "Clojure") map {
      _.length
    } should be(__)
  }

  /**
  * La currification
  */
  exercice("La currification est une technique qui permet de transformer une fonction avec des paramètres multiples en une fonction à un seul paramètre") {
    def multiply(x: Int, y: Int) = x * y
    val multiplyCurried = (multiply _).curried
    multiply(4, 5) should be(__)
    multiplyCurried(3)(2) should be(__)
  }


  exercice("La currification permet de créer des fonctions spécialisées") {
    def customFilter(f: Int => Boolean)(xs: List[Int]) = {
      xs filter f
    }
    def onlyEven(x: Int) = x % 2 == 0
    val xs = List(12, 11, 5, 20, 3, 13, 2)
    customFilter(onlyEven)(xs) should be(__)

    val onlyEvenFilter = customFilter(onlyEven) _  //attention au caractère '_' qui indique au compilateur qu’il ne faut pas appliquer la fonction mais y faire référence
    onlyEvenFilter(xs) should be(__)
  }
}
