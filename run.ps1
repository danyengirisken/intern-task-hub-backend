# Intern Task Hub - Backend calistirma yardimcisi
# Bu makinede sistem JAVA_HOME degeri JDK 8'i gosterdiginden, Maven'in
# JDK 17 kullanmasi icin JAVA_HOME burada gecici olarak ayarlanir.
#
# Veritabani ayarlari artik application-dev.properties dosyasindadir
# (jdbc:postgresql://localhost:5432/interndb).

$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"

Write-Host "JAVA_HOME = $env:JAVA_HOME"
Write-Host "Profil    = dev  (DB: interndb)"
Write-Host "Backend baslatiliyor: http://localhost:8082 ..."

& "$PSScriptRoot\mvnw.cmd" spring-boot:run
