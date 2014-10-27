package controllers
import play.api._

import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import action._

/**
 * This trait is stolen from the zenworks example and extended with token based session management
 */
trait Secured {
  
  /**
   * Retrieve the connected username .
   */
  protected def username(request: RequestHeader) = {
      val token = request.session.get("uuid").orNull + ":username"
      Cache.getAs[String](token)
  }

  /**
   * Redirect to login if the user in not authorized. 
   * TODO: add original URL to flash to be able to redirect to the same page after successful login
   */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)
  
  // --
  
  /** 
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) { user =>
    HttpsAction(request => f(user)(request))
  }


  /**
   * Check if the connected user is a member of this project.
   */
  //def IsMemberOf(project: Long)(f: => String => Request[AnyContent] => Result) = IsAuthenticated { user => request =>
  //  if(Project.isMember(project, user)) {
  //    f(user)(request)
  //  } else {
  //    Results.Forbidden
  //  }
  //}
  /**
   * Check if the connected user is a owner of this task.
   */
  //def IsOwnerOf(task: Long)(f: => String => Request[AnyContent] => Result) = IsAuthenticated { user => request =>
  //  if(Task.isOwner(task, user)) {
  //    f(user)(request)
  //  } else {
  //    Results.Forbidden
  //  }
  //}

}

