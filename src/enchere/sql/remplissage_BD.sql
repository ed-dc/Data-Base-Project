-- Insertion dans la table CATEGORY
INSERT INTO CATEGORY (name, description) VALUES ('Art', 'Œuvres art modernes');
INSERT INTO CATEGORY (name, description) VALUES ('Technology', 'Appareils électroniques et gadgets');
INSERT INTO CATEGORY (name, description) VALUES ('Furniture', 'Mobilier de maison et de bureau');
INSERT INTO CATEGORY (name, description) VALUES ('Jewelry', 'Bijoux de luxe et accessoires');

-- Insertion dans la table SALEROOM
INSERT INTO SALEROOM (nameCategory) VALUES ('Art');
INSERT INTO SALEROOM (nameCategory) VALUES ('Technology');
INSERT INTO SALEROOM (nameCategory) VALUES ('Furniture');
-- INSERT INTO SALEROOM (nameCategory) VALUES ('Jewelry');

-- Insertion dans la table USR
INSERT INTO USR (email, lastName, firstName, address) VALUES ('john.doe@example.com', 'Doe', 'John', '1234 Rue Fictive, Paris, France');
INSERT INTO USR (email, lastName, firstName, address) VALUES ('jane.smith@example.com', 'Smith', 'Jane', '5678 Avenue Imagination, Lyon, France');
INSERT INTO USR (email, lastName, firstName, address) VALUES ('bob.johnson@example.com', 'Johnson', 'Bob', '9 Boulevard du Fictionnel, Marseille, France');
INSERT INTO USR (email, lastName, firstName, address) VALUES ('alice.green@example.com', 'Green', 'Alice', '1234 Rue Fictive, Paris, France');

-- Insertion dans la table PRODUCT
INSERT INTO PRODUCT (nameCategory, name, costPrice, quantity) VALUES ('Art', 'Peinture Abstraite', 5000, 3);
INSERT INTO PRODUCT (nameCategory, name, costPrice, quantity) VALUES ('Technology', 'Smartphone UltraPro', 800, 10);
INSERT INTO PRODUCT (nameCategory, name, costPrice, quantity) VALUES ('Furniture', 'Chaise Ergonomique', 150, 20);
-- INSERT INTO PRODUCT (nameCategory, name, costPrice, quantity) VALUES ('Jewelry', 'Collier Diamant', 2500, 5);

-- Insertion dans la table CHARACTERISTIC
INSERT INTO CHARACTERISTIC (name, idProduct, value) VALUES ('Dimensions', 1, '50x70 cm');
INSERT INTO CHARACTERISTIC (name, idProduct, value) VALUES ('Artist', 1, 'Claude Monet');
INSERT INTO CHARACTERISTIC (name, idProduct, value) VALUES ('Brand', 2, 'UltraTech');
INSERT INTO CHARACTERISTIC (name, idProduct, value) VALUES ('Battery Life', 2, '48 hours');
INSERT INTO CHARACTERISTIC (name, idProduct, value) VALUES ('Material', 3, 'Cuir synthétique');
INSERT INTO CHARACTERISTIC (name, idProduct, value) VALUES ('Color', 3, 'Noir');
-- INSERT INTO CHARACTERISTIC (name, idProduct, value) VALUES ('Weight', 4, '50g');
-- INSERT INTO CHARACTERISTIC (name, idProduct, value) VALUES ('Gemstone', 4, 'Diamant');

-- Insertion dans la table SALE
-- INSERT INTO SALE (mailUserSelling, idProduct, idSaleRoom, startingPrice, isUpward, isRevocable, isLimited, isUnique) 
-- VALUES ('john.doe@example.com', 1, 1, 5000, 1, 1, 0, 1);
-- INSERT INTO SALE (mailUserSelling, idProduct, idSaleRoom, startingPrice, isUpward, isRevocable, isLimited, isUnique) 
-- VALUES ('jane.smith@example.com', 2, 2, 800, 0, 1, 1, 0);
-- INSERT INTO SALE (mailUserSelling, idProduct, idSaleRoom, startingPrice, isUpward, isRevocable, isLimited, isUnique) 
-- VALUES ('bob.johnson@example.com', 3, 3, 150, 1, 0, 0, 0);
-- INSERT INTO SALE (mailUserSelling, idProduct, idSaleRoom, startingPrice, isUpward, isRevocable, isLimited, isUnique) 
-- VALUES ('alice.green@example.com', 4, 4, 2500, 0, 1, 1, 1);

-- Insertion dans la table OFFER
-- INSERT INTO OFFER (mailUserOffering, idSale, purchasePrice, dateOffer, quantity, wasAccepted) 
-- VALUES ('bob.johnson@example.com', 1, 4500, TO_DATE('2024-11-25', 'YYYY-MM-DD'), 1, 1);
-- INSERT INTO OFFER (mailUserOffering, idSale, purchasePrice, dateOffer, quantity, wasAccepted) 
-- VALUES ('alice.green@example.com', 2, 750, TO_DATE('2024-11-25', 'YYYY-MM-DD'), 2, 0);
-- INSERT INTO OFFER (mailUserOffering, idSale, purchasePrice, dateOffer, quantity, wasAccepted) 
-- VALUES ('john.doe@example.com', 3, 120, TO_DATE('2024-11-25', 'YYYY-MM-DD'), 1, 1);
-- INSERT INTO OFFER (mailUserOffering, idSale, purchasePrice, dateOffer, quantity, wasAccepted) 
-- VALUES ('jane.smith@example.com', 4, 2300, TO_DATE('2024-11-25', 'YYYY-MM-DD'), 1, 0);

-- Insertion dans la table DOWNWARDSALE
-- INSERT INTO DOWNWARDSALE (idSale, discountStep) VALUES (2, 50);
-- INSERT INTO DOWNWARDSALE (idSale, discountStep) VALUES (4, 200);

-- Insertion dans la table LIMITEDTIME
-- INSERT INTO LIMITEDTIME (idSale, startDate, endDate) VALUES (2, TO_DATE('2024-11-01', 'YYYY-MM-DD'), TO_DATE('2024-11-30', 'YYYY-MM-DD'));
-- INSERT INTO LIMITEDTIME (idSale, startDate, endDate) VALUES (4, TO_DATE('2024-11-01', 'YYYY-MM-DD'), TO_DATE('2024-12-01', 'YYYY-MM-DD'));
