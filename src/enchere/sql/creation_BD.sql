-- Dropping existing tables
DROP TABLE DOWNWARDSALE;
DROP TABLE LIMITEDTIME;
DROP TABLE OFFER;
DROP TABLE SALE;
DROP TABLE CHARACTERISTIC;
DROP TABLE PRODUCT;
DROP TABLE USR;
DROP TABLE SALEROOM;
DROP TABLE CATEGORY;

-- Creating the CATEGORY table
CREATE TABLE CATEGORY (
    name VARCHAR(128) NOT NULL,
    description VARCHAR(128) NOT NULL,
    CONSTRAINT Kname PRIMARY KEY (name)
);

-- Creating the SALEROOM table
CREATE TABLE SALEROOM (
    idSaleRoom NUMBER GENERATED ALWAYS AS IDENTITY,
    nameCategory VARCHAR(128) NOT NULL,
    FOREIGN KEY (nameCategory) REFERENCES CATEGORY(name),
    CONSTRAINT KidSaleRoom PRIMARY KEY (idSaleRoom)
);

-- Creating the USER table
CREATE TABLE USR (
    email VARCHAR(128) NOT NULL,
    lastName VARCHAR(128) NOT NULL,
    firstName VARCHAR(128) NOT NULL,
    address VARCHAR(1024),
    CONSTRAINT Kemail PRIMARY KEY (email)
);

-- Creating the PRODUCT table
CREATE TABLE PRODUCT (
    idProduct NUMBER GENERATED ALWAYS AS IDENTITY,
    nameCategory VARCHAR(128) NOT NULL,
    name VARCHAR(128) NOT NULL,
    costPrice INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    FOREIGN KEY (nameCategory) REFERENCES CATEGORY(name),
    FOREIGN KEY (idProduct) REFERENCES PRODUCT(idProduct),
    CONSTRAINT KidProduct PRIMARY KEY (idProduct),
    CONSTRAINT CheckPrice CHECK (costPrice >= 0)
);

-- Creating the CHARACTERISTIC table
CREATE TABLE CHARACTERISTIC (
    name VARCHAR(128) NOT NULL,
    idProduct INTEGER NOT NULL,
    value VARCHAR(256) NOT NULL,
    FOREIGN KEY (idProduct) REFERENCES PRODUCT(idProduct),
    CONSTRAINT KnameCharacteristic PRIMARY KEY (name, idProduct)
);

-- Creating the SALE table
CREATE TABLE SALE (
    idSale NUMBER GENERATED ALWAYS AS IDENTITY,
    mailUserSelling VARCHAR(128),
    idProduct INTEGER NOT NULL,
    idSaleRoom INTEGER NOT NULL,
    startingPrice INTEGER NOT NULL,
    isUpward NUMBER(1)  NOT NULL,
    isRevocable NUMBER(1)  NOT NULL,
    isLimited NUMBER(1)  NOT NULL,
    isUnique NUMBER(1)  NOT NULL,
    isFinish NUMBER(1) NOT NULL,
    startTime TIMESTAMP NOT NULL,
    FOREIGN KEY (mailUserSelling) REFERENCES USR(email),
    FOREIGN KEY (idProduct) REFERENCES PRODUCT(idProduct),
    FOREIGN KEY (idSaleRoom) REFERENCES SALEROOM(idSaleRoom),
    CONSTRAINT KidSale PRIMARY KEY (idSale)
);

-- Creating the OFFER table
CREATE TABLE OFFER (
    idOffer NUMBER GENERATED ALWAYS AS IDENTITY,
    mailUserOffering VARCHAR(128),
    idSale INTEGER,
    purchasePrice INTEGER NOT NULL,
    dateOffer TIMESTAMP NOT NULL,
    quantity INTEGER NOT NULL,
    wasAccepted NUMBER(1) ,
    FOREIGN KEY (mailUserOffering) REFERENCES USR(email),
    FOREIGN KEY (idSale) REFERENCES SALE(idSale),
    CONSTRAINT KidOffer PRIMARY KEY (idOffer),
    CONSTRAINT CheckPriceOffer CHECK (purchasePrice >= 0),
    CONSTRAINT CheckQuantity CHECK (quantity >= 0)
);

-- Creating the DOWNWARDSALE table
CREATE TABLE DOWNWARDSALE (
    idSale INTEGER,
    discountStep INTEGER NOT NULL,
    FOREIGN KEY (idSale) REFERENCES SALE(idSale),
    CONSTRAINT KidSaleDW PRIMARY KEY (idSale)
);

-- Creating the LIMITEDTIME table
CREATE TABLE LIMITEDTIME (
    idSale INTEGER,
    endTime TIMESTAMP NOT NULL,
    FOREIGN KEY (idSale) REFERENCES SALE(idSale),
    CONSTRAINT KidSaleLT PRIMARY KEY (idSale)
);
