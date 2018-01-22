Application Backend Gestion des absences
========================================


Ressource jours fériés
-----------------------


URI path                         |  méthode HTTP  |  Description     
-------------------------------- | -------------- | --------------------------------------------------------------------------
**_/jours_feries_**              |  GET           |  retourne la liste des jours fériés au format JSON
**_/jours_feries_**              |  POST          |  sauvegarde un jour férié et le retourne au format JSON
**_/jours_feries/{JOUR_FERIE_ID}_**           |  PUT           |  met à jour le jour férié et le retourne au format JSON
**_/jours_feries/{JOUR_FERIE_ID}_**              |  DELETE          |  supprime le jour férié avec l'identifiant {JOUR_FERIE_ID}
