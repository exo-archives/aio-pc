------------------------------------------------------
--WSRP STATE DATA TABLE
------------------------------------------------------
create table WSRP_STATE (
  id              BIGINT NOT NULL,
  key             VARCHAR(255) NOT NULL,
  dataType        VARCHAR(128) NOT NULL,
  data            LONGVARBINARY,
  PRIMARY KEY (id));

create table WSRP_PRODUCER (
  id              VARCHAR{255) NOT NULL,
  data            LONGVARBINARY,
  PRIMARY KEY (id));
