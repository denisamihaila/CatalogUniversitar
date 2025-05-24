# Catalog Universitar
Proiect realizat în cadrul materiei Programare Avansată pe Obiecte, în limbajul Java.

## Etapa I
### 1. Definirea sistemului
- Tema proiectului: Catalog Universitar
- 8 tipuri de obiecte: Classroom, Course, CourseOffering, Department, Enrollment, Grade, Person, Professor, Student
- 10 acțiuni/interogări posibile (în CatalogService): înscriere student la curs, retragere student de la curs, listare cursuri dintr-un anumit an și semestru, atribuire profesor unui curs, adăugare materie, modificare informații materie, vizualizare studenți înscriși la curs, înregistrare notă, listare note și medie student, căutare cursuri după departament
### 2. Implementare
- Clase simple cu atribute private/protected și metode de acces, organizate în pachete: Entități, Repositories, Servicii, Utile, Resources
- Colecții diferite: Map, List, Set (ex: TreeMap pentru ordine, HashSet pentru înscrieri, List pentru liste ordonate)
- Moștenire: Person ca superclasă pentru Student și Professor
- Singleton: Serviciu unic CatalogService care expune operațiunile sistemului
- Clasa Main: interacțiune console (meniu), citire input și apeluri către CatalogService

## Etapa II
### 1. Bază de date relațională și JDBC
- Am extins repository-urile cu DAO-uri pe JDBC (CourseDao, StudentDao, etc.)
- Fiecare DAO implementează operații CRUD (create, read, update, delete) pentru entitățile sale
- Clasa DBInitializer resetează și creează schema MySQL cu AUTO_INCREMENT și constrângeri FK
- În CatalogService, la fiecare operație de creare/modificare/ștergere am adăugat și apeluri către DAO
### 2. Realizarea unui serviciu de audit
- Clasa AuditService scrie într-un fișier CSV (Resources/audit.csv) numele acțiunii și timestamp-ul
- Fiecare metodă din CatalogService apelată scrie un rând în fișierul de audit
