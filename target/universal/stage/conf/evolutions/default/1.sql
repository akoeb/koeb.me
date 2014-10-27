# posts schema
 
# --- !Ups

CREATE SEQUENCE post_id_seq;
CREATE TABLE posts (
    id integer NOT NULL DEFAULT nextval('post_id_seq'),
    title varchar(255),
    teaser varchar(2000),
    text text,
    created timestamp
);
         
# --- !Downs
         
DROP TABLE post;
DROP SEQUENCE post_id_seq;
