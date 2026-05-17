# DeveloperHub Frontend

Frontend na tréning Spring Boot backendu.

## Spustenie

```bash
npm install
npm run dev
```

Frontend očakáva backend na:

```text
http://localhost:8080
```

## Backend endpointy, ktoré frontend volá

```http
GET    /developers
POST   /developers
GET    /developers/{id}
DELETE /developers/{id}

GET    /projects
POST   /projects

GET    /applications
POST   /applications
PATCH  /applications/{id}/status
```

## Error playground

Frontend má tlačidlá na testovanie:

```http
400 Bad Request
404 Not Found
```
