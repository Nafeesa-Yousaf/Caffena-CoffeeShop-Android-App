# ☕ Caffeena - Online Coffee Shop App

Caffeena is a modern Android application for ordering coffee online. It provides a smooth user experience, secure Firebase authentication, cart management, category-based product listings, and integrates with Firebase Realtime Database.

---

## 📱 Features

- 🔐 **User Authentication**
  - Email/Password Sign In & Sign Up
  - Google Sign-In with Credential Manager
  - Password Reset
  - User session management

- 🛍️ **Product Management**
  - Browse coffee items by category
  - Popular items section
  - View product details
  - Add items to cart

- 🛒 **Cart Functionality**
  - Real-time cart synced with Firebase
  - Increase/decrease quantity
  - Remove individual items
  - Calculate total price

- 🎯 **Architecture**
  - MVVM-inspired architecture
  - Firebase Realtime Database integration
  - Repositories for clean data management
  - SharedPreferences for local session management

---

## 🛠️ Tech Stack

| Component        | Technology            |
|------------------|------------------------|
| Language         | Java                   |
| Architecture     | Repository Pattern     |
| UI Framework     | Android View Binding   |
| Backend          | Firebase Authentication, Realtime Database |
| Image Loading    | Glide                  |
| Google Auth      | Credential Manager API |

---

## 📂 Project Structure

├── Activity/

│ └── SplashActivity, LoginActivity, MainActivity, etc.

├── Adapter/

│ └── PopularAdapter, CategoryAdapter, CartAdapter

├── Domain/

│ └── Data models like ItemsModel, CategoryModel

├── Helper/

│ └── ManagmentCart, Listener Interfaces

├── Repository/

│ └── AuthRepository, UserRepository, CartRepository

├── res/
│ └── layout/, drawable/, values/

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Electric Eel or later
- Java 11+
- Firebase project setup

---
