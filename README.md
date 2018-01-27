Application Backend Gestion des absences
========================================


Ressource Collaborateurs
------------------------



URI path                         |  méthode HTTP  |  Description  
-------------------------------- | -------------- | --------------------------------------------------------------------------
**_/collaborateurs_**            |  GET           |  retourne la liste des collaborateurs au format JSON
**_/collaborateurs/update-to-admin/{MATRICULE}_** |  PATCH         |  donne le rôle administrateur à un collaborateur et le retourne au format JSON
**_/collaborateurs/remove-from-admin/{MATRICULE}_** |  PATCH         |  enleve le rôle administrateur à un collaborateur et le retourne au format JSON