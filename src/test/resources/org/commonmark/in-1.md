
# Build Prozess

Voraussetzungen:
- Entwicklungsumgebung ist Windows
- Eclipse Version "Luna" mit SVN Plugin
- java 8 von Oracle muss installiert sein
- Zugang zum comron SVN repository muss gehen


## Kern auf Windows builden

Eclipse starten.

Projekt mcom-common aus SVN auschecken: /mcom/mcom-common/trunk.
In Eclipse mcom-common builden, build.xml Target "rebuild-all". 
Das erzeugt einen build/ Ordner. In build/dist/ liegen nun ein paar 
jar Files.

Projekt mcom-core aus SVN auschecken: /mcom/mcom-core/trunk.
In Eclipse mcom-core builden, build.xml Target "rebuild-all".
Das "rebuild-all" Target kopiert alle mcom-common jar Files
ins mcom-core Projekt. Build-Ergebnisse liegen nun in mcom-core/build/.

In C:\Temp folgende Ordner Struktur anlegen:

    C:\Temp
        └── mcom
            ├── core
            │   └── data
            ├── modules
            └── system

## Kern auf Windows starten

Der Kern kann nun gestartet werden, main-Klasse ist de.comron.mcom.core.CoreMain
Das erste Starten schlägt fehl weil dem Kern nicht mitgeteilt wurde wo 
seine Home- und Datenordner usw. sind. Das muß über CLI-Parameter erfolgen. 
Dazu in Eclipse Debug Configuration "CoreMain" bearbeiten:
CoreMain braucht folgende Arguments:

    -home    C:/eclipse-workspaces/luna/comron/mcom-core/home
    -data    C:/Temp/mcom/core/data
    -modules C:/Temp/mcom/modules
    -system  C:/Temp/mcom/system
    -allowEmptyLogin true

Die einzelnen Parameter sind in der CoreMain.java, Method "printUsage()" 
beschrieben.  Das "-home" muss wahrscheinlich an die eigene Eclipse-Installation 
angepasst werden.

Kern sollte jetzt startbar sein und in C:\Temp\mcom\core\data eine 
leere sqlite3 Datenbank "db" eine core.log und eine log4j.properties
Datei anlegen.
Die log4j.properties ist vom Kern selbst generiert und loggt per default
nur in core.log. Zum Entwickeln kann die Datei editiert werden und der 
console Appender enabled und logLevel auf DEBUG gesetzt werden.
Die Datenbank kann man mit [sqlite3](http://www.sqlite.org/download.html), 
Abschnitt "Precompiled Binaries for Windows" anschauen und ändern.
Oder mit jedem JDBC-fähigen DB-Tool.
 
So, der Kern läuft. Nun auf http://localhost gehen und "mcom Login" drücken.
In der Login-Box alles leer lassen und "Login" drücken. Man sollte dann als 
Superuser eingeloggt sein. Das geht weil "-allowEmptyLogin" auf 
true steht. Produktiv unbedingt "-allowEmptyLogin" weglassen!!

Das Starten des Kerns über die Kommandozeile ist auch möglich, mit folgendem Aufruf:

    #> java -cp "C:\eclipse-workspaces\luna\comron\mcom-core\home\lib\*;C:/eclipse-workspaces/luna/comron/mcom-core/bin" 
            de.comron.mcom.core.CoreMain
            -home C:\eclipse-workspaces\luna\comron\mcom-core\home
            -data C:\Temp\mcom\core\data
            -modules C:\Temp\mcom\modules
            -system C:\Temp\mcom\system
            -allowEmptyLogin true

oder direkt aus dem Projektverzeichnis:

    #> cd C:\eclipse-workspaces\luna\comron\mcom-core
    #> java -cp "home\lib\*;bin" 
            de.comron.mcom.core.CoreMain
            -home .
            -data C:\Temp\mcom\core\data
            -modules C:\Temp\mcom\modules
            -system C:\Temp\mcom\system
            -allowEmptyLogin true

Prinzipiell kann der Kern von jedem Verzeichnis aus aufgerufen werden, intern 
verwendet der Kern nur feste Pfade. Empfohlen ist aber der Aufruf direkt 
aus dem home/ Folder. (Vielleicht gibt's ja doch mal einen Segfault in der 
JVM, dann finden wir die Diagnosefiles leichter.)



## Modul auf Windows builden und ausführen

Am Beispiel von mod-stress. Dazu mcom/mcom-modframe und mcom/mcom-mod-stress 
als zwei eigene Eclipse-Projekte aus SVN auschecken.
Das Projekt modframe ist ein Framework für mcom Module. Dieses zuerst builden, 
build-xml Target "rebuild-all". Dann das Projekt mcom-mod-stress builden, 
auch mit "rebuild-all". Das ergibt im mcom-mod-stress/build/dist ein ZIP file 
das das Modul "Stress" enthält. Dieses kann im Kern über die Web-Oberfläche 
als Modul installiert, konfiguriert und gestartet werden.






## Projektstruktur

mcom besteht aus folgenden Eclipse Projekten:

- mcom-common 
  Bibliothek die vom Kern und von den Modulen benutzen wird.
  Enthält Klasen um Telegramme zu senden/empfangen und 
  manifest.json und settings.properties Dateien zu lesen/parsen/schreiben.

- mcom-core   
  Der Kern
  
- mcom-modframe
  Enthält einen Rahmen der für die Modulentwicklung benutzt werden kann
  
- mcom-mod-*
  Jedes mcom-mod-<modulename> Projekt enthält ein Modul, wobei <modulname> der eindeutige 
  Modulname ist, z.B. mcom-mod-mail, mcom-mod-aastra-omm, mcom-mod-foobar, usw.


Unter den Projekten gibt's Abhängigkeiten, und zwar die folgenden:

              
          +---------------+     +---------------+
          | mcom-mod-mail | ... | mcom-mod-zero |
          +---------------+     +---------------+
                  |                |
                  +-----+      +---+
                        |      |
                        |      |
                        V      V
    +-----------+   +---------------+
    | mcom-core |   | mcom-modframe |
    +-----------+   +---------------+
          |                |
          |                |
          |                |
          V                V
    +------------------------------+
    |          mcom-common         |
    +------------------------------+


Wenn also zB an mcom-modframe was geändert wird müssen diese Änderungen in alle 
Projekte übernommen werden die davon betroffen sind. Dafür gibt's in jedem Projekt
ein build.xml mit einem "update-*" Target. Dieses holt den aktuellen Stand des 
benötigten Projekts in den eigenen Verzeichnisbaum. (Wobei aber zu beachten ist dass
das benötigte Projekt selbst erstmal gebuildet werden muss.)
Alle Projekte gehen in das eigene build/ Verzeichnis, das erstellt wird sollte es 
noch nicht existieren.

Um alle mcom Projekte zu builden muß man also folgende Schritte ausführen:

1. mcom-common "rebuild-all"
2. mcom-core "rebuild-all"
3. mcom-modframe "rebuild-all"
4. mcom-mod-zero "rebuild-all"
(Schritt 4 für alle mcom-mod-* Projekte wiederholen)

Die Build-Ergebnisse finden sich dann im Ordner build/dist/ wieder.


