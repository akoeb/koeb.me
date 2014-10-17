/*
* This file contains actions for the blog that require authenticated access
*/

package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

// need that for Pk datatype
import anorm._

import models._
import views._


object Blog extends Controller with Secured {

    /*
    * The form for creating blog posts
    */
    val blogPostForm = Form(
        mapping(
            "id" -> ignored(NotAssigned:Pk[Long]),
            "title" -> nonEmptyText,
            "teaser" -> optional(text),
            "text" -> nonEmptyText,
            "created" -> optional(date)  // TODO: how to make that ignored?
        )(Post.apply)(Post.unapply)
    )


    /**
     * display form to create a new post
     *
     */
    def createPost = IsAuthenticated { user => request =>
        Ok(html.createForm(blogPostForm))
    }


    /**
     * save a new Post to the persistence backend
     *
     */
    def savePost = IsAuthenticated { user => implicit request =>
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
    def editPost(id: Long) = IsAuthenticated { user => request =>
        Post.findPostById(id).map { post =>
            Ok(html.editForm(id,blogPostForm.fill(post)))
        }.getOrElse(NotFound)
    }

    /**
     * save changes to an existing post
     * 
     * @param id the post id
     */
    def updatePost(id: Long) = IsAuthenticated { user => implicit request =>
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
    def deletePost(id: Long) = IsAuthenticated { user => request =>
        Post.delete(id)
        Redirect(routes.Application.listPosts(0,10)).flashing("success" -> "Post has been deleted")
    }

}

