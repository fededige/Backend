#  READCYCLE
## Description:
This project contains a web application based on a microservice architecture developed with Java Spring Boot.
## What can the user do?
The normal user can:
- **Insert a book** in the application catalog. This operation make the book available to every other user to borrow.
- **Borrow a book** which is available (not lent at the moment of the operation). When a book is borrowed from some user than it's not shown in the catalog anymore.
- **Return a book** which was previously borrowed.
- **Leave a review** to a book previously borrowed.
## Microservices summary
- **Catalog**: 
  - Handles the list of book available.
  - Handles the operations of insertion, removal and update of inserted books.
  - When a book is added pays the owner with one (1) coins.
- **Reservation**:
  - Handles the operations of insertion, removal and update of new reservation when a book is borrowed by some user.
  - Accepts or rejects reservations based on coins available to the user and other parameters.
- **Review**: 
  - Handles the operations of insertion, removal and update of new review related to some book.
- **User**:
  - Handles the operations of insertion, removal and update of new users.
## Kubernetes
To start the application in kubernetes just follow the instruction written in the [kubernetes](kubernetes/README.md) folder 
## Docker
There are two ways to start the application: building the images locally or use the images already built on [Docker Hub](https://hub.docker.com/repository/docker/fededige/readcycle/general).
### Starting the application building the images locally
_If you want to build the images locally than you need to also build the image for the frontend following the instructions in the [Frontend repo](https://github.com/fededige/ProgettoTAASS).This step is **NOT** needed if building the images from [Docker Hub](https://hub.docker.com/repository/docker/fededige/readcycle/general)._

Run the following command in the root folder:
```bash
docker compose -f docker-compose.yml build
```
Then to create and start the containers run:
```bash
docker compose up
```
### Starting the application with the images from Docker Hub
Run the following command in the root folder:
```bash
docker compose -f docker-compose-remote.yml up
```
