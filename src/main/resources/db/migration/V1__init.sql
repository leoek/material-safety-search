CREATE TABLE log (
  id bigint not null AUTO_INCREMENT,
  searchTerm varchar(150),
  requestStart timestamp,
  start timestamp,
  end TIMESTAMP,
  publicIp varchar(39),
  localIp varchar(39),
  resultClicks integer,
  anyResults BOOLEAN,
  resultCount INTEGER,
  session VARCHAR(100),
  PRIMARY KEY (id)
);

CREATE TABLE dwelltime (
  id bigint not null auto_increment,
  logId bigint,
  datasheetId varchar(100),
  section VARCHAR(20),
  start timestamp,
  end timestamp,
  PRIMARY KEY (id)
)