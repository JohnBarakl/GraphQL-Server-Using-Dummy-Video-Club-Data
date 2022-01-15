--
-- File generated with SQLiteStudio v3.3.3 on Sun Jan 16 00:08:53 2022
--
-- Text encoding used: UTF-8
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: Category
CREATE TABLE Category (
                          id INTEGER  NOT NULL  ,
                          name VARCHAR(100)  NOT NULL    ,
                          PRIMARY KEY(id));

-- Table: Customer
CREATE TABLE Customer (
                          id INTEGER  NOT NULL  ,
                          fullName VARCHAR(100)  NOT NULL  ,
                          dateOfBirth DATE    ,
                          address VARCHAR(100)  NOT NULL  ,
                          phoneNumber VARCHAR(20)  NOT NULL  ,
                          email VARCHAR(100)      ,
                          PRIMARY KEY(id));

-- Table: inCategory
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

-- Table: Medium
CREATE TABLE Medium (
                        id INTEGER  NOT NULL  ,
                        name VARCHAR(100)      ,
                        PRIMARY KEY(id));

-- Table: MovieCopy
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

-- Table: MovieFormat
CREATE TABLE MovieFormat (
                             id INTEGER  NOT NULL  ,
                             name INTEGER      ,
                             PRIMARY KEY(id));

-- Table: MovieTitle
CREATE TABLE MovieTitle (
                            id INTEGER  NOT NULL  ,
                            title VARCHAR(100)  NOT NULL  ,
                            description VARCHAR(1000)    ,
                            releaseDate DATE    ,
                            rating FLOAT      ,
                            PRIMARY KEY(id));

-- Table: MovieTitleParticipants
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

-- Table: Person
CREATE TABLE Person (
                        id INTEGER  NOT NULL  ,
                        name VARCHAR(100)  NOT NULL    ,
                        PRIMARY KEY(id));

-- Table: producedBy
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

-- Table: ProductionCompany
CREATE TABLE ProductionCompany (
                                   id INTEGER  NOT NULL  ,
                                   name VARCHAR(100)  NOT NULL    ,
                                   PRIMARY KEY(id));

-- Table: RentTransaction
CREATE TABLE RentTransaction (Customer_id INTEGER NOT NULL, MovieCopy_id INTEGER NOT NULL, id INTEGER NOT NULL, price FLOAT NOT NULL, dateFrom DATETIME NOT NULL, dateTo DATETIME, PRIMARY KEY (id), FOREIGN KEY (Customer_id) REFERENCES Customer (id) ON DELETE NO ACTION ON UPDATE NO ACTION, FOREIGN KEY (MovieCopy_id) REFERENCES MovieCopy (id) ON DELETE NO ACTION ON UPDATE NO ACTION);

-- Index: Customer_has_MovieCopy_FKIndex1
CREATE INDEX Customer_has_MovieCopy_FKIndex1 ON RentTransaction (Customer_id);

-- Index: Customer_has_MovieCopy_FKIndex2
CREATE INDEX Customer_has_MovieCopy_FKIndex2 ON RentTransaction (MovieCopy_id);

-- Index: MovieCopy_FKIndex1
CREATE INDEX MovieCopy_FKIndex1 ON MovieCopy (Medium_id);

-- Index: MovieCopy_FKIndex2
CREATE INDEX MovieCopy_FKIndex2 ON MovieCopy (MovieTitle_id);

-- Index: MovieCopy_FKIndex3
CREATE INDEX MovieCopy_FKIndex3 ON MovieCopy (MovieFormat_id);

-- Index: MovieTitle_has_Category_FKIndex1
CREATE INDEX MovieTitle_has_Category_FKIndex1 ON inCategory (MovieTitle_id);

-- Index: MovieTitle_has_Category_FKIndex2
CREATE INDEX MovieTitle_has_Category_FKIndex2 ON inCategory (Category_id);

-- Index: MovieTitle_has_Person_FKIndex1
CREATE INDEX MovieTitle_has_Person_FKIndex1 ON MovieTitleParticipants (MovieTitle_id);

-- Index: MovieTitle_has_Person_FKIndex2
CREATE INDEX MovieTitle_has_Person_FKIndex2 ON MovieTitleParticipants (Person_id);

-- Index: ProductionCompany_has_MovieTitle_FKIndex1
CREATE INDEX ProductionCompany_has_MovieTitle_FKIndex1 ON producedBy (ProductionCompany_id);

-- Index: ProductionCompany_has_MovieTitle_FKIndex2
CREATE INDEX ProductionCompany_has_MovieTitle_FKIndex2 ON producedBy (MovieTitle_id);

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
