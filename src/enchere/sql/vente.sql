/*
Fichier de tests insérant des données dans les tables SALE et OFFER pour simuler des ventes et des offres.
*/

-- INSERT INTO SALE (
--     mailUserSelling, idProduct, idSaleRoom, startingPrice, isUpward, 
--     isRevocable, isLimited, isUnique, isFinish, startTime
-- ) 
-- VALUES (
--     'lois', 3, 4, 200, 1, 1, 1, 0, 0, 
--     TO_TIMESTAMP('2024-11-26 21:45:00', 'YYYY-MM-DD HH24:MI:SS')
-- );

-- INSERT INTO OFFER (
--     mailUserOffering, idSale, purchasePrice, dateOffer, quantity, wasAccepted
-- ) 
-- VALUES (
--     'gagnant@gmail.com', 29, 500,
--     TO_TIMESTAMP('2024-11-26 21:46:00', 'YYYY-MM-DD HH24:MI:SS'),
--     2, 0
-- );
-- 
-- 
-- INSERT INTO OFFER (
--     mailUserOffering, idSale, purchasePrice, dateOffer, quantity, wasAccepted
-- ) 
-- VALUES (
--     'perdant@gmail.com', 29, 600,
--     TO_TIMESTAMP('2024-11-26 21:46:00', 'YYYY-MM-DD HH24:MI:SS'),
--     2, 0
-- );
-- 
-- INSERT INTO OFFER (
--     mailUserOffering, idSale, purchasePrice, dateOffer, quantity, wasAccepted
-- ) 
-- VALUES (
--     'gagnant@gmail.com', 29, 500,
--     TO_TIMESTAMP('2024-11-26 21:46:00', 'YYYY-MM-DD HH24:MI:SS'),
--     1, 0
-- );
-- 
-- 
-- INSERT INTO OFFER (
--     mailUserOffering, idSale, purchasePrice, dateOffer, quantity, wasAccepted
-- ) 
-- VALUES (
--     'gagnant@gmail.com', 29, 500,
--     TO_TIMESTAMP('2024-11-26 21:40:00', 'YYYY-MM-DD HH24:MI:SS'),
--     2, 0
-- );

-- INSERT INTO SALE ( mailUserSelling, idProduct, idSaleRoom, startingPrice, isUpward, isRevocable, isLimited, isUnique, isFinish, startTime) VALUES ( 'lois', 3, 4, 300, 0, 1, 1, 0, 0, TO_TIMESTAMP('2024-11-26 21:45:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO OFFER (
    mailUserOffering, idSale, purchasePrice, dateOffer, quantity, wasAccepted
) 
VALUES (
    'gagnant@gmail.com', 30, 300,
    TO_TIMESTAMP('2024-11-26 22:29:50', 'YYYY-MM-DD HH24:MI:SS'),
    2, 0
);


INSERT INTO OFFER (
    mailUserOffering, idSale, purchasePrice, dateOffer, quantity, wasAccepted
) 
VALUES (
    'perdant@gmail.com', 30, 300,
    TO_TIMESTAMP('2024-11-26 22:29:40', 'YYYY-MM-DD HH24:MI:SS'),
    2, 0
);

INSERT INTO OFFER (
    mailUserOffering, idSale, purchasePrice, dateOffer, quantity, wasAccepted
) 
VALUES (
    'gagnant@gmail.com', 30, 200,
    TO_TIMESTAMP('2024-11-26 22:30:01', 'YYYY-MM-DD HH24:MI:SS'),
    1, 0
);
