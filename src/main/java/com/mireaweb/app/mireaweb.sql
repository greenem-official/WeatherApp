CREATE TABLE city
(
   id int NOT NULL,
   name varchar(100),
   PRIMARY KEY (id)
);

CREATE TABLE mesure
(
   id int NOT NULL AUTO_INCREMENT,
   city_id int NOT NULL,
   year int,
   month int,
   day int,
   value FLOAT NOT NULL,
   PRIMARY KEY (id)
);

