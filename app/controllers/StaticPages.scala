package controllers
import play.api._
import play.api.mvc._

import models._
import views._



object StaticPages extends Controller {
  
    def index = Action {
        Redirect(routes.StaticPages.about)
    }

    
    def about = Action { implicit request =>
      Ok(html.statics.about())
    }
    
    def contact = Action { implicit request =>
      Ok(html.statics.contact())
    }
}