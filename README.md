# 🎬 CineHub — Movie Ticket Booking Backend

### A Spring Boot REST API for a real-world movie ticket booking platform.

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Backend-6DB33F?logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-4169E1?logo=postgresql)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-Caching-DC382D?logo=redis)](https://redis.io/)

---

## 🚀 Overview

CineHub is a movie ticket booking platform inspired by BookMyShow, built using **Java, Spring Boot, PostgreSQL, and Redis**.

It provides backend functionality for authentication, movie management, theatre operations, show scheduling, seat reservations, bookings, and payment integration.

## 🌐 Live Demo

- **Frontend:** [Explore CineHub](https://cinehub-app.netlify.app)


## 🏗️ Architecture

![CineHub Monolithic Architecture](docs/cinehub-monolithic-architecture.png)

CineHub follows a **layered monolithic architecture**, with controllers, services, repositories, and domain modules running within one Spring Boot application.

## ✨ Key Features

- 🔐 **Authentication & Authorization** — JWT, OTP verification, and role-based access.
- 🎬 **Movie Management** — Browse movies and manage the catalogue.
- 🏢 **Theatre Management** — Theatre-owner requests, theatres, screens, and seats.
- 📅 **Show Management** — Show scheduling and seat availability.
- 🎟️ **Booking & Reservation** — Redis-based temporary seat locking.
- 💳 **Payment Integration** — Razorpay payment verification.
- 🖼️ **Image Storage** — Supabase Storage integration.

## 🛠️ Technology Stack

| Category | Technologies |
|---|---|
| Backend | Java 21, Spring Boot |
| Security | Spring Security, JWT |
| Database | PostgreSQL |
| Data Access | Spring Data JPA, Hibernate |
| Caching | Redis |
| Storage | Supabase Storage |
| Payments | Razorpay |
| Build | Maven |
| Frontend | React, Vite |
| Deployment | Render, Netlify |

## 👨‍💻 Author

**Prajan Gowda**

Java Backend Development • Spring Boot • REST APIs • Database Design
