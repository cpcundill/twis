
# --- !Ups

CREATE TABLE tweets (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    text varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE tweets;