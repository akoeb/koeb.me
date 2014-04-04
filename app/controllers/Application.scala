package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

// need that for Pk datatype
import anorm._

import models._
import views._


// do not re-use model case class, because we have id and date in model
//case class BlogPost (title: String, teaser: String, text: String)


object Application extends Controller {

    val blogPostForm = Form(
        mapping(
            "id" -> ignored(NotAssigned:Pk[Long]),
            "title" -> nonEmptyText,
            "teaser" -> optional(text),
            "text" -> nonEmptyText,
            "created" -> optional(date)  // TODO: how to make that ignored?
        )(Post.apply)(Post.unapply)
    )

// Homepage action
    def index = Action {
        Redirect(routes.Application.listPosts(0,10))
    }

    // ACTIONS

    /**
     * Display the paginated list of posts
     *
     * @param page Current page number (starts from 0)
     * @param postsPerPage number of posts on one page
     */
    def listPosts(page: Int, postsPerPage: Int) = Action { implicit request =>
        Ok(html.list(Post.list(page = page, postsPerPage = postsPerPage)))
    }

    /**
     * display form to create a new post
     *
     */
    def createPost = Action {
        Ok(html.createForm(blogPostForm))
    }

    /**
     * save a new Post to the persistence backend
     *
     * @param title the post headline 
     * @param teaser the teaser text of the post
     * @param text   the post body
     */
    def savePost = Action { implicit request =>
        blogPostForm.bindFromRequest.fold(
            formWithErrors => BadRequest(html.createForm(formWithErrors)),
            blogPost => {
                /* binding success, you get the actual value. */
                //val newPost = Post(blogPost.title, blogPost.teaser, blogPost.text)
                Post.insert(blogPost)
                Redirect(routes.Application.index).flashing("success" -> "Post %s has been created".format(blogPost.title))
            }
        )
    }

    /**
     * display form to edit an existing blog post
     *
     * @param id the post id
     */
    def editPost(id: Long) = TODO

    /**
     * save changes to an existing post
     * 
     * @param id the post id
     * @param title the post headline 
     * @param teaser the teaser text of the post
     * @param text   the post body
     */
    def updatePost(id: Long) = TODO

    /**
     * delete a post
     *
     * @param id the post id
     */
    def deletePost(id: Long) = TODO


}
