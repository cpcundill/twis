
# --- !Ups

ALTER TABLE tweets DROP COLUMN link;

CREATE TABLE tweet_links (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  tweet_id bigint(20) NOT NULL,
  url varchar(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (tweet_id) REFERENCES tweets(id)
);

ALTER TABLE tweets ADD rank double NOT NULL DEFAULT 0;

# --- !Downs

ALTER TABLE tweets ADD link varchar(255) NULL;

DROP TABLE tweet_links;

ALTER TABLE tweets DROP COLUMN rank;