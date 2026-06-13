# DeveloperHub Frontend

Frontend for a training full-stack application with a Spring Boot backend.

The project is used to practice REST API communication, frontend-backend integration, VPS deployment, Nginx reverse proxy configuration, and automated deployment with GitHub Actions.

## Live Demo

The application is deployed on a VPS server:

text http://164.90.234.19

The API is available through an Nginx reverse proxy:

text http://164.90.234.19/api

Note: this is a training server. The backend currently stores data only in memory, so all data can be lost after a backend restart, server restart, crash, or new deployment.

## Technologies

Frontend:

text React Vite JavaScript CSS

Backend:

text Java Spring Boot REST API

Deployment:

text DigitalOcean VPS Ubuntu Nginx systemd GitHub Actions

## Local Development

Go to the frontend directory:

bash npm install npm run dev

The frontend runs locally on:

text http://localhost:5173

## Backend and API

In production, the frontend calls the backend using a relative API URL:

text /api

For example:

text http://164.90.234.19/api/developers

Nginx receives these requests and forwards them internally to the Spring Boot backend running on the server.

For local development, the backend should run on:

text http://localhost:8080

or a local Vite proxy should be configured.

## Backend Endpoints Used by the Frontend

http GET    /developers POST   /developers GET    /developers/{id} DELETE /developers/{id}  GET    /projects POST   /projects  GET    /applications POST   /applications PATCH  /applications/{id}/status

In production, these endpoints are called through the /api prefix, for example:

http GET    /api/developers POST   /api/developers GET    /api/projects POST   /api/projects

## Automated Deployment

The project has automated deployment configured with GitHub Actions.

Every push to the main branch triggers a workflow that:

text 1. connects to the VPS server over SSH 2. pulls the latest code from GitHub 3. builds the Spring Boot backend 4. restarts the backend systemd service 5. builds the React/Vite frontend 6. copies the frontend build output to the Nginx web directory 7. reloads Nginx

This means that changes pushed to main are automatically deployed to the live VPS server.

## Current Data Storage

The backend currently uses in-memory storage.

This means:

text data is not persistent data is shared only while the backend process is running data is cleared after backend restart or deployment

A persistent database such as PostgreSQL is planned as a future improvement.

## Error Playground

The frontend includes buttons for testing API error responses:

http 400 Bad Request 404 Not Found

## Planned Improvements

text PostgreSQL database Docker support Docker Compose deployment HTTPS with a custom domain authentication better validation and error handling.
