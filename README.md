Application Backend Gestion des absences
========================================


Ressource Absence
-------------------


URI path                         |  méthode HTTP  |  Description     
-------------------------------- | -------------- | --------------------------------------------------------------------------
**_/absences/{MATRICULE}_**      |  GET           |  retourne la liste des demandes d'absence d'un employé identifié par le matricule {MATRICULE}  au format JSON
**_/absences/{MATRICULE}?statut=STATUT_**      |  GET           |  retourne la liste des demandes d'absence, en fonction de leur statut, d'un employé identifié par le matricule {MATRICULE} au format JSON