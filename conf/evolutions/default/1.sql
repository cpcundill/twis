
# --- !Ups

CREATE TABLE tweets (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    date timestamp NOT NULL,
    text varchar(255) NOT NULL,
    link varchar(255) NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE tweets;