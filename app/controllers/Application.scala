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
    def listPosts(page: Int, postsPerPage: Int = 10) = Action { implicit request =>
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
     */
    def savePost = Action { implicit request =>
        blogPostForm.bindFromRequest.fold(
            formWithErrors => BadRequest(html.createForm(formWithErrors)),
            blogPost => {
                /* binding success, you get the actual value. */
                Post.insert(blogPost)
                Redirect(routes.Application.listPosts(0,10)).flashing("success" -> "Post has been created")
            }
        )
    }

    /**
     * display form to edit an existing blog post
     *
     * @param id the post id
     */
    def editPost(id: Long) = Action {
        Post.findPostById(id).map { post =>
            Ok(html.editForm(id,blogPostForm.fill(post)))
        }.getOrElse(NotFound)
    }

    /**
     * save changes to an existing post
     * 
     * @param id the post id
     */
    def updatePost(id: Long) = Action { implicit request =>
        blogPostForm.bindFromRequest.fold(
            formWithErrors => BadRequest(html.editForm(id, formWithErrors)),
            blogPost => {
                Post.update(id, blogPost)
                Redirect(routes.Application.listPosts(0,10)).flashing("success" -> "Post has been updated")
            }
        )
    }

    /**
     * delete a post
     *
     * @param id the post id
     */
    def deletePost(id: Long) = Action { 
        Post.delete(id)
        Redirect(routes.Application.listPosts(0,10)).flashing("success" -> "Post has been deleted")
    }



}
