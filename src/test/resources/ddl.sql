 alter session set container = ORCLPDB1;
CREATE USER lem_stage IDENTIFIED BY lem_stage;
GRANT CONNECT TO lem_stage;
GRANT CONNECT, RESOURCE, DBA TO lem_stage;
GRANT CREATE SESSION to lem_stage ;
GRANT ALL PRIVILEGES TO lem_stage;
GRANT UNLIMITED TABLESPACE TO lem_stage;
