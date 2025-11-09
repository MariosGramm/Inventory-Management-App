# 🏦 Simple Inventory Management System

A **full stack inventory management app** built with **Spring Boot**, **Thymeleaf**, **Hibernate**, and **MySQL**.  
The application supports full **CRUD** functionality (Create, Read, Update, Delete) for **Products**, **Inventories**, **Transactions**, and **Users**.

---

## 🚀 Technologies

- **Java 17+**
- **Spring Boot 3**
- **Spring MVC**
- **Spring Data JPA / Hibernate**
- **Thymeleaf** (for the frontend)
- **MySQL** (for data storage)
- **Bootstrap 5** (for UI/UX styling)

---

## 🧩 Features Overview

### 👥 User Roles

#### 👨‍💼 **Admin**
- Has **full system access**.
- Can:
  - Create, edit, and delete **products**.
  - Manage **inventories**.
  - View and create **transactions** (Increase / Decrease).
  - **Manage users** (create, update, delete).
- Has access to an **admin dashboard** with statistics and **last login tracking**.

#### 👤 **User**
- Can perform actions **related to products and inventories only**.
- Cannot manage other users.
- Can view and register **transactions**.

---

## 💾 CRUD Logic

The CRUD operations are implemented following a **Service Layer architecture**:
- **Products:** Create, update, view, and delete.
- **Inventories:** Automatically updated based on transactions.
- **Transactions:**  
  - Each Increase / Decrease dynamically updates the inventory quantity.
  - Linked to both a product and a warehouse.
- **Users:**  
  - Admin has full management privileges.
  - The **last login time** is automatically updated.

---

## 🗃️ Database

The system uses a **MySQL** relational database.  
Entities are linked through **JPA/Hibernate relationships**:
- `Product` ↔ `Inventory`
- `Product` ↔ `Transaction`
- `Warehouse` ↔ `Inventory`
- `User` → (with roles: Admin / User)
