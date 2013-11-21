
# --- !Ups

CREATE TABLE users (
  id varchar(120) NOT NULL,
  password varchar(255) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO USERS VALUES ('chrisc@cakesolutions.net', 'foo');

CREATE TABLE user_permissions (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id varchar(120) NOT NULL,
  permission varchar(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO USER_PERMISSIONS (USER_ID, PERMISSION) VALUES ('chrisc@cakesolutions.net', 'BASIC');
INSERT INTO USER_PERMISSIONS (USER_ID, PERMISSION) VALUES ('chrisc@cakesolutions.net', 'ADMIN');

# --- !Downs


DROP TABLE user_permissions;
DROP TABLE users;