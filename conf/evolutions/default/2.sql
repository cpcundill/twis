
# --- !Ups

ALTER TABLE tweets ADD username varchar(100) NULL BEFORE date;
ALTER TABLE tweets ADD displayname varchar(150) NULL BEFORE date;

# --- !Downs

ALTER TABLE tweets DROP COLUMN username;
ALTER TABLE tweets DROP COLUMN displayname;