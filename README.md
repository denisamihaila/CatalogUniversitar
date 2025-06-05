# Catalog Universitar
Proiect realizat în cadrul materiei Programare Avansată pe Obiecte, în limbajul Java.

## Etapa I
### 1. Definirea sistemului
- Tema proiectului: Catalog Universitar
- 8 tipuri de obiecte: `Classroom`, `Course`, `CourseOffering`, `Department`, `Enrollment`, `Grade`, `Person`, `Professor`, `Student`
- 10 acțiuni/interogări posibile (în CatalogService): înscriere student la curs, retragere student de la curs, listare cursuri dintr-un anumit an și semestru, atribuire profesor unui curs, adăugare materie, modificare informații materie, vizualizare studenți înscriși la curs, înregistrare notă, listare note și medie student, căutare cursuri după departament
### 2. Implementare
- Clase simple cu atribute private/protected și metode de acces, organizate în pachete: Entități, Repositories, Servicii, Utile, Resources
- Colecții diferite pentru gestionarea obiectelor în memorie:
  - `HashSet<Enrollment>` în Student pentru a evita duplicatele de înscriere
  - `ArrayList<Grade>` în Enrollment pentru notele unui student, păstrând ordinea adăugării
  - `ArrayList<CourseOffering>`, `ArrayList<Enrollment>`, `HashMap<String, Department>`, `HashMap<String, Course>`, `HashMap<String, Professor>`, `HashMap<String, Student>` în DataLoader
  - `ArrayList<Classroom>` în CourseOffering
  - `ArrayList<Course>` în CourseDao; `ArrayList<CourseOffering>` in CourseOfferingDao; `ArrayList<Department>` in DepartmentDao
  - `ArrayList<Enrollment>` in EnrollmentDao; `ArrayList<Professor>` in ProfessorDao; `ArrayList<Student>` in StudentDao
  - `TreeMap<Integer, Student>`(colecție sortată), `HashMap<Integer, Professor>`, `HashMap<Integer, Enrollment>` in CatalogService
- Moștenire: `Person` ca superclasă pentru `Student` și `Professor`
- Singleton: Serviciu unic `CatalogService` care expune operațiunile sistemului
- Clasa Main: interacțiune console (meniu), citire input și apeluri către CatalogService

## Etapa II
### 1. Bază de date relațională și JDBC
- DAO (Data Access Object): pattern ce izolează logica de acces la baza de date de restul aplicației
  - Fiecare DAO (CourseDao, StudentDao, ProfessorDao, etc.) oferă metode CRUD (Create, Read, Update, Delete)
  - DAO-ul folosește DBConnection pentru obținerea unei conexiuni java.sql.Connection
- DBInitializer resetează și creează schema MySQL cu tabelele și cheile externe, folosind AUTO_INCREMENT acolo unde e cazul
- DataLoader citește fișierele *.txt și apelează serviciul pentru a popula inițial baza de date
### 2. Realizarea unui serviciu de audit
- Clasa `AuditService` scrie într-un fișier CSV (`Resources`/`audit.csv`) numele acțiunii și timestamp-ul
- Fiecare metodă din `CatalogService` apelată scrie un rând în fișierul de audit


## Structura fișierelor de date (directory data/)
course.txt: `<codMaterie>,<denumire>,<nrCredite>,<numeDepartament>`

courseOffering.txt: `<codMaterie>,<semestru>,<an>`

department.txt: `<numeDepartament>,<facultate>`

enrollment.txt: `<numeStudent>,<codMaterie>,<semestru>,<an>,<dataInscrierii>`

grade.txt: `<numeStudent>,<codMaterie>,<semestru>,<an>,<dataInscrierii>,<valoareNota>`

professor.txt: `<numeProfesor>,<titluDidactic>,<numeDepartament>`

student.txt: `<numeStudent>,<dataNasterii>,<specializare>`

## Exemplu de rulare și testare a tuturor funcționalităților
### 1. Înscriere student la curs
Alegem de la tastatură opțiunea 1. Introducem un id dtudent inexistent, de exemplu 100. Primim următorul mesaj: 
„Eroare: Nu există student cu id=100”. Acum introducem id-uri valide. Exemplu: id student = 1, id oferta = 5 și 
alegem o dată de înscriere oarecare. Primim mesajul „Inscriere realizata: id=21”. Pentru a verifica, intrăm în 
MySQL Workbench și rulăm „select * from enrollment;”. Vedem că ultima înscriere este cea cu id=21.
### 2. Retragere student din curs
Alegem opțiunea 2. Încercăm un id ofertă inexistent (100) și primim mesajul: „Eroare: Nu există înscriere cu id=100”.
Apoi alegem un id existent (select * from enrollment;) și observăm că este eliminat din baza de date.
### 3. Listare cursuri pentru semestru și an
Alegem opțiunea 3. Selectăm un semstru dintre cele valide(1,2) și un an(1,2,3,4) și obținem toate cursurile disponibile.
De asemenea, dacă nu alegem un semestru sau un an valid, vom primi un mesaj cum că nu există oferte.
### 4. Atribuire profesor la curs
Alegem opțiunea 4. Pentru a vedea profesorii existenți rulăm în MySQL Workbench comanda „select * from professor;”, 
iar pentru cursuri, comanda „select * from course;”. Alegem câte un id existent pentru fiecare și rulăm comanda 
„select * from course_offering;” pentru a vedea că s-a efectuat atribuirea.
### 5. Adaugare materie in catalog
Alegem opțiunea 5. Completăm informațiile necesare și vom găsi adăugat în tabela course noul nostru curs. Atenție să 
scrieți denumirea întocmai a departamentului (Informatica/CTI/Matematica)
### 6. Modificare credite materie
Alegem opțiunea 6. Pentru a vedea codurile materiilor, ne uităm în tabela course. Apoi, alegem noul număr de credite
pe care vrem să îl aibă materia respectivă.
### 7. Vizualizare studenti inscrisi la curs
Alegem opțiunea 7. Apoi alegem un id din tabela course_offering și vom primi lista studenților înscriși la acel curs.
### 8. Înregistrare notă pentru student
Alegem opțiunea 8. Alegem un id inscriere existent (ex:1). Dacă alegem o notă care nu este în intervalul 0-10, 
primim un mesaj corespunzător. Pentru a verifica noua notă, o putem găsi în tabela grade, la final.
### 9. Listare note și medie student
Alegem opțiunea 9. Selectăm un id student inexistent(cei cu id-urile 1 și 2 au cele mai multe note în baza de date)
și ni se vor lista toate notele lor, iar apoi și media acestora, obținută prin tăiere, cu două zecimale.
### 10. Căutare cursuri după departament
Alegem opțiunea 10. Alegem numele unui departament (Informatica/CTI/Matematica) și obținem toate cursurile care 
fac parte din acel departament. Dacă introducem un departament inexistent, primim un mesaj corespunzător. 
### 0. Iesire
Pentru a închide aplicația, apăsăm tasta 0, urmată de Enter.
De asemenea, dacă nu alegem o opțiune existentă, primim următorul mesaj: 
„Opțiune invalidă. Alege un număr între 0 și 10.”. 