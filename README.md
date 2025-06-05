# Catalog Universitar
Proiect realizat în cadrul materiei Programare Avansată pe Obiecte, în limbajul Java.

## Etapa I
### 1. Definirea sistemului
- Tema proiectului: Catalog Universitar
- 8 tipuri de obiecte: Classroom, Course, CourseOffering, Department, Enrollment, Grade, Person, Professor, Student
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
- Moștenire: Person ca superclasă pentru Student și Professor
- Singleton: Serviciu unic CatalogService care expune operațiunile sistemului
- Clasa Main: interacțiune console (meniu), citire input și apeluri către CatalogService

## Etapa II
### 1. Bază de date relațională și JDBC
- DAO (Data Access Object): pattern ce izolează logica de acces la baza de date de restul aplicației
  - Fiecare DAO (CourseDao, StudentDao, ProfessorDao, etc.) oferă metode CRUD (Create, Read, Update, Delete)
  - DAO-ul folosește DBConnection pentru obținerea unei conexiuni java.sql.Connection
- DBInitializer resetează și creează schema MySQL cu tabelele și cheile externe, folosind AUTO_INCREMENT acolo unde e cazul
- DataLoader citește fișierele *.txt și apelează serviciul pentru a popula inițial baza de date
### 2. Realizarea unui serviciu de audit
- Clasa AuditService scrie într-un fișier CSV (Resources/audit.csv) numele acțiunii și timestamp-ul
- Fiecare metodă din CatalogService apelată scrie un rând în fișierul de audit


## Structura fișierelor de date (directory data/)
course.txt: `<codMaterie>,<denumire>,<nrCredite>,<numeDepartament>`

courseOffering.txt: `<codMaterie>,<semestru>,<an>`

department.txt: `<numeDepartament>,<facultate>`

enrollment.txt: `<numeStudent>,<codMaterie>,<semestru>,<an>,<dataInscrierii>`

grade.txt: `<numeStudent>,<codMaterie>,<semestru>,<an>,<dataInscrierii>,<valoareNota>`

professor.txt: `<numeProfesor>,<titluDidactic>,<numeDepartament>`

student.txt: `<numeStudent>,<dataNasterii>,<specializare>`

