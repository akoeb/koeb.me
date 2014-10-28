package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import models._
import views._
import play.twirl.api._

import play.api.cache.Cache
import play.api.Play.current

object Application extends Controller with Secured {

    // the login Form
    val loginForm = Form(
        tuple(
            "username" -> text,
            "password" -> text
            ) verifying ("Invalid username or password", result => result match {
                case (username, password) => Account.authenticate(username, password).isDefined
            })
        )

    // ACTIONS

    // Homepage action
    def index = Action {implicit request =>
        Redirect(routes.Blog.listPosts(0,10))
    }



    /**
     * Login page.
     */
    def login = Action { implicit request =>
        Ok(html.loginForm(loginForm))
    }
    
    /**
     * Handle login form submission.
    */
   def authenticate = Action { implicit request =>
       loginForm.bindFromRequest.fold(
           formWithErrors => BadRequest(html.loginForm(formWithErrors)),
           user => {
        	   
                val uuid = java.util.UUID.randomUUID.toString
                val token = uuid + ":username"
                Cache.set(token, user._1)
                Redirect(routes.Blog.createPost).withSession("uuid" -> uuid)
           }
       )
       
   }
   
   /**
    * Logout and clean the session.
    */
   def logout = Action { implicit request => {
       val token = request.session.get("uuid") + ":username"
       Cache.remove(token)
       Redirect(routes.Application.login).withNewSession.flashing(
           "success" -> "You've been logged out"
       )}
   }
   
}
