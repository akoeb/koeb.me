# posts schema
 
# --- !Ups

CREATE SEQUENCE user_id_seq;
CREATE TABLE users (
    id integer NOT NULL DEFAULT nextval('user_id_seq'),
    username varchar(100),
    password varchar(100),
    email varchar(100),
    created timestamp
);


-- my test user with password tester
INSERT into users (username, password, email, created) VALUES ('koebi', '$2a$10$5cdYTZT4BfPo51QjrHiuGOKyoaRuF7AYT/NdazgETRxSP6PtF.uPe', 'ak@koeb.me', now())


# --- !Downs
         
DROP TABLE users;
DROP SEQUENCE user_id_seq;
