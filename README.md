Application Backend Gestion des absences
========================================


Ressource Absence
-------------------


URI path                         |  méthode HTTP  |  Description  
-------------------------------- | -------------- | --------------------------------------------------------------------------
**_/absences/{MATRICULE}_**      |  GET           |  retourne la liste des demandes d'absence d'un employé identifié par le matricule {MATRICULE}  au format JSON
**_/absences?statut={STATUT}_**  |  GET           |  retourne la liste des demandes d'absence en fonction de leur statut {STATUT} au format JSON
**_/absences_**                  |  POST          |  sauvegarde la demande d'absence et le retourne au format JSON
