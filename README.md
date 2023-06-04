# Projet CAF - HEG

Équipe RFJ : Robin Rolle, Victor Feller, Romain Jysch.

## Compréhension du projet

Ce projet démarre sur une base existante d'une application dont le but métier est la gestion d'une Caisse d'Allocation Familiales (CAF).
Le projet permet d'interagir avec la logique métier d'une CAF dont un algorithme permettant de définir quel parent bénéficie d'allocations selon différents paramètres.

L'application propose une API REST permettant diverses actions liées au contexte métier comme : 
- Déterminer quel parent bénéficie d'allocations
- Listing des allocataires/allocations selon certains critères
- Export d'allocations au format pdf

## Structure du projet

Le projet contient trois packages :

- integration-test
- main
- test

Notre package **main** contient deux sous-packages :

- Business
  - Contient toutes les classes concernant le corps métier
  - Par exemple, nous retrouvons nos classes métiers et la logique algorithmique
- Infrastructure
  - Contient toutes les classes techniques
  - On retrouve notamment les classes de gestion de l'API
  - Contient également le point d'entrée de l'application 
  - Un sous-package persistance contient, comme son nom l'indique, toutes les classes ayant un besoin de communication avec la base de données et c'est ici que nous gérons les transactions


Le package **test** contient des classes de tests unitaires des fonctionnalités de l'application.

Finalement, le package **integration-test** contient les tests d'intégrations réalisés en fin de projet. Ceux-ci ont pour but de tester les données en s'appuyant sur DBUnit.

## Compréhension des outils

**Librairies :**

1. pdfbox : Bibliothèque pour le traitement des fichiers PDF.
2. h2 : Base de données relationnelle en mémoire utilisée pour le développement et les tests.
3. flyway-core : Bibliothèque de gestion des migrations de base de données.
4. springfox-swagger2 : Bibliothèque pour générer la documentation Swagger pour les API REST.
5. HikariCP : Bibliothèque de pool de connexions JDBC haute performance.
6. jackson-core : Bibliothèque pour le traitement des données JSON.
7. aalto-xml : Bibliothèque pour le traitement des fichiers XML.
8. lombok : Bibliothèque pour la génération de code Java avec des annotations simplifiées.
9. dbunit : Bibliothèque pour la création de jeux de données de test dans les tests d'intégration.

**Frameworks :**

1. spring-boot-starter-web : Framework pour le développement web basé sur Spring Boot.
2. spring-boot-starter-test : Ensemble de bibliothèques et configurations pour faciliter les tests avec Spring Boot.
3. cucumber-java : Framework pour le développement des tests d'acceptation utilisant le langage Gherkin.
4. cucumber-junit : Framework pour l'exécution des tests d'acceptation avec Cucumber et JUnit.
5. mockito-core : Framework pour la création de mocks et de stubs lors des tests.
6. junit-jupiter-api : Framework pour les tests unitaires avec JUnit 5.
7. junit-jupiter-engine : Moteur d'exécution pour les tests unitaires avec JUnit 5.
8. junit-vintage-engine : Moteur d'exécution pour les anciens tests unitaires avec JUnit.

## Compétences acquises durant la réalisation du projet

Nous avons acquis de nombreuses compétences lors de la réalisation de ce projet.
Nous pouvons citer le Test Driven Development (TDD), l'utilisation de Mock, la maîtrise d'un IDE et des fonctionnalités offertes par celui-ci, la maîtrise des outils de debogage, GIT, travail d'équipe et répartitoin des tâches, clean-code, utilisation et lecture des logs et l'appropriation d'un projet existant en lui apportant des modifications.

## Choix effectués

Le projet avait une structure déjà prédéfinie, nous n'avons dû effectuer qu'un nombre de choix limités.


#### Choix n°1 : Utilisation des DTO
Pour remplacer la map contenant les paramètres de la requête du service REST. Nous avons décidé d'utiliser des DTO pour faciliter l'échange entre REST et Java.
Cela permet de travailler avec un objet concret au lieu d'une map.
Ce DTO (Data Transfert Object) nous permet de filtrer les attributs nécessaires lors de l'utilisation de l'API REST.
Un sous-choix lié à ce DTO a été de faire des méthodes de conversion de type Allocataire vers AllocataireDTO et inversément.
En effet, l'API REST nous retourne un AllocataireDTO alors que l'interaction avec la DB nécessite un Allocataire.

#### Choix n°2 : Amélioration de la gestion des transactions

Nous avons ajouté la possibilité de faire plusieurs types de transactions. Avec les interfaces Runnable et Supplier.
Car nous les utilisons dans le RESTcontroller pour simplifier les différents transactions que nous devons assurer pour le service.

#### Choix n°3 : Personnalisation des retours de l'API REST pour gérer les message de retours HTTP
Initialement, le projet ne gérait pas parfaitement les code de retours HTTP en cas d'erreur du serveur ou de requête incohérente.
Nous avons utilisé les options offertes par Spring pour personnaliser cela.

#### Choix n°4 : Gestion des exceptions
Nous avons pris le choix de gérer au mieux les exceptions au sein du projet.
Il a également été nécessaire pour le cas métier de décision de quel parent reçoit les allocations de lever une exception lorsque l'algorithme ne parvient pas à difinir précisément le parent en question.

## Difficultés rencontrées

Nous avons rencontré des difficultés quant à l'élaboration des tests. En effet, la notion de TDD et de mock est quelque chose de nouveau pour nous.
Il n'est pas aisé de "switcher" et de comprendre la notion des mocks lorsque nous débutons avec ceux-ci.
De même que de commencer par établir des tests avant de se lancer dans l'implémentation du code est quelque chose dont nous n'avons pas l'habitude.
Après plusieurs échanges avec le référent du projet, nous avons réussi à mettre correctement en place nos tests.

## Autres éléments intégrables

Nous aurions aimé rendre nos méthodes de conversions de type Allocataire/AllocataireDTO statiques pour plus de simplicité. Les inclures dans un package utils aurait été intéressant.
Nous avons complété le fichier IDEES_EVOLUTION.txt avec nous idées. Certaines ont été implémentées.

## Advent of Code

https://github.com/robinrolle/advent-of-code
