Application Backend Gestion des absences
========================================


Ressource Absence
-------------------


<<<<<<< HEAD

URI path                         |  méthode HTTP  |  Description  
-------------------------------- | -------------- | --------------------------------------------------------------------------
**_/absences/{MATRICULE}_**      |  GET           |  retourne la liste des demandes d'absence d'un employé identifié par le matricule {MATRICULE}  au format JSON
**_/absences?statut={STATUT}_**  |  GET           |  retourne la liste des demandes d'absence en fonction de leur statut {STATUT} au format JSON
**_/absences_**                  |  POST          |  sauvegarde la demande d'absence et le retourne au format JSON
**_/absences/{ABSENCE_ID}_**     |  PUT           |  met à jour la demande et le retourne au format JSON
**_/absences/ABSENCE_ID_**       |  PATCH         |  met à jour le statut de la demande et le retourne au format JSON
=======
URI path                         |  méthode HTTP  |  Description     
-------------------------------- | -------------- | --------------------------------------------------------------------------
**_/absences/{ABSENCE_ID}_**     |  DELETE        |  supprime la demande avec l'identifiant {ABSENCE_ID}
>>>>>>> refs/remotes/origin/api-supprimer-absence
