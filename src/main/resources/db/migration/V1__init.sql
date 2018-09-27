CREATE TABLE log (
  id bigint not null AUTO_INCREMENT,
  searchTerm varchar(150),
  requestStart timestamp,
  start timestamp,
  end TIMESTAMP,
  ipAddress varchar(39),
  resultClicks integer,
  anyResults BOOLEAN,
  resultCount INTEGER,
  PRIMARY KEY (id)
);

CREATE TABLE dwelltime (
  id bigint not null auto_increment,
  logId bigint,
  datasheetId varchar(255),
  start timestamp,
  end timestamp,
  PRIMARY KEY (id)
)