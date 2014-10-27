package models
import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._
import org.mindrot.jbcrypt.BCrypt
import play.api.Logger

case class Account(id: Option[Long] = None, username: String, password: String, email: String )

object Account {
    // parser for sql queries:
    val account = {
        get[Option[Long]]("id") ~
        get[String]("username") ~
        get[String]("password") ~
        get[String]("email") map {
            case id~username~password~email => Account(id, username, password, email)
        }
    }
    def authenticate(username: String, password: String): Option[Account] = {
        findByUsername(username).filter { account => BCrypt.checkpw(password, account.password) }
    }


    def findByUsername(username: String):Option[Account] = {
        DB.withConnection { implicit connection =>
            SQL("select * from users where username = {username}").
            on('username -> username).
            as(account.singleOpt)
        }
    }
}

