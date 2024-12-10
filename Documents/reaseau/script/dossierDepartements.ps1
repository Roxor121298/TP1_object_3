# DOSSIER RACINE _S1_INFO
$cheminFile = "\\S1\E$\_S1_INFO"

# Supprimer share _S1_INFO
$cheminPartage = "\\S1\E$\_S1_INFO"
Remove-SmbShare -Name "_S1_INFO$" -Force -CIMSession "S1" -ErrorAction SilentlyContinue
Write-Host "Partage SMB '_S1_INFO$' supprimé." -ForegroundColor Green

# Supprimer dossier _S1_INFO
Remove-Item -Path $cheminPartage -Recurse -Force -ErrorAction SilentlyContinue
Write-Host "Dossier '_S1_INFO' supprimé." -ForegroundColor Green

New-Item -Path $cheminFile `
         -ItemType directory

# Désactiver l'héritage sur le fichier principal
icacls.exe $cheminFile /inheritance:r
$groupeCie = "grCompagnie"

# Ajuster les droits sur le chemin
icacls.exe $cheminFile /grant "Administrateurs:(OI)(CI)(F)"   # Administrateurs
icacls.exe $cheminFile /grant "*S-1-5-18:(OI)(CI)(F)"         # Système
icacls.exe $cheminFile /grant "*S-1-3-4:(OI)(CI)(M)"          # Droits du propriétaire
icacls.exe $cheminFile /grant $groupeCie":(RX)"       # Tous les employés et gestionnaires

# Avec New-SMBShare, le chemin est toujours un chemin local
# si le dossier est distant, il faut utiliser le paramètre -CIMSession
New-SMBShare -Name "_S1_INFO$" `
             -Path "E:\_S1_INFO" `
             -FullAccess "Tout le monde" `
             -FolderEnumerationMode AccessBased `
             -EncryptData $True `
             -CachingMode none `
             -CIMSession "S1"

# CRÉER DOSSIERS DU DEPARTEMENT
New-Item -Path "\\S1\_S1_INFO$\CONCEPTEURS_WEB" `
         -ItemType directory

icacls.exe "\\S1\_S1_INFO$\CONCEPTEURS_WEB" /grant "grWebEmp:(OI)(CI)(M)"
icacls.exe "\\S1\_S1_INFO$\CONCEPTEURS_WEB" /grant "grWebGest:(OI)(CI)(F)"

New-Item -Path "\\S1\_S1_INFO$\PROGRAMMATION_ANDROID" `
         -ItemType directory

icacls.exe "\\S1\_S1_INFO$\CONCEPTEURS_WEB" /grant "grAndroidProgrammersEmp:(OI)(CI)(M)"
icacls.exe "\\S1\_S1_INFO$\CONCEPTEURS_WEB" /grant "grAndroidProgrammersGest:(OI)(CI)(F)"

New-Item -Path "\\S1\_S1_INFO$\PROGRAMMATION_PYTHON" `
         -ItemType directory

icacls.exe "\\S1\_S1_INFO$\CONCEPTEURS_WEB" /grant "grPythonProgrammersEmp:(OI)(CI)(M)"
icacls.exe "\\S1\_S1_INFO$\CONCEPTEURS_WEB" /grant "grPythonProgrammersGest:(OI)(CI)(F)"

New-Item -Path "\\S1\_S1_INFO$\TECHNICIENS" `
         -ItemType directory

icacls.exe "\\S1\_S1_INFO$\CONCEPTEURS_WEB" /grant "grTechniciansEmp:(OI)(CI)(M)"
icacls.exe "\\S1\_S1_INFO$\CONCEPTEURS_WEB" /grant "grTechniciansGest:(OI)(CI)(F)"

New-Item -Path "\\S1\_S1_INFO$\TESTEURS_ANDROID" `
         -ItemType directory

icacls.exe "\\S1\_S1_INFO$\CONCEPTEURS_WEB" /grant "grAndroidTestersEmp:(OI)(CI)(M)"
icacls.exe "\\S1\_S1_INFO$\CONCEPTEURS_WEB" /grant "grAndroidTestersGest:(OI)(CI)(F)"

New-Item -Path "\\S1\_S1_INFO$\TESTEURS_PYTHON" `
         -ItemType directory

icacls.exe "\\S1\_S1_INFO$\CONCEPTEURS_WEB" /grant "grPythonTestersEmp:(OI)(CI)(M)"
icacls.exe "\\S1\_S1_INFO$\CONCEPTEURS_WEB" /grant "grPythonTestersGest:(OI)(CI)(F)"


