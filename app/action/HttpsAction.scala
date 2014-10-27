package action

import play.api.Play
import play.api.Play.current
import play.api.mvc._
 
import scala.concurrent.Future


object HttpsAction extends ActionBuilder[Request] with Results {
	def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
		if (Play.isProd && !request.secure) {
		  Future.successful(Redirect(s"https://${request.domain}:443${request.path}", 301))
		} else {
		  block(request)
		}
	}
}