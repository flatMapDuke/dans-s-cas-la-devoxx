package bonus_event_sourcing

import support.HandsOnSuite

package events {
  import java.util.UUID

  /**
   * Identifiant d'un post
   */
  case class PostId(uuid: UUID)
  object PostId {
    def generate(): PostId = PostId(UUID.randomUUID())
  }

  /**
   * Contenu d'un post
   */
  case class PostContent(author: String, title: String, body: String)

  /**
   * Evénements du domaine pour définir le cycle de vie d'un blog post.
   */

  sealed trait PostEvent {
    def postId: PostId
  }

  case class PostAdded(postId: PostId, content: PostContent) extends PostEvent
  case class PostEdited(postId: PostId, content: PostContent) extends PostEvent
  case class PostDeleted(postId: PostId) extends PostEvent
}

package model {
  import bonus_event_sourcing.events._

  case class Post(id: PostId, content: PostContent)  {
      def versions:List[PostContent] = ???
  }

  case class Posts(byId: Map[PostId, Post] = Map.empty, orderedByTimeAdded: Seq[PostId] = Vector.empty) {
    def get(id: PostId): Option[Post] = byId.get(id)
    def mostRecent(n: Int): Seq[Post] = orderedByTimeAdded.takeRight(n).reverse.map(byId)

    def apply(event: PostEvent): Posts = event match {
      case PostAdded(id, content)  => ???
      case PostEdited(id, content) => ???
      case PostDeleted(id)         => ???
      case _ => this.copy()
    }
  }

  object Posts {
    def fromHistory(events: PostEvent*): Posts = events.foldLeft(Posts())(_ apply _)
  }
}

import bonus_event_sourcing.model.Posts
import bonus_event_sourcing.events._
import bonus_event_sourcing.events.PostEdited
import bonus_event_sourcing.events.PostContent
import bonus_event_sourcing.events.PostAdded

class testEventSourcing extends HandsOnSuite {

  val articleBiaiséSurlES =  PostContent("(_ + _) (<- l'opérateur panda, membre de la confrérie du semi groupe)",
                                         "à propos de la scalabilité infinie en lecture",
                                         "bla bla bla")

  val articleUnPeuPlusSérieux = PostContent("($/_$/)",
                                            "L'Event Sourcing, une opportunité pour votre businezz !",
                                            "bla bla bla")

  exercice("Ajout de post") {
    val postId = PostId.generate()

    val posts = Posts.fromHistory().apply(PostAdded(postId,articleBiaiséSurlES))

    posts.get(postId) should be('defined)

    posts.get(postId).get.content should be(articleBiaiséSurlES)
  }

  exercice("modification de post") {
    val postId = PostId.generate()

    val posts = Posts.fromHistory(PostAdded(postId, articleBiaiséSurlES))

    val post = posts.apply(PostEdited(postId, articleUnPeuPlusSérieux)).get(postId)

    post should be('defined)

    post.get.content should be(articleUnPeuPlusSérieux)
  }

  exercice("suppression d'un post") {
    val postId = PostId.generate()

    val posts = Posts.fromHistory(PostAdded(postId, articleUnPeuPlusSérieux))

    posts.get(postId) should be('defined)

    posts.apply(PostDeleted(postId)).get(postId) should be('empty)
  }

  exercice("versionning") {

    /* ....*/

  }
}
