# Spaceship Fleet

Spaceship Fleet is a small full‑stack application for managing a fleet of spaceships. It exposes a REST API built with Spring Boot and a React + Vite frontend, all running in Docker containers via Docker Compose.

The application lets a user browse, search and manage spaceships stored in a relational database. Each spaceship stores both static information (name, class) and operational data (crew utilisation, fuel level, home base).

## Tech stack

- Java, Spring Boot (REST API)
- JPA/Hibernate, relational database (Dockerized)
- React + Vite (frontend)
- Docker, Docker Compose

## Requirements

- Docker and Docker Desktop installed
- Free ports on the host machine:
    - 8080 for the backend
    - 5173 for the frontend
    - (optional) database port, if exposed

## How to run

Clone the repository and start all services using Docker Compose:

- `git clone https://github.com/vdx99/spaceship-fleet.git`
- `cd spaceship-fleet`
- `docker compose build`
- `docker compose up`

After the containers start, the application is available at:

- Frontend: http://localhost:5173
- Backend (API): http://localhost:8080

## Application overview

Spaceship Fleet is a small full‑stack application for managing a fleet of spaceships. It exposes a REST API built with Spring Boot and a React + Vite frontend, all running in Docker containers via Docker Compose.

The application lets a user browse, search and manage spaceships stored in a relational database. Each spaceship stores both static information (name, class) and operational data (crew utilisation, fuel level, home base).

Example `Spaceship` model:

- `id`: unique identifier
- `name`: unique spaceship name
- `classType`: ship class (for example: FREIGHTER, FIGHTER)
- `crewCapacity`: maximum crew capacity
- `currentCrewCount`: current number of crew members on board
- `homeBase`: primary station / planet / spaceport
- `fuelLevelPercent`: current fuel level in percent

## Functional description

- View fleet overview  
  The main view shows a paginated list of all spaceships stored in the system. For each ship, the user can see its name, class type, crew capacity, current crew count, home base and current fuel level percentage.

- Class‑based organisation  
  Every spaceship belongs to a specific class (for example: FREIGHTER, FIGHTER). This allows you to quickly distinguish between combat ships, cargo vessels and other specialised types when analysing the fleet.

- Crew utilisation  
  The model tracks both `crewCapacity` and `currentCrewCount`. This makes it easy to spot under‑ or over‑utilised ships (for instance, ships that have a high capacity but very few crew members currently assigned).

- Operational context (home base)  
  Each spaceship has a `homeBase` field that indicates its primary station, planet or spaceport. This can be used to group ships by location and reason about deployment, logistics or maintenance.

- Fuel monitoring  
  The `fuelLevelPercent` field stores the current fuel level as a percentage. It gives a quick indication of which ships are ready for a mission and which would require refuelling before departure.

- Create new spaceship  
  The UI provides a form that allows the user to register a new spaceship. The form collects the basic details (name, class type, crew capacity, home base and initial fuel level), sends them as a POST request to the backend and persists the new record in the database.

- Backend REST API  
  The backend exposes a standard REST API around the `Spaceship` entity. The current implementation focuses on listing and creating ships, but the structure is ready to be extended with update, delete and more advanced search endpoints.

- Basic API metrics  
  The application includes a simple metrics endpoint that tracks how many requests have been made to the API. The frontend calls this endpoint and displays the current request count, demonstrating how to collect and expose operational metrics from a Spring Boot service.

## Example endpoints

Replace with your exact mappings if they differ.

- `GET /api/spaceships?page=0&size=20` – list spaceships with pagination
- `POST /api/spaceships` – create a new spaceship
- `GET /api/spaceships/{id}` – get details of a single spaceship
- `GET /api/metrics/requests-count` – return the total number of API requests

## Architecture notes

- Backend: Spring Boot REST API with JPA/Hibernate and a relational database running in a Docker container.
- Frontend: React + Vite single‑page application, communicating with the backend over HTTP/JSON.
- Infrastructure: Docker Compose orchestrates the backend, database and frontend so that the whole stack can be started with a single command.
