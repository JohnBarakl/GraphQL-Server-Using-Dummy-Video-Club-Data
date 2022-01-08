CREATE TABLE MovieTitle (
  id INTEGER  NOT NULL  ,
  title VARCHAR(100)  NOT NULL  ,
  description VARCHAR(1000)    ,
  releaseDate DATE    ,
  rating FLOAT      ,
PRIMARY KEY(id));



CREATE TABLE MovieFormat (
  id INTEGER  NOT NULL  ,
  name INTEGER      ,
PRIMARY KEY(id));



CREATE TABLE ProductionCompany (
  id INTEGER  NOT NULL  ,
  name VARCHAR(100)  NOT NULL    ,
PRIMARY KEY(id));



CREATE TABLE Person (
  id INTEGER  NOT NULL  ,
  name VARCHAR(100)  NOT NULL    ,
PRIMARY KEY(id));



CREATE TABLE Category (
  id INTEGER  NOT NULL  ,
  name VARCHAR(100)  NOT NULL    ,
PRIMARY KEY(id));



CREATE TABLE Customer (
  id INTEGER  NOT NULL  ,
  fullName VARCHAR(100)  NOT NULL  ,
  dateOfBirth DATE    ,
  address VARCHAR(100)  NOT NULL  ,
  phoneNumber VARCHAR(20)  NOT NULL  ,
  email VARCHAR(100)      ,
PRIMARY KEY(id));



CREATE TABLE Medium (
  id INTEGER  NOT NULL  ,
  name VARCHAR(100)      ,
PRIMARY KEY(id));



CREATE TABLE producedBy (
  ProductionCompany_id INTEGER  NOT NULL  ,
  MovieTitle_id INTEGER  NOT NULL    ,
PRIMARY KEY(ProductionCompany_id, MovieTitle_id)    ,
  FOREIGN KEY(ProductionCompany_id)
    REFERENCES ProductionCompany(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(MovieTitle_id)
    REFERENCES MovieTitle(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);


CREATE INDEX ProductionCompany_has_MovieTitle_FKIndex1 ON producedBy (ProductionCompany_id);
CREATE INDEX ProductionCompany_has_MovieTitle_FKIndex2 ON producedBy (MovieTitle_id);



CREATE TABLE inCategory (
  MovieTitle_id INTEGER  NOT NULL  ,
  Category_id INTEGER  NOT NULL    ,
PRIMARY KEY(MovieTitle_id, Category_id)    ,
  FOREIGN KEY(MovieTitle_id)
    REFERENCES MovieTitle(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(Category_id)
    REFERENCES Category(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);


CREATE INDEX MovieTitle_has_Category_FKIndex1 ON inCategory (MovieTitle_id);
CREATE INDEX MovieTitle_has_Category_FKIndex2 ON inCategory (Category_id);



CREATE TABLE MovieTitleParticipants (
  MovieTitle_id INTEGER  NOT NULL  ,
  Person_id INTEGER  NOT NULL  ,
  participationRole VARCHAR(100)      ,
PRIMARY KEY(MovieTitle_id, Person_id)    ,
  FOREIGN KEY(MovieTitle_id)
    REFERENCES MovieTitle(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(Person_id)
    REFERENCES Person(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);


CREATE INDEX MovieTitle_has_Person_FKIndex1 ON MovieTitleParticipants (MovieTitle_id);
CREATE INDEX MovieTitle_has_Person_FKIndex2 ON MovieTitleParticipants (Person_id);



CREATE TABLE MovieCopy (
  id INTEGER  NOT NULL  ,
  MovieFormat_id INTEGER  NOT NULL  ,
  MovieTitle_id INTEGER  NOT NULL  ,
  Medium_id INTEGER  NOT NULL  ,
  rentPrice FLOAT  NOT NULL    ,
PRIMARY KEY(id)      ,
  FOREIGN KEY(Medium_id)
    REFERENCES Medium(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(MovieTitle_id)
    REFERENCES MovieTitle(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(MovieFormat_id)
    REFERENCES MovieFormat(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);


CREATE INDEX MovieCopy_FKIndex1 ON MovieCopy (Medium_id);
CREATE INDEX MovieCopy_FKIndex2 ON MovieCopy (MovieTitle_id);
CREATE INDEX MovieCopy_FKIndex3 ON MovieCopy (MovieFormat_id);



CREATE TABLE RentTransaction (
  Customer_id INTEGER  NOT NULL  ,
  MovieCopy_id INTEGER  NOT NULL  ,
  id INTEGER  NOT NULL  ,
  price FLOAT  NOT NULL  ,
  dateFrom DATETIME  NOT NULL  ,
  dateTo DATETIME      ,
PRIMARY KEY(Customer_id, MovieCopy_id, id)    ,
  FOREIGN KEY(Customer_id)
    REFERENCES Customer(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(MovieCopy_id)
    REFERENCES MovieCopy(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);


CREATE INDEX Customer_has_MovieCopy_FKIndex1 ON RentTransaction (Customer_id);
CREATE INDEX Customer_has_MovieCopy_FKIndex2 ON RentTransaction (MovieCopy_id);




