# Vélos Libre Service - Projet de Conception Orientée Objet (COO) en Java


Ce projet de simulation vise à modéliser un système de location de vélos en libre-service, inspiré par des services tels que Vélib' ou V'Lille. La simulation comprend la gestion des stations de location, des vélos, d'un centre de contrôle, et intègre des fonctionnalités telles que la redistribution des vélos, les réparations, et la gestion des vols.

## Fonctionnalités

1. **Stations de Location :**
   - Les stations sont représentées par la classe `Station`, qui prend plusieurs types de véhicules de la classe `Rentable`, paramétrée par des classes de type `CanBeRented` qui représentent tout véhicule.
   - Chaque station est définie par un identifiant unique et une capacité d'accueil (de 10 à 20 emplacements).
   - Un véhicule peut être déposé dans un emplacement vide et retiré d'un emplacement occupé.

2. **Types de Vélos :**
   - Les vélos sont représentés par les classes `Bike`, `ClassicBike` et `ElectricBike`. Ces classes sont passées en paramètre à la classe `Rentable` qui représente les véhicules.
   - Les vélos peuvent être classiques ou à assistance électrique, équipés ou non d'un panier ou de porte-bagages.
   - La possibilité d'introduire d'autres types de vélos, tels que les vélos pliants, est envisageable grâce à la classe paramétrée `Rentable` qui prend tous les sous-types de la classe `CanBeRented`.

3. **Centre de Contrôle :**
   - Le centre de contrôle est représenté par la classe `ControlCenter`, qui connaît l'ensemble des stations et véhicules.
   - Il supervise l'ensemble de la flotte de vélos et des stations.
   - Il est notifié à chaque dépôt ou location de vélo.
   - Il est responsable de la redistribution des vélos entre les stations lorsque certaines stations restent vides ou pleines trop longtemps.

4. **Redistribution des Vélos :**
   - À intervalles de temps, des dépôts et des retraits de vélos sont effectués grâce à la simulation implémentée dans le `ControlCenter`.
   - Si une station reste vide ou pleine pendant plus de deux intervalles de temps (correspondant à 2 * 5 unités de temps dans notre représentation), le centre de contrôle décide de la redistribution des vélos. Les méthodes de redistribution sont représentées par l'interface `RedistributionStrategy`, la stratégie implémentée par défaut étant `EqualRedistribution`.

5. **Hors-Service et Réparations :**
   - Après un certain nombre de locations (5 par défaut), un vélo devient hors-service.
   - Le centre de contrôle s'occupe d'envoyer un réparateur pour effectuer une vérification du vélo, pendant laquelle le vélo ne peut ni être retiré ni redistribué.

6. **Interactions avec d'autres Corps de Métier :**
   - Les corps de métiers sont représentés par la classe `Staff`, le seul disponible pour l'instant étant le réparateur.

7. **Gestion des Vols :**
   - Un vélo peut être volé avec une chance de 1% s'il reste le seul vélo non retiré d'une station pendant deux intervalles de temps.
   - Un vélo volé est retiré du système de libre-service.

8. **Simulation :**
   - La fonction de simulation est intégrée au `ControlCenter`. Elle a été conçue pour que toutes les fonctionnalités soient visibles au moment de l'exécution. Par exemple, elle crée un certain déséquilibre dans les opérations de location et de dépôt pour provoquer les redistributions et les vols, en donnant un poids à chaque station pour simuler les stations à forte pression dans le monde réel.

## Design Patterns Utilisés

- **Singleton :** Utilisé pour le `ControlCenter` afin de garantir une seule instance du centre de contrôle dans l'ensemble du système, assurant ainsi une coordination cohérente.

- **Strategy :** Employé pour les stratégies de redistribution avec la classe `RedistributionStrategy`. Les différentes stratégies (par exemple, `EqualRedistribution`) peuvent être interchangées dynamiquement selon les besoins.

- **Factory Method :** Utilisé dans la création de méthodes de test pour instancier des objets spécifiques nécessaires aux scénarios de test.

- **Observer :** Implémenté pour observer et réagir aux opérations telles que le dépôt, la location, la vérification de la redistribution, la vérification des vols, et l'envoi du réparateur. Cela permet une gestion flexible des événements dans le système.

## Conception du Programme

Le programme est conçu de manière modulaire pour faciliter l'extension avec de nouveaux types de véhicules et d'interactions. La programmation orientée objet est utilisée pour une gestion efficace des entités (vélos, stations, etc.) et de leurs comportements.

## Tests

- Une suite de tests complète en JUnit 5 est fournie pour garantir le bon fonctionnement de toutes les fonctionnalités implémentées. Ces tests couvrent différents scénarios d'utilisation, y compris les cas limites et les situations exceptionnelles.
- Des mocks en été employé pour créer des objets factices lors des tests unitaires afin d'isoler les composants et de vérifier le comportement souhaité.

## Comment Utiliser le Programme

- Compilation et création du Jar : make
- Exécution : make run
- Création de la doc : make docs
- Exécution des tests : make test  

## Perspectives d'Amélioration

Le projet peut être étendu en ajoutant de nouvelles fonctionnalités telles que la prise en charge de nouveaux types de véhicules.

---
