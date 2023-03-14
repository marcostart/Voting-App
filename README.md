# Vote_App

VoteApp est une application web qui a pour but de simplifier le processus de vote. Elle est facile à prendre en main et peut être utilisée pour tous les types d'élections. Le système de sécurité mis en place sur cette plateforme est très performant, et les résultat sont automatiques.

## Installation

### Prérequis

- [NodeJS](https://nodejs.org/en/)
- [MySQL](https://www.mysql.com/fr/)
- [Java](https://www.java.com/fr/download/)
- JDK et JRE (en fonction du système d'exploitation)
- Un IDE (IntelliJ, Sts, VScode, etc...)
- [TailwindCSS](https://tailwindcss.com/docs/installation)


### Procédure pour Visual Studio Code

- Cloner le projet
- Ouvrir le projet dans VSCode
- Ouvrir un terminal dans le dossier du projet
- Lancer la commande `npm install` pour installer les dépendances
- (Facultatif) Télecharger les extensions suivantes serait d'une grande aide:
    - Spring Boot Dashboard
    - Spring Boot Tools
- Ouvrez le fichier pom.xml, faite un clic droit et appuyez sur 'reload projetc', cela devrait raffraichir le projet et installer toutes les dépendances du projet java (assurez vous d'etre connecté à internet)
- Ouvrez le fichier `src/main/resources/application.properties` et modifier les paramètres de connexion à la base de données avec les credentials de l'utilisateur créé précédemment
- Lancez le projet en vous servant du terminal ou de l'extension Spring Boot Dashboards;
- Après le lancement du serveur, vous pouvez récupérer le numéro du port dans la console puis accéder au site par l'adresse : "127.0.0.1/num_port"

#### Dès que vous lancez le projet, la base est automatiquement mise à jour !


- Créez les rôles "ROLE_USER" et "ROLE_ADMIN" dans la table "roles" de la base de données;
- Créez un utilisateur dans MySQL puis attribuez lui les droits d'administration:
    - Pour ce faire, la table "user_roles" établit la relation entre les utilisateurs et leurs rôles



### Procédure pour Spring Tool Suite ou Eclipse

Si vous avez déja le projet dans votre répertoire local, vous pouvez passer à l'étape suivante. Sinon, vous allez cloner le projet avec Spring Tool Suite. Voici les étapes à suivre pour cela:

Dans Sts, sélectionnez :
- Window > Show View > Other ...
- Puis, chercher une View nommée "Git Repositories", et cliquer "Open";
- Dans la fenêtre "Git Repositories" qui vient de s'ouvrir, cliquer sur "Clone a Git repository";
- Entrer votre username/password et l'adresse de GitHub Repository, puis, cliquer Next;
- Sélectionner un répertoire à utiliser comme un Local Repository (Référentiel local);
- Après avoir achevé "Clone", vous obtenez le dossier du projet dans le "Local Repository";

Ensuite, vous devez importer le projet vers le workspace en cours de Spring Tool Suite.

Dans Sts, sélectionnez :
- File > Import...
    - General > Existing Projects into Workspace
    - Next
- Sélectionner "Local Repository" en tant que répertoire racine pour importer les projets;
- Après l'importation, vous verrez les fichiers dans le workspace de Spring Tool Suite ou d'Eclipse.
- Ouvrez le fichier `src/main/resources/application.properties` et modifier les paramètres de connexion à la base de données avec les credentials de l'utilisateur créé précédemment
- Lancez le projet en vous servant du button run > Run As > Spring Boot App
- Après le lançement du serveur, vous pouvez récupérer le numéro du port dans la console puis accéder au site par l'adresse : "127.0.0.1/num_port"

#### Dès que vous lancez le projet, la base est automatiquement mise à jour !


- Créez les rôles "ROLE_USER" et "ROLE_ADMIN" dans la table "roles" de la base de données;
- Créez un utilisateur dans MySQL puis attribuez lui les droits d'administration:
    - Pour ce faire, la table "user_roles" établit la relation entre les utilisateurs et leurs rôles
# Voting-App
