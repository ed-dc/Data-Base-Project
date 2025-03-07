/*
    DAO_Queries.sql

    Description : 
    Ce fichier regroupe les requêtes SQL utilisées par les DAO du projet pour gérer 
    les enchères, utilisateurs, salles de vente et ventes. Chaque requête est documentée 
    avec sa fonction et ses paramètres pour une permettre une meilleure lisibilité.
*/

----------------------------------------------------------------------------------------------------------------------------

/* EnchereDAO */

/* Récupère le prix de revient d'un produit spécifique.
   Paramètre : idProduct (identifiant du produit) */
SELECT costPrice FROM PRODUCT WHERE idProduct = ?;

/* Insère une nouvelle offre dans la table OFFER.
   Paramètres : mailUserOffering (email de l'utilisateur), idSale (id de la vente), 
                purchasePrice (prix proposé), dateOffer (date de l'offre), 
                quantity (quantité proposée), wasAccepted (offre acceptée ou non) */
INSERT INTO OFFER (mailUserOffering, idSale, purchasePrice, dateOffer, quantity, wasAccepted) 
VALUES (?,?,?,?,?,?);

/* Récupère toutes les offres faites par un utilisateur spécifique.
   Paramètre : mailUserOffering (email de l'utilisateur) */
SELECT * FROM OFFER WHERE mailUserOffering = ?;

/* Récupère la quantité d'une offre pour une vente donnée et un utilisateur.
   Paramètres : mailUserOffering (email de l'utilisateur), idSale (id de la vente) */
SELECT quantity FROM OFFER WHERE mailUserOffering = ? AND idSale = ?;

/* Récupère le prix de départ d'une vente pour un produit spécifique.
   Paramètre : idProduct (identifiant du produit) */
SELECT startingPrice FROM SALE WHERE idProduct = ?;

/* Récupère la quantité disponible pour un produit donné.
   Paramètre : idProduct (identifiant du produit) */
SELECT quantity FROM PRODUCT WHERE idProduct = ?;

/* Récupère l'utilisateur ayant effectué l'enchère la plus élevée pour une vente ascendante.
   Paramètre : idSale (id de la vente) */
SELECT o.idOffer, o.mailUserOffering, o.purchasePrice, o.dateOffer
FROM Offer o, Sale s
WHERE o.idSale = s.idSale
AND s.isUpward = 1
AND o.purchasePrice = (
    SELECT MAX(purchasePrice)
    FROM Offer
    WHERE idSale = s.idSale
);

----------------------------------------------------------------------------------------------------------------------------
/* FinEnchereDAO */

/* Vérifie si une vente spécifique est terminée.
   Paramètre : idSale (identifiant de la vente) */
SELECT isFinish FROM SALE WHERE idSale = ?;

/* Récupère l'heure de fin d'une vente limitée dans le temps.
   Paramètre : idSale (identifiant de la vente) */
SELECT endTime FROM LIMITEDTIME WHERE idSale = ?;

/* Récupère l'heure de début d'une vente spécifique.
   Paramètre : idSale (identifiant de la vente) */
SELECT startTime FROM SALE WHERE idSale = ?;

/* Récupère l'heure de la dernière offre si elle existe
   Paramètre : idSale (identifiant de la vente) */
SELECT MAX(dateOffer) AS lastOfferDate FROM OFFER WHERE idSale = ?;

----------------------------------------------------------------------------------------------------------------------------
/* SalleVenteDAO */

/* Récupère les identifiants et noms des catégories des salles de vente. */
SELECT idSaleRoom, nameCategory FROM SALEROOM;

/* Récupère toutes les ventes associées à une salle de vente spécifique.
   Paramètre : idSaleRoom (identifiant de la salle de vente) */
SELECT * FROM SALE WHERE idSaleRoom = ?;

/* Récupère toutes les ventes enregistrées dans la base de données. */
SELECT * FROM SALE;

/* Insère une nouvelle catégorie de salle de vente dans la table SALEROOM.
   Paramètre : nameCategory (nom de la catégorie) */
INSERT INTO SALEROOM (nameCategory) VALUES (?);

/* Récupère le nom de la catégorie pour une salle de vente spécifique.
   Paramètre : idSaleRoom (identifiant de la salle de vente) */
SELECT nameCategory FROM SALEROOM WHERE idSaleRoom = ?;

----------------------------------------------------------------------------------------------------------------------------
/* UserDAO */

/* Insère un nouvel utilisateur dans la table USR.
   Paramètres : email (email de l'utilisateur), lastName (nom de famille), 
                firstName (prénom), address (adresse) */
INSERT INTO USR (email, lastName, firstName, address) VALUES (?, ?, ?, ?);

/* Récupère les informations d'un utilisateur spécifique.
   Paramètre : email (email de l'utilisateur) */
SELECT * FROM USR WHERE EMAIL = ?


----------------------------------------------------------------------------------------------------------------------------

/* VenteDAO */

/* Insère une vente limitée dans le temps dans la table LIMITEDTIME.
   Paramètres : 
   - endTime (heure de fin)
   - idSale (identifiant de la vente) */
INSERT INTO LIMITEDTIME (endTime, idSale) VALUES (?, ?);

/* Insère une vente dégressive dans la table DOWNWARDSALE.
   Paramètres : 
   - discountStep (montant de réduction par étape)
   - idSale (identifiant de la vente) */
INSERT INTO DOWNWARDSALE (discountStep, idSale) VALUES (?, ?);

/* Récupère l'heure de début d'une vente pour un produit spécifique.
   Paramètres : 
   - idSale (identifiant de la vente)
   - idProduct (identifiant du produit) */
SELECT startTime FROM SALE WHERE idSale = ? AND idProduct = ?;

/* Récupère l'étape de réduction pour une vente dégressive.
   Paramètre : idSale (identifiant de la vente) */
SELECT discountStep FROM DOWNWARDSALE WHERE idSale = ?;

/* Récupère l'identifiant d'une vente à partir de son rowid.
   Paramètre : rowid (identifiant de ligne interne à Oracle) */
SELECT idSale FROM SALE WHERE rowid = ? 
