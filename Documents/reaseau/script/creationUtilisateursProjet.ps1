# Nom et prénom : Mai, Giang Tien
# Matricule : 1621737
# Date de création : 15 novembre 2024
# Objectif : Supression, création des utilisateurs à partir d'un CSV + organiser dans OU
# Serveur d'exécution : ###
# Serveurs modifiés : ###

# CRÉATION SUPPRESSION DES GROUPES
Clear-Host
$cheminGroupe = "OU=GROUPES,OU=INFORMATIQUE, DC=ETU1621737,DC=LOCAL"
$Etendue = "Global"
$Type = "Security"
$Noms = "grWebEmp", "grWebGest", 
        "grAndroidProgrammersEmp", "grAndroidProgrammersGest", 
        "grPythonProgrammersEmp", "grPythonProgrammersGest", 
        "grTechniciansEmp", "grTechniciansGest",
        "grAndroidTestersEmp", "grAndroidTestersGest", 
        "grPythonTestersEmp","grPythonTestersGest",
        "grEmp", "grGest", "grCompagnie"

try
{
  # DELETE GROUPS IF EXISTS

  Get-ADGroup -SearchBase $cheminGroupe `
              -Filter * | Remove-ADGroup -Confirm:$false

  Write-Host "$chemin existe, donc on l'efface." -ForegroundColor Green

}

catch [Microsoft.ActiveDirectory.Management.Commands.GetADGroup]
{
  Write-Host "$chemin n'existe pas." -ForegroundColor Yellow
}

# Supprimer share _S1_PERSO
$cheminPartage = "\\S1\E$\_S1_PERSO"
Remove-SmbShare -Name "_S1_PERSO$" -Force -CIMSession "S1" -ErrorAction SilentlyContinue
Write-Host "Partage SMB '_S1_PERSO$' supprimé." -ForegroundColor Green

# Supprimer dossier _S1_PERSO
Remove-Item -Path $cheminPartage -Recurse -Force -ErrorAction SilentlyContinue
Write-Host "Dossier '_S1_PERSO' supprimé." -ForegroundColor Green



# création des groupes
Foreach($Nom in $Noms){
    New-ADGroup -Name "$Nom" `
                -GroupScope "$Etendue" `
                -GroupCategory "$Type"  `
                -Path "$cheminGroupe"
    Write-Host "$Nom est créé" -ForegroundColor Green
}

# Création du dossier personnel principal _S1_PERSO
$cheminFile = "\\S1\E$\_S1_PERSO"

