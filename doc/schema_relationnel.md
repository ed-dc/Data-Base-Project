
# Schéma de Base de Données

## Tables et Relations

### SaleRoom
```sql
SaleRoom {
    int idSaleRoom (pk)          // not null, unique
    str nameCategory (fk)          // not null
}
```
**Relations**:  
- `SaleRoom.nameCategory` référence `Category.name`

---

### Offer
```sql
Offer {
    int idOffer (pk)             // not null, unique
    str mailUserOffering (fk)    // not null
    int idSale (fk)              // not null
    int purchasePrice            // not null
    timestamp dateOffer          // not null
    int quantity                 // not null
    bool wasAccepted             // nullable
}
```
**Relations**:  
- `Offer.mailUserOffering` référence `Usr.mail`  
- `Offer.idSale` référence `Sale.idSale`

---

### User
```sql
Usr {
    str email (pk)               // not null, unique
    int idAddress (fk)           // not null
    str lastName                 // not null
    str firstName                // not null
    str address                  // not null
}
```
**Relations**:  
- `Usr.idAddress` référence `Address.idAddress`

---

### Sale
```sql
Sale {
    int idSale (pk)              // not null, unique
    str mailUserSelling (fk)       // not null
    int idProduct (fk)           // not null
    int idSaleRoom (fk)          // not null
    int startingPrice            // not null
    bool isUpward                // not null
    bool isRevocable             // not null
    bool isLimited               // not null
    bool isUnique                // not null
    bool isFinish                // not null
    timestamp startTime          // not null
}
```
**Relations**:  
- `Sale.mailUserSelling` référence `Usr.mail`  
- `Sale.idProduct` référence `Product.idProduct`  
- `Sale.idSaleRoom` référence `SaleRoom.idSaleRoom`

---

### Product
```sql
Product {
    int idProduct (pk)           // not null, unique
    str nameCategory (fk)          // not null
    str name                     // not null
    int costPrice                // not null
    int quantity                 // not null
}
```
**Relations**:  
- `Product.nameCategory` référence `Category.name`

---

### Characteristic
```sql
Characteristic {
    str name (pk)                // not null
    int idProduct (fk)(pk)       // not null
    str value                    // not null
}
```
**Relations**:  
- `Characteristic.idProduct` référence `Product.idProduct`

---

### Category
```sql
Category {
    str name (pk)                // not null, unique
    str description              // not null
}
```



---

### DownwardSale
```sql
DownwardSale {
    int discountStep             // not null
    int idSale (pk)(fk)          // not null, unique
}
```
**Relations**:  
- `DownwardSale.idSale` référence `Sale.idSale`

---

### LimitedTime
```sql
LimitedTime {
    timestamp endTime            // not null
    int idSale (pk)(fk)          // not null, unique
}
```
**Relations**:  
- `LimitedTime.idSale` référence `Sale.idSale`

---

## Contraintes Non-Représentées
- Une catégorie a au moins un produit 

## Contraintes Additionnels

1. **Dates des Offres dans les Ventes Limitées**  
   - `LimitedTime.endTime >= Offer.dateOffer >= Sale.startTime`
        
        Dans une salle de vente à durée limitée, la date de début précède celles des offres associées, qui doivent précéder la date de fin.

2. **Interdiction d'Auto-Offre**  
   - `Offer.mailUserOffering != Sale.mailUserSelling`

        Le vendeur ne peux pas emettre des offres sur ses ventes.

3. **Quantité des Offres**  
   - `Offer.quantity <= Product.quantity`

        La quantité associée à une offre doit être inférerieure ou égale à la quantité disponible du produit en vente. 

4. **Ventes Uniques**  
   - Si `Sale.isUnique == True`, alors une seule offre par utilisateur sur un même produit est possible.

5. **Offres Montantes (`Sale.isUpward == True`)**  
   - Le champ `Offer.purchasePrice` pour une offre donnée doit être strictement supérieur à celui des autres offres associées au même produit et dont la valeur de `Offer.dateOffer` est antérieure.
   - `Offre.purchasePrice >= Sale.startingPrice`

6. **Valeurs par défaut**
    - Par défaut, les ventes sont montantes, non révocables, sans limite de temps et permettent à un même utilisateur d’enchérir plusieurs fois.


6. **Valeurs Minimums**  
   - `Product.quantity > 0`  
   - `Offer.quantity > 0`  
   - `Product.costPrice >= 0`  
   - `Sale.startingPrice >= 0`

7. **Couples Uniques**  
   - `(Characteristic.name, Characteristic.idProduct)` est unique

