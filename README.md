Application Backend Gestion des absences
========================================


Ressource Absences
------------------------



URI path                         |  méthode HTTP  |  Description  
-------------------------------- | -------------- | --------------------------------------------------------------------------
**_/absences_**                  |  GET           |  retourne la liste des demandes d'absence au format JSON
**_/absences/{MATRICULE}_**      |  GET           |  retourne la liste des demandes d'absence d'un employé identifié par le matricule {MATRICULE}  au format JSON
**_/absences?statut={STATUT}_**  |  GET           |  retourne la liste des demandes d'absence en fonction de leur statut {STATUT} au format JSON
**_/absences_**                  |  POST          |  sauvegarde la demande d'absence et le retourne au format JSON
**_/absences/{ABSENCE_ID}_**     |  PUT           |  met à jour la demande et le retourne au format JSON
**_/absences/{ABSENCE_ID}_**     |  PATCH         |  met à jour le statut de la demande et le retourne au format JSON
**_/absences/{ABSENCE_ID}_**     |  DELETE        |  supprime la demande avec l'identifiant {ABSENCE_ID}



Ressource jours fériés
-----------------------


URI path                         |  méthode HTTP  |  Description     
-------------------------------- | -------------- | --------------------------------------------------------------------------
**_/jours_feries_**              |  GET           |  retourne la liste des jours fériés au format JSON
**_/jours_feries_**              |  POST          |  sauvegarde un jour férié et le retourne au format JSON
**_/jours_feries/{JOUR_FERIE_ID}_**  |  PUT       |  met à jour le jour férié et le retourne au format JSON
**_/jours_feries/{JOUR_FERIE_ID}_**  |  DELETE    |  supprime le jour férié avec l'identifiant {JOUR_FERIE_ID}



Authentification
-----------------

URI path                         |  méthode HTTP  |  Description     
-------------------------------- | -------------- | --------------------------------------------------------------------------
**_/login_**                     |  POST          |  authentifie et retourne le collaborateur authentifié au format JSON



Ressource Collaborateurs
------------------------


URI path                         |  méthode HTTP  |  Description  
-------------------------------- | -------------- | --------------------------------------------------------------------------
**_/collaborateurs_**            |  GET           |  retourne la liste des collaborateurs au format JSON
**_/collaborateurs/update-to-admin/{MATRICULE}_** |  PATCH         |  donne le rôle administrateur à un collaborateur et le retourne au format JSON
**_/collaborateurs/remove-from-admin/{MATRICULE}_** |  PATCH         |  enleve le rôle administrateur à un collaborateur et le retourne au format JSON

