1. Przykład wygenerowany na bazie archetypu (moduł main):
   com.github.charlie-cityu.archetypes:docs-city-archetype-quickstart
   (A variation on the maven-archetype-quickstart with source set to 1.8,
    build to executable jar with dependencies and junit 4.12. File names have been changed
    to Main.java and MainTest.java. Directory structure remains consistent with the Maven
    standard.)

2. Przyład buduje plik jar z zależnościami - posiada dodaną zależność (z lab. 3 i lab. 4):

3. Został dodany moduł utils oraz macierzysty plik pom.xml okreslający, że projekt składa się z modułów:

  <modules>
      <module>utils</module>
      <module>main</module>
  </modules>


  <dependencyManagement>
      <dependencies>
          <dependency>
              <groupId>pl.edu.agh.kis.pz1</groupId>
              <artifactId>main</artifactId>
              <version>${project.version}</version>
          </dependency>
          <dependency>
              <groupId>pl.edu.agh.kis.pz1</groupId>
              <artifactId>utils</artifactId>
              <version>${project.version}</version>
          </dependency>
      </dependencies>
  </dependencyManagement>
  
4. W pom.xml modułów jest odwołanie do pliku macierzystego:
  <parent>	
    <groupId>pl.edu.agh.kis.pz1</groupId>
    <artifactId>multi-module</artifactId>
    <version>1.0</version>
  </parent>  
  
5. Paczka wykonywalna znajduje sie w module main, który ma dodaną zależność od modułu utils gdyż wykorzystje klasę tam zdefiniowaną - w sekcji <dependencies> dodano:

    <dependency>
        <groupId>pl.edu.agh.kis.pz1</groupId>
        <artifactId>utils</artifactId>
        <version>${project.version}</version>
    </dependency>