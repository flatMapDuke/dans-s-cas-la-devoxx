package cons_et_nil

import support.HandsOnSuite

class e1_bonus_stream extends HandsOnSuite {

  sealed trait Stream[+A] {

    def map[B](fonction:A => B):Stream[B]

    def flatMap[B](fonction:A => Stream[B]):Stream[B]

    def filter(fonction:A => Boolean):Stream[A]

    final def union[B >: A](stream: => Stream[B]):Stream[B]= {
      this match {
        case cons:Cons[A] => Cons(cons.head, ???)
        case EmptyStream => ???
      }
    }

    def isEmpty:Boolean

    def take(n: Int): Stream[A] = {
      this match {
        case EmptyStream => EmptyStream
        case cons: Cons[A] => {
          if (n <= 0) EmptyStream
          else if (n == 1) Cons(cons.head, EmptyStream)
          else Cons(cons.head, cons.tail.take((n - 1)))
        }
      }
    }

    def foreach(effetDeBord:A => Unit):Unit
  }

  object Stream {
    // 'A*' signifie var args de A
    def apply[A](values:A*):Stream[A] = {
      if (values.isEmpty) {
        EmptyStream
      } else {
        // ':_*' permet d'étendre une Stream en var args
        (Cons(values.head, Stream(values.tail:_*)))
      }
    }

    def unapply[A](xs: Stream[A]): Option[(A, Stream[A])] = {
      xs match {
        case EmptyStream => None
        case cons:Cons[A] => Some((cons.head, cons.tail))
      }
    }
  }
  /**
   * Cons veut dire Constructor, c'est lui qui permet de construire la Stream en ajoutant un élément
   * à la queue
   */
  final class Cons[A](val head:A, tl: => Stream[A]) extends  Stream[A] {


    def isEmpty = false

    // ce mécanisme permet de garantir la lazyness de la queue de la stream
    // ainsi que la mémoization des valeurs accédées
    lazy val tail:Stream[A] = tl

    def map[B](fonction:A => B):Stream[B] = new Cons(fonction(head), tail.map(fonction))


    /**
     * l'implémentation de flatMap a besoin d'union
     */
    def flatMap[B](fonction:A => Stream[B]):Stream[B] = ???

    override def filter(fonction:A => Boolean):Stream[A] = ???

    override def equals(that:Any):Boolean = ???

    override def hashCode():Int = head.hashCode()

    override def foreach(effetDeBord:A => Unit):Unit = {
      effetDeBord(head)
      tail.foreach(effetDeBord)
    }
  }

  object Cons {
    def apply[A](head:A, tl: => Stream[A]) = new Cons(head,tl)

    def unapply[A](cons:Cons[A]) = Stream.unapply(cons)
  }

  /**
   * il y a qu'un seul EmptyStream, donc cela peut être un case object
   */
  case object EmptyStream extends Stream[Nothing] {
    type A = Nothing

    def map[B](fonction:A => B):Stream[B]  = ???

    def flatMap[B](fonction:A => Stream[B]):Stream[B] = ???

    def filter(fonction:A => Boolean):Stream[A] = ???

    def isEmpty: Boolean = true

    def foreach(effetDeBord:A => Unit):Unit = {}
  }

  exercice("création") {

    Stream() should be(EmptyStream)

    Stream(1,2,3) should be(Cons(1,Cons(2,Cons(3,EmptyStream))))

  }

  exercice("map") {
    Stream(1,2,3).map(x => x + 1) should be(Stream(2,3,4))
  }


  exercice("union") {

    Stream(1,2,3).union(Stream(4,5)) should be(Stream(1,2,3,4,5))

    Stream(1,2,3).union(Stream("A","B","C")) should be(Stream(1,2,3,"A","B","C"))
    // Cet example est complexe, le compilateur cherche un B pour l'union tel que
    // String <: B
    // A (ici Int) <: B
    // Le type qui convient est Any, l'équivalent de Object en Java

    // Il n'y a rien a faire de particulier pour que cet example, le compilateur fait tout.

  }

  exercice("flatMap") {

    val combinaison = for (a <- Stream("A","B"); i <- Stream(1,2)) yield (a + i)

    combinaison should be( Stream("A1","A2","B1","B2"))

  }

  exercice("filter") {

    Stream(1,2,3).filter(x => x > 1) should be(Stream(2,3))

    Stream(1,2,3).filter(x => false) should be(EmptyStream)
  }

  exercice("lazyness") {

    val s = Stream(1,2,3).map{
      case 1 => 1
      case _ => {
        throw new Exception("I should be lazy")
      }
    }

    assert(!s.take(1).isEmpty)


  }

  exercice("lazyness 2") {
    val s:Stream[Int] = Stream(1,2,3).map{
      case 1 => 1
      case _ => {
        throw new Exception("I should be lazy")
      }
    }

    val s2 = for (i <- s; j <- s) yield(i + j)

    s2.take(1).foreach(println)
  }


}
