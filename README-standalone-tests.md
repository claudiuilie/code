# Rulare teste Cucumber + JUnit 5 dintr-un jar standalone

Acest ghid descrie pas cu pas modificările necesare în proiect pentru a putea construi
**un singur jar executabil** care conține testele Cucumber + toate dependințele + JUnit
Platform Launcher încorporat, rulabil cu `java -jar ...`, independent de `mvn test` /
Surefire, cu suport pentru paralelism și filtrare pe tag-uri suprascrise la rulare.

Pornim de la starea actuală a proiectului: Cucumber 7.15.0 + JUnit 5.10.2, testele rulează
azi prin `maven-surefire-plugin` (dezactivat implicit prin `<skip>true</skip>`, activat doar
în profilul Maven `test`), cu clasa `@Suite` `playwright.RunCucumberTest`.

---

## 0. Fix obligatoriu înainte de orice altceva

Configurația actuală din `pom.xml` conține:

```xml
<configurationParameters>
    cucumber.junit-platform.naming-strategy=surefire
</configurationParameters>
```

Valoarea `surefire` pentru `cucumber.junit-platform.naming-strategy` a fost introdusă abia
în **cucumber-jvm 7.23.0** ([PR #3003](https://github.com/cucumber/cucumber-jvm/pull/3003)).
Proiectul folosește `cucumber.bom.version = 7.15.0`, unde această valoare nu există, și orice
execuție (Surefire sau standalone) aruncă:

```
java.lang.IllegalArgumentException: No enum constant io.cucumber.junit.platform.engine.DefaultNamingStrategy.SUREFIRE
```

**Fix (recomandat):** elimină linia din `pom.xml`, din blocul `<configurationParameters>` al
plugin-ului `maven-surefire-plugin`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>${maven.surfire.plugin.version}</version>
    <configuration>
        <skip>true</skip>
        <!-- blocul <properties>...</properties> cu naming-strategy=surefire se elimină -->
    </configuration>
</plugin>
```

Implicit se va folosi strategia `short`, ceea ce e perfect valabil. (Alternativ, dacă vrei
neapărat formatul de nume "surefire-friendly", urcă `cucumber.bom.version` la `7.23.0` sau
mai nou — dar asta e o schimbare de versiune separată, netratată în acest ghid.)

---

## 1. Modificări în `pom.xml`

### 1.1. Dependință nouă: `junit-platform-launcher`

E folosită direct de clasa `Main` de mai jos ca să pornească testele programatic. E deja
disponibilă tranzitiv prin `junit-platform-suite`, dar o declarăm explicit pentru claritate
și pentru compilare fără avertismente:

```xml
<dependency>
    <groupId>org.junit.platform</groupId>
    <artifactId>junit-platform-launcher</artifactId>
    <scope>test</scope>
</dependency>
```

Adaug-o în `<dependencies>`, lângă celelalte dependințe `org.junit.platform.*`. Versiunea
vine automat din `junit-bom` (deja definit în `dependencyManagement`).

### 1.2. Profil Maven nou: `standalone-tests`

Adaugă acest profil în `<profiles>`, ca profil separat (nu afectează build-ul normal —
se activează explicit cu `-Pstandalone-tests`):

```xml
<profile>
    <id>standalone-tests</id>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>3.7.1</version>
                <configuration>
                    <descriptors>
                        <descriptor>src/assembly/standalone-tests.xml</descriptor>
                    </descriptors>
                    <archive>
                        <manifest>
                            <mainClass>playwright.TestRunnerMain</mainClass>
                        </manifest>
                    </archive>
                    <finalName>anaf-tests-standalone</finalName>
                    <appendAssemblyId>false</appendAssemblyId>
                </configuration>
                <executions>
                    <execution>
                        <id>build-standalone-tests</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</profile>
```

---

## 2. Fișiere noi de creat

### 2.1. `src/test/java/playwright/TestRunnerMain.java`

Clasă `Main` care pornește programatic JUnit Platform Launcher, selectează suite-ul
`RunCucumberTest` și transmite orice `-Dcucumber.*` / `-Djunit.*` primit la `java -jar` ca
configuration parameter — inclusiv paralelism și `cucumber.filter.tags`.

```java
package playwright;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class TestRunnerMain {

    public static void main(String[] args) {
        Map<String, String> configParams = new HashMap<>();

        // valori implicite, echivalente cu profilul "test" din Surefire
        configParams.put("cucumber.execution.parallel.enabled", "true");
        configParams.put("cucumber.execution.parallel.config.strategy", "fixed");
        configParams.put("cucumber.execution.parallel.config.fixed.parallelism", "4");
        configParams.put("cucumber.execution.parallel.config.fixed.max-pool-size", "4");

        // orice -Dcucumber.xxx / -Djunit.xxx dat la "java -jar ..." suprascrie
        // valorile implicite de mai sus (inclusiv cucumber.filter.tags,
        // cucumber.execution.parallel.config.fixed.parallelism, etc.)
        for (String name : System.getProperties().stringPropertyNames()) {
            if (name.startsWith("cucumber.") || name.startsWith("junit.")) {
                configParams.put(name, System.getProperty(name));
            }
        }

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectClass(RunCucumberTest.class))
                .configurationParameters(configParams)
                .build();

        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);

        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        summary.printTo(new PrintWriter(System.out));
        summary.printFailuresTo(new PrintWriter(System.out), 100);

        System.exit(summary.getTotalFailureCount() > 0 ? 1 : 0);
    }
}
```

> Nu mai setăm `cucumber.junit-platform.naming-strategy` aici (vezi pasul 0).

### 2.2. `src/assembly/standalone-tests.xml`

Descriptorul de assembly: bagă `target/test-classes` (testele compilate + `features/*.feature`
+ resursele de configurare) și toate dependințele (despachetate) într-un singur jar.
`containerDescriptorHandlers` cu `metaInf-services` e **obligatoriu** — combină fișierele
`META-INF/services/org.junit.platform.engine.TestEngine` din `cucumber-junit-platform-engine`
și `junit-platform-suite-engine` în loc să lase unul să-l suprascrie pe celălalt (altfel unul
din cele două motoare de test nu mai e descoperit).

```xml
<assembly xmlns="http://maven.apache.org/ASSEMBLY/1.1.3"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/ASSEMBLY/1.1.3 http://maven.apache.org/xsd/assembly-1.1.3.xsd">
    <id>standalone-tests</id>
    <formats>
        <format>jar</format>
    </formats>
    <includeBaseDirectory>false</includeBaseDirectory>

    <containerDescriptorHandlers>
        <containerDescriptorHandler>
            <handlerName>metaInf-services</handlerName>
        </containerDescriptorHandler>
    </containerDescriptorHandlers>

    <fileSets>
        <fileSet>
            <directory>${project.build.testOutputDirectory}</directory>
            <outputDirectory>/</outputDirectory>
        </fileSet>
    </fileSets>

    <dependencySets>
        <dependencySet>
            <outputDirectory>/</outputDirectory>
            <useProjectArtifact>false</useProjectArtifact>
            <scope>test</scope>
            <unpack>true</unpack>
        </dependencySet>
    </dependencySets>
</assembly>
```

---

## 3. Build

Din rădăcina proiectului:

```powershell
mvn clean package -Pstandalone-tests
```

Rezultat: `target\anaf-tests-standalone.jar` — un singur fișier, cu tot ce e nevoie (teste +
dependințe + launcher), portabil pe orice mașină cu JDK 21.

Câteva `WARN: duplicate entry` la despachetare (licențe, `MANIFEST.MF` din dependințe) sunt
normale la un uber-jar și nu blochează build-ul.

---

## 4. Rulare

### 4.1. Rulare simplă (paralelism implicit 4, fără filtru de tag-uri)

```powershell
java -jar target\anaf-tests-standalone.jar
```

### 4.2. Suprascriere paralelism la rulare

```powershell
java -Dcucumber.execution.parallel.config.fixed.parallelism=8 `
     -Dcucumber.execution.parallel.config.fixed.max-pool-size=8 `
     -jar target\anaf-tests-standalone.jar
```

Dezactivare completă a paralelismului pentru o rulare de debug:

```powershell
java -Dcucumber.execution.parallel.enabled=false -jar target\anaf-tests-standalone.jar
```

### 4.3. Filtrare după tag-uri (`cucumber.filter.tags`)

Nu necesită nicio modificare de cod — `TestRunnerMain` propagă automat orice proprietate
`-Dcucumber.*`:

```powershell
java -Dcucumber.filter.tags="@smoke and not @wip" -jar target\anaf-tests-standalone.jar
```

### 4.4. Combinat: paralelism + tag-uri

```powershell
java -Dcucumber.execution.parallel.config.fixed.parallelism=2 `
     -Dcucumber.execution.parallel.config.fixed.max-pool-size=2 `
     -Dcucumber.filter.tags="@regression" `
     -jar target\anaf-tests-standalone.jar
```

> Rulează comanda din rădăcina proiectului, ca `report/cucumber/report.json` și evidence-urile
> Playwright să se scrie în aceleași locuri ca acum (căile sunt relative la directorul curent).

---

## 5. Cod de ieșire (pentru CI)

`TestRunnerMain` face `System.exit(1)` dacă există cel puțin un scenariu picat, și `System.exit(0)`
altfel — util pentru un pipeline CI care verifică exit code-ul comenzii `java -jar ...`.

---

## 6. Troubleshooting

| Simptom | Cauză | Fix |
|---|---|---|
| `No enum constant ...DefaultNamingStrategy.SUREFIRE` | `cucumber.junit-platform.naming-strategy=surefire` cu cucumber < 7.23.0 | Vezi pasul 0 |
| Cucumber nu descoperă niciun scenariu, sau `@Suite` nu mai e recunoscut | Fișiere `META-INF/services/...TestEngine` suprascrise unul pe altul în jar | Verifică `containerDescriptorHandlers` din `standalone-tests.xml` (pasul 2.2) |
| `WARN: duplicate entry ...` la `mvn package -Pstandalone-tests` | Normal la uber-jar (licențe, manifeste din dependințe) | Ignoră, sau exclude explicit fișierele respective în `dependencySet` dacă devine prea zgomotos |
