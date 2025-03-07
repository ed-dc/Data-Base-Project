# Projet Base de Données

## Documentation

Enfin, nous allons voir comment utiliser notre système d'enchères.

### Lancer le système d'enchère

Pour lancer le système d'enchères, il suffit de se placer à la racine du projet et d'exécuter la commande suivante :

```
make run
```

Il n'y a pas de prérequis particuliers pour lancer cette commande, si ce n'est d'avoir Java installé sur son ordinateur (ainsi que les packages classiques).

### Les possibilités offertes par notre système

Lorsque le système d'enchères est lancé, l'utilisateur a la possibilité de se connecter ou de créer un compte en renseignant les données nécessaires. Une fois connecté, l'utilisateur a accès à un large éventail de fonctionnalités :
- Créer une salle de vente en choisissant sa catégorie
- Ajouter un produit dans la base de données
- Créer une vente dans la salle de son choix en précisant si elle est montante ou descendante, à durée libre ou limitée, révocable ou non, et si un utilisateur est autorisé à faire plusieurs offres sur cette vente
- Faire une offre en sélectionnant d'abord la salle de vente, puis la vente qui l’intéresse
- Mettre fin aux ventes qu'il a créées : l'utilisateur peut alors voir le nom du gagnant de la vente et le prix auquel le produit a été vendu
- Rafraîchir l'état des offres
- Se déconnecter

## Connection à la base

la connection ne peut se faire seulement sur les serveurs de l'école.

