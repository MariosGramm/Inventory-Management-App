# 🏦 Simple Inventory Management System

Ένα **πλήρες σύστημα διαχείρισης αποθήκης** (Inventory Management System) υλοποιημένο με **Spring Boot**, **Thymeleaf**, **Hibernate** και **MySQL**.  
Η εφαρμογή υποστηρίζει πλήρη λειτουργικότητα **CRUD** (Create, Read, Update, Delete) για προϊόντα, αποθέματα (inventories), συναλλαγές (transactions) και χρήστες (users).

---

## 🚀 Τεχνολογίες

- **Java 17+**
- **Spring Boot 3**
- **Spring MVC**
- **Spring Data JPA / Hibernate**
- **Thymeleaf** (για το frontend)
- **MySQL** (για την αποθήκευση των δεδομένων)
- **Bootstrap 5** (UI/UX styling)

---

## 🧩 Περιγραφή Λειτουργιών

### 👥 Ρόλοι Χρηστών

#### 👨‍💼 **Admin**
- Έχει **πλήρη πρόσβαση** στο σύστημα.
- Μπορεί να:
  - Προσθέτει / επεξεργάζεται / διαγράφει προϊόντα.
  - Διαχειρίζεται τα αποθέματα (Inventories).
  - Παρακολουθεί και δημιουργεί **Transactions** (Increase / Decrease).
  - **Διαχειρίζεται χρήστες** (δημιουργία, τροποποίηση, διαγραφή).
- Διαθέτει dashboard με **στατιστικά** και **τελευταίο login**.

#### 👤 **User**
- Μπορεί να εκτελεί **μόνο λειτουργίες σχετικές με προϊόντα και αποθέματα**.
- Δεν έχει πρόσβαση στη **διαχείριση χρηστών**.
- Μπορεί να δει και να καταχωρήσει **transactions**.

---

## 💾 CRUD Λογική

Η λογική CRUD έχει υλοποιηθεί με βάση τη **δομή υπηρεσιών (Service Layer)**:
- **Products:** Δημιουργία, ενημέρωση, προβολή, διαγραφή.
- **Inventories:** Αυτόματη ενημέρωση αποθεμάτων βάσει transactions.
- **Transactions:**  
  - Κάθε Increase / Decrease ενημερώνει δυναμικά το απόθεμα.
  - Συνδέονται με προϊόν και αποθήκη.
- **Users:**  
  - Ο admin έχει πλήρη έλεγχο.
  - Το τελευταίο login ενημερώνεται δυναμικά.

---

## 🗃️ Database

Η βάση δεδομένων είναι **MySQL**.  
Τα entities συνδέονται μέσω **JPA/Hibernate** relationships:
- `Product` ↔ `Inventory`
- `Product` ↔ `Transaction`
- `Warehouse` ↔ `Inventory`
- `User` → (με ρόλους Admin / User)
