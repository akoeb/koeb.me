package models

import play.api.Logger
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import java.util.{Date}
import org.jsoup._, safety._
import play.Application
import controllers._
import scala.language.postfixOps

/**
 * Helper for pagination.
 */
case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
    lazy val prev = Option(page - 1).filter(_ >= 0)
    lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}



case class Post(id: Option[Long] = None, title: String, teaser: Option[String], text: String, created: Option[Date] )


object Post {
    val whitelist = new Whitelist();
    

    // parser for sql queries:
    val post = {
        get[Option[Long]]("id") ~
        get[String]("title") ~
        get[Option[String]]("teaser") ~
        get[String]("text") ~
        get[Option[Date]]("created") map {
            case id~title~teaser~text~created => Post(id, title, teaser, text, created)
        }
    }

    /**
     * Find a post from ID
     *
     * @param id The primary key of the post
     */
    def findPostById(id: Long):Option[Post] = {
        DB.withConnection { implicit connection =>
            SQL("select * from posts where id = {id}").
                on('id -> id).
                as(post.singleOpt)
        }
    }


    /**
    * return a paginated list of posts
    *
    * @param page The page number, 0 to start
    * @param postsPerPage number of posts we want to display on each page
    */
    def list(page: Int, postsPerPage: Int): Page[Post] = {
        val offset = page * postsPerPage
        DB.withConnection { implicit conn =>

            val posts = SQL("SELECT * from posts order by created desc limit {postsPerPage} offset {offset}").on(
                'offset -> offset,
                'postsPerPage -> postsPerPage
            ).as(post *)
        
            val total = SQL("SELECT COUNT(*) FROM posts").as(scalar[Long].single)
        
            Page(posts, page, offset, total)
        }
    }

    /**
     * Insert a new post.
     *
     * @param post the post to insert
     */
    def insert(post: Post) = {
        DB.withConnection { implicit connection =>
            SQL("""INSERT INTO posts (id, title, teaser, text, created)
                VALUES (
                    (select next value for post_id_seq), 
                    {title}, {teaser}, {text}, now()
                )""").on(
                'title -> stripHtml(post.title),
                'teaser -> stripHtml(post.teaser),
                'text -> stripHtml(post.text)
            ).executeUpdate()
        }
    }

    /**
     * modify an existing post
     *
     * @param id the post id
     * @param post the post to update
     */
    def update(id: Long, post: Post) = {
        DB.withConnection { implicit connection =>
            SQL(
                """
                UPDATE posts
                SET title = {title}, teaser = {teaser}, text = {text}
                WHERE id = {id}
            """
            ).on(
                'id -> id,
                'title -> stripHtml(post.title),
                'teaser -> stripHtml(post.teaser),
                'text -> stripHtml(post.text)
            ).executeUpdate()
        }
    }


    def delete(id: Long) = {
        DB.withConnection { implicit connection =>
            SQL(
                """
                DELETE FROM posts WHERE id = {id}
                """
            ).on(
                'id -> id
            ).executeUpdate()
        }
    }

    def stripHtml(string: String):Option[String] = {
        
        val baseURL = play.Play.application().configuration().getString("application.protocol") + 
        		play.Play.application().configuration().getString("application.baseUrl")
        
        val retval = Jsoup.clean(string.replaceAll("\n", "<br/>"), 
            baseURL, 
            Whitelist.basicWithImages().preserveRelativeLinks(true))
        if (retval.isEmpty) {
          None
        }
        else {
          Some(retval)
        }
    }
    
    def stripHtml(string: Option[String]):Option[String] = {
        if(string.isDefined) {
        	stripHtml(string.get)
        }
        else {
          None
        }
    }
}
