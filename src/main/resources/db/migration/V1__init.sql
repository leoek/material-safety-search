CREATE TABLE log (
  id bigint not null AUTO_INCREMENT,
  timestamp timestamp,
  ipAddress varchar(39),
  dwellTime DOUBLE,
  resultClicks integer,
  anyResults BOOLEAN,
  resultCount INTEGER,
  PRIMARY KEY (id)
);