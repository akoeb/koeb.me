/*
* This file contains actions for the blog that require authenticated access
*/

package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import models._
import views._


object Blog extends Controller with Secured {

    /*
    * The form for creating blog posts
    */
    val blogPostForm = Form(
        mapping(
            "id" -> optional(longNumber),
            "title" -> nonEmptyText,
            "teaser" -> optional(text),
            "text" -> nonEmptyText,
            "created" -> optional(date)
        )(Post.apply)(Post.unapply)
    )
    /**
     * Display the paginated list of posts
     *
     * @param page Current page number (starts from 0)
     * @param postsPerPage number of posts on one page
     */
    def listPosts(page: Int, postsPerPage: Int = 10) =  Action { implicit request =>
        Ok(html.blog.list(Post.list(page = page, postsPerPage = postsPerPage), username(request)))
    }

   /**
     * display one single blog post in its totality
     *
     * @param id the post id
     */
    def showPost(id: Long) = 
      Action { implicit request =>
    
      	
      	val nextPost = Post.findPostById(id + 1)
      	val prevPost = Post.findPostById(id - 1)
      	
      	Post.findPostById(id).map { post => 
            Ok(html.blog.post(post, nextPost, prevPost, username(request)))
        }.getOrElse(NotFound(views.html.errors.onNotFound()))
    }

    
    /**
     * display form to create a new post
     *
     */
    def createPost = IsAuthenticated { user => implicit request =>
        Ok(html.blog.createForm(blogPostForm))
    }


    /**
     * save a new Post to the persistence backend
     *
     */
    def savePost = IsAuthenticated { user => implicit request =>
        blogPostForm.bindFromRequest.fold(
            formWithErrors => BadRequest(html.blog.createForm(formWithErrors)),
            blogPost => {
                /* binding success, you get the actual value. */
                Post.insert(blogPost)
                Redirect(routes.Blog.listPosts(0,10)).flashing("success" -> "Post has been created")
            }
        )
    }

    /**
     * display form to edit an existing blog post
     *
     * @param id the post id
     */
    def editPost(id: Long) = IsAuthenticated { user => implicit request =>
        Post.findPostById(id).map { post =>
            Ok(html.blog.editForm(id,blogPostForm.fill(post)))
        }.getOrElse(NotFound(views.html.errors.onNotFound()))
    }

    /**
     * save changes to an existing post
     * 
     * @param id the post id
     */
    def updatePost(id: Long) = IsAuthenticated { user => implicit request =>
        blogPostForm.bindFromRequest.fold(
            formWithErrors => BadRequest(html.blog.editForm(id, formWithErrors)),
            blogPost => {
                Post.update(id, blogPost)
                Redirect(routes.Blog.listPosts(0,10)).flashing("success" -> "Post has been updated")
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
        Redirect(routes.Blog.listPosts(0,10)).flashing("success" -> "Post has been deleted")
    }

}

