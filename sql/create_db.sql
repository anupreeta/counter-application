CREATE DATABASE counters;
--CREATE USER myuser WITH PASSWORD 'password';
ALTER ROLE myuser SET client_encoding TO 'utf8';
ALTER ROLE myuser SET default_transaction_isolation TO 'read committed';
ALTER ROLE myuser SET timezone TO 'UTC';
GRANT ALL PRIVILEGES ON DATABASE "counters" TO "myuser";

CREATE TABLE IF NOT EXISTS counters (
  id BIGINT GENERATED ALWAYS AS IDENTITY,
  name varchar(250) NOT NULL,
  counter_value int NOT  NULL,
  PRIMARY KEY (name)
);