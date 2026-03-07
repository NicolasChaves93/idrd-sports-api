
# IDRD Sports API – Project Context

## Project Information

**Project Name:** idrd-sports-api  
**Type:** Backend REST API  
**Organization:** Instituto Distrital de Recreación y Deporte (IDRD)  
**Location:** Bogotá, Colombia  
**Author:** Bryan Nicolas Chaves Arce  
**Architecture:** Hexagonal Architecture (Ports & Adapters)  
**Language:** Java 17  
**Framework:** Spring Boot 3.4  

---

# Project Purpose

The **idrd-sports-api** project is the backend service for a sports management platform designed for the Instituto Distrital de Recreación y Deporte (IDRD).

The objective of this system is to centralize sports data management including:

- Athletes
- Trainers
- Training sessions
- Sports facilities
- Performance metrics

The platform will provide REST APIs that enable integration with web applications, analytics systems, and future AI-based decision tools.

---

# Technology Stack

## Backend

- Java 17
- Spring Boot 3.4
- Maven

## Architecture

- Hexagonal Architecture
- Clean Code
- SOLID principles

## Security

- Spring Security
- JWT Authentication

## API Documentation

- OpenAPI / Swagger

## Testing

- JUnit 5
- Mockito

## Code Quality

- SonarQube

## Containerization

- Docker

---

# Maven Configuration

groupId:

gov.idrd.sports

artifactId:

idrd-sports-api

version:

1.0.0

packaging:

jar

---

# Base Package

All Java code must use the base package:

gov.idrd.sports

Example structure:

gov.idrd.sports
├── domain
├── application
├── infrastructure
│   ├── adapters
│   ├── config
│   └── security
└── shared

---

# Architecture Layers

## Domain

Contains business rules and core entities.

Examples:

- Athlete
- Trainer
- TrainingSession
- Facility
- User

Responsibilities:

- Entities
- Business logic
- Domain services
- Ports (interfaces)

---

## Application

Coordinates business operations through use cases.

Responsibilities:

- Use cases
- Application services
- DTOs
- Transaction orchestration

---

## Infrastructure

Implements external integrations.

Responsibilities:

- REST Controllers
- Database adapters
- Security configuration
- Persistence repositories

---

# Database Configuration (MVP)

For the **MVP stage**, the system will use a **local SQLite database** instead of a remote PostgreSQL server.

This allows development without requiring a running database server.

Future versions will migrate to PostgreSQL.

## Database Engine

SQLite

## Database File

idrd_sports_mvp.db

Location example:

/data/idrd_sports_mvp.db

---

# Spring Boot SQLite Configuration

Example configuration for `application.properties`:

spring.application.name=idrd-sports-api

server.port=8080

spring.datasource.url=jdbc:sqlite:idrd_sports_mvp.db
spring.datasource.driver-class-name=org.sqlite.JDBC

spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

---

# Domain Entities

## User

Attributes:

- id
- name
- email
- role
- password

---

## Athlete

Attributes:

- id
- name
- age
- category
- trainer_id

---

## Trainer

Attributes:

- id
- name
- specialty

---

## TrainingSession

Attributes:

- id
- athlete_id
- date
- load
- repetitions
- velocity

---

## SportFacility

Attributes:

- id
- name
- location
- type

---

# API Endpoints

## Authentication

POST /api/auth/login  
POST /api/auth/register

---

## Athletes

GET /api/athletes  
GET /api/athletes/{id}  
POST /api/athletes  
PUT /api/athletes/{id}  
DELETE /api/athletes/{id}

---

## Trainings

GET /api/trainings  
POST /api/trainings  
GET /api/trainings/{id}

---

## Facilities

GET /api/facilities  
POST /api/facilities  
PUT /api/facilities/{id}

---

# User Stories

| ID | Role | Story | Priority |
|----|-----|------|---------|
| HU-01 | Admin | Register athletes | High |
| HU-02 | Admin | Edit athlete information | High |
| HU-03 | Trainer | Register training sessions | High |
| HU-04 | Trainer | View athlete performance history | High |
| HU-05 | Admin | Manage sports facilities | Medium |
| HU-06 | Admin | Register trainers | Medium |
| HU-07 | Trainer | Assign training plans | Medium |
| HU-08 | Trainer | View performance statistics | Medium |
| HU-09 | Admin | Generate reports | Medium |
| HU-10 | System | Validate user input | High |
| HU-11 | Trainer | Compare athlete performance | Low |
| HU-12 | Admin | Export reports | Low |
| HU-13 | Trainer | View training calendar | Medium |
| HU-14 | Admin | Manage users | High |
| HU-15 | System | Log system activity | Medium |

---

# Sprint 1 Backlog

| Story | Description |
|------|-------------|
| HU-01 | Athlete registration |
| HU-03 | Training session registration |
| HU-04 | Athlete performance consultation |
| HU-10 | Data validation |

---

# Code Standards

The project must follow:

- Clean Code principles
- SOLID principles
- Low coupling
- High cohesion

Rules:

- Controllers must not contain business logic
- Business rules belong to the domain layer
- Repositories are defined as ports
- Adapters implement ports
- Use DTOs between layers

---

# SonarQube Quality Gate

Target metrics:

bugs: 0

vulnerabilities: 0

coverage >= 70%

duplication < 3%

---

# Future Evolution

Planned improvements:

- Migration from SQLite to PostgreSQL
- Data analytics integration
- ETL pipelines
- Machine learning for athlete performance
- Microservices architecture

---

# Agent Guidelines

When generating code for this project:

- Follow Hexagonal Architecture
- Keep controllers thin
- Use application services for orchestration
- Implement repositories as ports
- Use adapters for database access
- Maintain separation between domain and infrastructure

Language: Java  
Framework: Spring Boot