New-Item -Path $cheminFile `
         -ItemType directory

# Désactiver l'héritage sur le fichier principal
icacls.exe $cheminFile /inheritance:r

$groupeCie = "grCompagnie"

# Ajuster les droits sur le chemin
icacls.exe $cheminFile /grant "Administrateurs:(OI)(CI)(F)"   # Administrateurs
icacls.exe $cheminFile /grant "*S-1-5-18:(OI)(CI)(F)"         # Système
icacls.exe $cheminFile /grant "*S-1-3-4:(OI)(CI)(M)"          # Droits du propriétaire
icacls.exe $cheminFile /grant $groupeCie":(RX)"      # Tous les employés et gestionnaires

# Avec New-SMBShare, le chemin est toujours un chemin local
# si le dossier est distant, il faut utiliser le paramètre -CIMSession
New-SMBShare -Name "_S1_PERSO$" `
             -Path "E:\_S1_PERSO" `
             -FullAccess "Tout le monde" `
             -FolderEnumerationMode AccessBased `
             -EncryptData $True `
             -CachingMode none `
             -CIMSession "S1"


# CRÉATION SUPPRESSION UTILISATEURS

# Fichier CSV
$fCsv = "PRATIQUE_ETU_A2024.csv"

# Importation du fichier CSV
$oCsv = Import-Csv -Path $fCsv -Delimiter ";"

# Paramètres des OUs pour chaque département et rôle
$ouWebEmp = "OU=EMP,OU=CONCEPTEURS_WEB,OU=Utilisateurs,OU=INFORMATIQUE,DC=ETU1621737,DC=LOCAL"
$ouWebGest = "OU=GEST,OU=CONCEPTEURS_WEB,OU=Utilisateurs,OU=INFORMATIQUE,DC=ETU1621737,DC=LOCAL"

$ouAndroidProgrammersEmp = "OU=EMP,OU=PROGRAMMATION_ANDROID,OU=Utilisateurs,OU=INFORMATIQUE,DC=ETU1621737,DC=LOCAL"
$ouAndroidProgrammersGest = "OU=GEST,OU=PROGRAMMATION_ANDROID,OU=Utilisateurs,OU=INFORMATIQUE,DC=ETU1621737,DC=LOCAL"

$ouPythonProgrammersEmp = "OU=EMP,OU=PROGRAMMATION_PYTHON,OU=Utilisateurs,OU=INFORMATIQUE,DC=ETU1621737,DC=LOCAL"
$ouPythonProgrammersGest = "OU=GEST,OU=PROGRAMMATION_PYTHON,OU=Utilisateurs,OU=INFORMATIQUE,DC=ETU1621737,DC=LOCAL"

$ouTechniciansEmp = "OU=EMP,OU=TECHNICIENS,OU=Utilisateurs,OU=INFORMATIQUE,DC=ETU1621737,DC=LOCAL"
$ouTechniciansGest = "OU=GEST,OU=TECHNICIENS,OU=Utilisateurs,OU=INFORMATIQUE,DC=ETU1621737,DC=LOCAL"

$ouAndroidTestersEmp = "OU=EMP,OU=TESTEURS_ANDROID,OU=Utilisateurs,OU=INFORMATIQUE,DC=ETU1621737,DC=LOCAL"
$ouAndroidTestersGest = "OU=GEST,OU=TESTEURS_ANDROID,OU=Utilisateurs,OU=INFORMATIQUE,DC=ETU1621737,DC=LOCAL"

$ouPythonTestersEmp = "OU=EMP,OU=TESTEURS_PYTHON,OU=Utilisateurs,OU=INFORMATIQUE,DC=ETU1621737,DC=LOCAL"
$ouPythonTestersGest = "OU=GEST,OU=TESTEURS_PYTHON,OU=Utilisateurs,OU=INFORMATIQUE,DC=ETU1621737,DC=LOCAL"

$compteur = 0

# Supprimer les utilisateurs sous Utilisateurs
try
{
    Get-ADUser -Filter * -SearchBase "OU=Utilisateurs,OU=INFORMATIQUE,DC=ETU1621737,DC=LOCAL" | Remove-ADUser -Confirm:$false
}

catch [Microsoft.ActiveDirectory.Management.ADIdentityNotFoundException]
{
    Write-Host "user n'existe pas." -ForegroundColor Yellow
}




# Parcourir chaque ligne du CSV
Foreach ($Ligne in $oCsv){

   $Matricule = $Ligne.MATRICULE
   $Nom = $Ligne.NOM
   $Prenom = $Ligne.PRENOM
   $Code = $Ligne.CODE

   # Créer le login (SamAccountName) à partir du Matricule
   $Login = "$Matricule"  # Login est directement basé sur le matricule

   # Créer le nom complet en combinant Prénom, Nom, et Code
   $NomComplet = "$Prenom $Nom – $Code"

   # Déterminer le département et l'OU en fonction du Matricule
   $chemin = ""
   $Role = ""
   $Groupes = ""

   # Gestionnaire ou Employé en fonction du matricule
   if ($Matricule -in 10000..19999) {
       # OU=PROGRAMMATION_PYTHON
       if ($Matricule -eq 10000) {
           $chemin = $ouPythonProgrammersGest
           $Role = "Gestionnaire"
           $Groupes = "grPythonProgrammersGest", "grGest", "grCompagnie"
       } else {
           $chemin = $ouPythonProgrammersEmp
           $Role = "Employé"
           $Groupes = "grPythonProgrammersEmp", "grEmp", "grCompagnie"
       }
   }
   elseif ($Matricule -in 20000..29999) {
       # OU=PROGRAMMATION_ANDROID
       if ($Matricule -eq 20000) {
           $chemin = $ouAndroidProgrammersGest
           $Role = "Gestionnaire"
           $Groupes = "grAndroidProgrammersGest", "grGest", "grCompagnie"
       } else {
           $chemin = $ouAndroidProgrammersEmp
           $Role = "Employé"
           $Groupes = "grAndroidProgrammersEmp", "grEmp", "grCompagnie"
       }
   }
   elseif ($Matricule -in 30000..39999) {
       # OU=TECHNICIENS
       if ($Matricule -eq 30000) {
           $chemin = $ouTechniciansGest
           $Role = "Gestionnaire"
           $Groupes = "grTechniciansGest", "grGest", "grCompagnie"
       } else {
           $chemin = $ouTechniciansEmp
           $Role = "Employé"
           $Groupes = "grTechniciansEmp", "grEmp", "grCompagnie"
       }
   }
   elseif ($Matricule -in 40000..49999) {
       # OU=CONCEPTEURS_WEB
       if ($Matricule -eq 40000) {
           $chemin = $ouWebGest
           $Role = "Gestionnaire"
           $Groupes = "grWebGest", "grGest", "grCompagnie"
       } else {
           $chemin = $ouWebEmp
           $Role = "Employé"
           $Groupes = "grWebEmp", "grEmp", "grCompagnie"
       }
   }
   elseif ($Matricule -in 50000..59999) {
       # OU=TESTEURS_PYTHON
       if ($Matricule -eq 50000) {
           $chemin = $ouPythonTestersGest
           $Role = "Gestionnaire"
           $Groupes = "grPythonTestersGest", "grGest", "grCompagnie"
       } else {
           $chemin = $ouPythonTestersEmp
           $Role = "Employé"
           $Groupes = "grPythonTestersEmp", "grEmp", "grCompagnie"
       }
   }
   elseif ($Matricule -in 60000..69999) {
       # OU=TESTEURS_ANDROID
       if ($Matricule -eq 60000) {
           $chemin = $ouAndroidTestersGest
           $Role = "Gestionnaire"
           $Groupes = "grAndroidTestersGest", "grGest", "grCompagnie"
       } else {
           $chemin = $ouAndroidTestersEmp
           $Role = "Employé"
           $Groupes = "grAndroidTestersEmp", "grEmp", "grCompagnie"
       }
   }

   # Définir une description significative
   $Description = "Utilisateur créé pour le code $Code, $Nom $Prenom - Role: $Role"

   # Vérifier si l'utilisateur existe
   $resultat = Get-ADUser -Filter "SamAccountName -eq '$Login'"

   # Si l'utilisateur n'existe pas, le créer
   if ($resultat -eq $null){

       # Définir un mot de passe par défaut
       $mdp = ConvertTo-SecureString -AsPlainText "AAAaaa111" -Force

       # CRÉER L'UTILISATEUR
       New-ADUser -Name "$Prenom $Nom – $Login" `
                  -SamAccountName $Login `
                  -UserPrincipalName "$Login@etu1641292.local" `
                  -Path $chemin `
                  -GivenName "$Prenom" `
                  -Surname "$Nom" `
                  -DisplayName "$NomComplet" `
                  -Description "$Description" `
                  -AccountPassword $mdp `
                  -PasswordNeverExpires $true `
                  -Enabled $true

        # AJOUTER UTILISATEURS AUX GROUPES CORRESPONDANTS
        # La commande ajoute plusieurs groupes à un utilisateur
          Add-ADPrincipalGroupMembership -Identity $Login `
                                         -MemberOf $Groupes

        $compteur++
        Write-Host "$Login est créé."

        # CRÉER SON DOSSIER PERSONNEL
        New-Item -Path "\\S1\_S1_PERSO$\$Login" `
                 -ItemType directory

        icacls.exe \\S1\_S1_PERSO$\$Login /grant $Login":(OI)(CI)(M)"

        # IMPORTANT: Il ne faut pas utiliser le chemin suivant: \\SERVEUR1\E$\_PERSO2\U2
        Set-ADUser -Identity "$Login" `
                   -HomeDrive "P:" `
                   -HomeDirectory "\\S1\_S1_PERSO$\$Login"
        

   } else {
        Write-Host "$Login existe déjà."
   }
}

Write-Host "Création de $compteur utilisateurs."



