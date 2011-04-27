------------------------------------------------------
--WSRP STATE DATA TABLE
------------------------------------------------------
create table WSRP_STATE2 (
  id              BIGINT NOT NULL,
  key             VARCHAR(255) NOT NULL,
  dataType        VARCHAR(128) NOT NULL,
  data            LONGVARBINARY,
  PRIMARY KEY (id));

create table WSRP_PRODUCER2 (
  id              VARCHAR{255) NOT NULL,
  data            LONGVARBINARY,
  PRIMARY KEY (id));
