package type_classes

import support.HandsOnSuite


/*
* ici on défini notre arborescence de type json,
* avec string, number, seq et object.
* représentant tous les types json possibles
* et étendant le même trait JsValue. ce trait est
* scellé, il ne peut pas être etendu en dehors de
* l'unité de compilation. essayez de creer une
* classe étendant ce trait dans un autre fichier
*/
sealed trait JsValue

case class JsString(s:String) extends JsValue{
  override def toString():String= ???
}
case class JsNumber(n:Number) extends JsValue{
  override def toString():String= ???
}
case class JsSeq(seq:Seq[JsValue]) extends JsValue{
  override def toString():String= ???
}
case class JsObject(properties:Map[String, JsValue]) extends JsValue{
  override def toString():String= ???
}

/*
* On crée un objet compagnon permettant de créer un JsObject
* à partir d'une liste de clé valeur de type String -> JsValue
*/
object JsObject{
  def apply(properties:(String,JsValue)*):JsObject= ???
}

/*
* Voici une type class
* C'est un trait générique qui permet de prendre un type A et de retourner
* la JsValue le représentant.
*/
trait Writer[A]{
  def write(value:A):JsValue
}

/*
* l'objet compagnon l'accompagnant sert à créer des writer sans à avoir à spécifier
* le type générique, car il est inféré à la compilation grâce au type de la fonction
* passée en paramètre à apply
*/
object Writer{
  def apply[A]( f: A=>JsValue ):Writer[A]={
    new Writer[A]{
      override def write(value:A)=f(value)
    }
  }
}

/*
* Dans cet objet on défini les writer pour les types qui nous intéressent.
* L'intérêt de les marquer implicit est que si on les importe à un endroit du code
* ils seront présent pour tout de scope du code.
*/
object Implicits {
  implicit val stringWriter=Writer { s:String=> ??? }
  implicit val intWriter=Writer { n:Int=> ??? }
  implicit val doubleWriter=Writer { n:Double=> ??? }
  implicit val bigDecimalWriter=Writer { n:BigDecimal=> ??? }
  implicit def seqWriter[B](implicit writer:Writer[B])= Writer { seq:Seq[B] => ??? }
}

/*
* Enfin le sérialiseur qui prend un objet de type A et implicitement
* un writer de type Writer[A]. Il n'a donc pas besoin d'importer ici
* les writers en question, il suffira de les avoir dans le scope implicit
* du cope appelant.
*/
object Json{
  def toJson[A](value:A)(implicit writer:Writer[A]):JsValue={
    writer.write(value)
  }
}



import Implicits._

class testJson extends HandsOnSuite {
  exercice("a Json tree should print itself"){
    val jsonString=JsString("json")
    val jsonNumber=JsNumber(int2Integer(10))
    val jsonSequece=JsSeq(Seq(JsString("a"), JsString("b"), JsString("c")))
    val jsonObject=JsObject(Map("string"->jsonString, "number"->jsonNumber, "seq"->jsonSequece))

    val expected:String="""{
      |  "string":"json",
      |  "number":10,
      |  "seq":["a","b","c"]
      |}""".stripMargin

    val actual=jsonObject.toString()

    actual should equal(expected)
  }
  exercice("toJson should correctly convert a String"){
    val expected=JsString("json")
    val actual=Json.toJson("json")

    actual should equal(expected)
  }
  exercice("toJson should correctly convert an Integer"){
    val expected=JsNumber(12)
    val actual=Json.toJson(12)

    actual should equal(expected)
  }
  exercice("toJson should correctly convert a Double"){
    val expected=JsNumber(12.0)
    val actual=Json.toJson(12.0)

    actual should equal(expected)
  }
  exercice("toJson should correctly convert a BigDecimal"){
    val expected=JsNumber(BigDecimal("99999999999999999999999999999999999999999999999999999999999999999999999999"))
    val actual=Json.toJson(BigDecimal("99999999999999999999999999999999999999999999999999999999999999999999999999"))

    actual should equal(expected)
  }
  exercice("toJson should correctly convert a Sequence of strings"){
    val expected=JsSeq(Seq(JsString("a"),JsString("b"),JsString("c")))
    val actual=Json.toJson(Seq("a","b","c"))

    actual should equal(expected)
  }
}
package client {
  /*
  * On simule ici un utilisateur de notre bibliothèque JSON,
  * il veut sérialiser un objet qu'il a défini lui même.
  * Cet objet n'est bien sur pas connu de la bibliothèque.
  * Pourtant nous allons pouvoir étendre cette dernière de
  * manière à conserver toutes les garanties de typage et
  * a ce que ces garanties soient vérifiées à la compilation
  */
  case class User(name:String,age:Int,friends:Seq[String])

  object User{
    implicit val userWrite:Writer[User] = Writer { u:User => ??? }
  }

  import User.userWrite
  class testJsonClient extends HandsOnSuite {
    exercice("toJson should correctly convert a user"){
      val user = User("Mathieu", 25, Seq("Jean","Jon","Ludwine"))

      val expected=JsObject(Map("name"->JsString("Mathieu"), "age"->JsNumber(25), "friends"->JsSeq(Seq(JsString("Jean"),JsString("Jon"),JsString("Ludwine")))))
      val actual=Json.toJson(user)

      actual should equal(expected)
    }
  }
}
