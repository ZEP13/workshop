# Workshop Zepcla API

## Authentication
All routes prefixed with `/logged/` require a valid JWT token in the `Authorization` header:
```
Authorization: Bearer <token>
```
Public routes (`/register`, `/login`, `/admin/create`) do not require a token.

---

## Auth — public routes

| Method | Route | Role | Description |
|--------|-------|------|-------------|
| POST | `/register` | PUBLIC | Register a new client account |
| POST | `/login` | PUBLIC | Login and get JWT token |
| POST | `/admin/create` | ADMIN | Create a new admin account linked to an enterprise |

### POST `/register`
**Body:**
```json
{
  "email": "client@mail.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "0479000000",
  "tokenRdv": "optional-appointment-token"
}
```

### POST `/admin/create`
**Body:**
```json
{
  "email": "admin@mail.com",
  "password": "password123",
  "firstName": "Jane",
  "lastName": "Doe",
  "phone": "0479000001",
  "enterpriseId": 1
}
```
**Rules:**
- `enterpriseId` is required for admin creation
- Admin will be automatically linked to the enterprise

---

## Users — `/logged/users`

| Method | Route | Role | Description |
|--------|-------|------|-------------|
| GET | `/logged/users/all` | ADMIN | Get all users |
| GET | `/logged/users/{id}` | ADMIN | Get user by ID |
| GET | `/logged/users/search` | ADMIN | Search users with filters |
| GET | `/logged/users/by-email` | ADMIN | Get user by email |
| GET | `/logged/users/by-name` | ADMIN | Get user by first and last name |
| PUT | `/logged/users/update/{id}` | ADMIN | Update a user |
| PUT | `/logged/users/update/me` | CLIENT, ADMIN | Update current user |
| DELETE | `/logged/users/delete/{id}` | ADMIN | Delete a user |
| DELETE | `/logged/users/delete/me` | CLIENT, ADMIN | Delete current user |

### GET `/logged/users/search`
**Query params:**
| Param | Type | Required | Description |
|-------|------|----------|-------------|
| page | Integer | No (default: 0) | Page number |
| size | Integer | No (default: 10) | Page size |
| id | Long | No | Filter by ID |
| firstName | String | No | Filter by first name |
| lastName | String | No | Filter by last name |
| email | String | No | Filter by email |
| role | String | No | Filter by role (CLIENT, ADMIN) |
| phoneNumber | String | No | Filter by phone number |

---

## Appointments — `/logged/appointments`

| Method | Route | Role | Description |
|--------|-------|------|-------------|
| POST | `/logged/appointments/create` | CLIENT, ADMIN | Create an appointment for the current user |
| POST | `/logged/appointments/admin-create` | ADMIN | Create an appointment for a specific client |
| PUT | `/logged/appointments/cancel/{id}` | CLIENT, ADMIN | Cancel an appointment (min 12h before) |
| GET | `/logged/appointments/all` | ADMIN | Get all appointments |
| GET | `/logged/appointments/{id}` | CLIENT, ADMIN | Get appointment by ID (client can only see own) |
| GET | `/logged/appointments/by-date?date=` | ADMIN | Get all appointments by date |
| GET | `/logged/appointments/by-client/{id_client}` | ADMIN | Get appointments by client ID |
| GET | `/logged/appointments/by-creator/{id_creator}` | ADMIN | Get appointments by creator ID |
| GET | `/logged/appointments/my-appointments` | CLIENT, ADMIN | Get current user's appointments |
| GET | `/logged/appointments/my-appointments/by-date?date=` | CLIENT, ADMIN | Get current user's appointments by date |
| GET | `/logged/appointments/search` | ADMIN | Search appointments with filters |

### POST `/logged/appointments/create`
**Body:**
```json
{
  "date_appointment": "2026-04-01",
  "time_appointment": "10:00:00",
  "duration": 60,
  "enterprise": { "id": 1 }
}
```

### POST `/logged/appointments/admin-create`
The enterprise is automatically taken from the authenticated admin's linked enterprise.
**Body:**
```json
{
  "date_appointment": "2026-04-01",
  "time_appointment": "10:00:00",
  "duration": 60,
  "id_client": { "id": 5 }
}
```

### GET `/logged/appointments/search`
**Query params:**
| Param | Type | Required | Description |
|-------|------|----------|-------------|
| page | Integer | No (default: 0) | Page number |
| size | Integer | No (default: 10) | Page size |
| id | Long | No | Filter by ID |
| date | String | No | Filter by date (yyyy-MM-dd) |
| time | String | No | Filter by time (HH:mm:ss) |
| duration | String | No | Filter by duration |
| status | String | No | Filter by status (PLANIFIED, CANCELLED) |
| token | String | No | Filter by token |

---

## Enterprises — `/logged/enterprises`

| Method | Route | Role | Description |
|--------|-------|------|-------------|
| POST | `/logged/enterprises/create` | ADMIN | Create an enterprise |
| GET | `/logged/enterprises/all` | ADMIN | Get all enterprises |
| GET | `/logged/enterprises/{id}` | ADMIN | Get enterprise by ID |
| GET | `/logged/enterprises/{id}/details` | ADMIN | Get enterprise with breaks and holidays |
| GET | `/logged/enterprises/{id}/breaks` | ADMIN | Get enterprise with breaks only |
| GET | `/logged/enterprises/{id}/holidays` | ADMIN | Get enterprise with holidays only |
| PUT | `/logged/enterprises/update/{id}` | ADMIN | Update an enterprise |
| DELETE | `/logged/enterprises/delete/{id}` | ADMIN | Delete an enterprise |

### POST `/logged/enterprises/create`
### PUT `/logged/enterprises/update/{id}`
**Body:**
```json
{
  "name": "My Workshop",
  "openingTime": "08:00:00",
  "closingTime": "18:00:00",
  "daysOff": "SUNDAY"
}
```
`daysOff` accepts: `MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY`

---

## Holidays — `/logged/holidays`

| Method | Route | Role | Description |
|--------|-------|------|-------------|
| POST | `/logged/holidays/create` | ADMIN | Create a holiday period |
| GET | `/logged/holidays/all` | ADMIN | Get all holidays |
| GET | `/logged/holidays/{id}` | ADMIN | Get holiday by ID |
| GET | `/logged/holidays/by-enterprise/{id_enterprise}` | ADMIN | Get holidays by enterprise |
| PUT | `/logged/holidays/update/{id}` | ADMIN | Update a holiday |
| DELETE | `/logged/holidays/delete/{id}` | ADMIN | Delete a holiday |

### POST `/logged/holidays/create`
### PUT `/logged/holidays/update/{id}`
**Body:**
```json
{
  "name": "Summer closure",
  "startDate": "2026-07-15",
  "endDate": "2026-08-15",
  "description": "Annual summer break"
}
```
**Rules:**
- `startDate` must be before or equal to `endDate`
- `endDate` cannot be in the past

---

## Breaks — `/logged/breaks`

| Method | Route | Role | Description |
|--------|-------|------|-------------|
| POST | `/logged/breaks/create` | ADMIN | Create a break |
| GET | `/logged/breaks/all` | ADMIN | Get all breaks |
| GET | `/logged/breaks/{id}` | ADMIN | Get break by ID |
| GET | `/logged/breaks/by-enterprise/{id_enterprise}` | ADMIN | Get breaks by enterprise |
| PUT | `/logged/breaks/update/{id}` | ADMIN | Update a break |
| DELETE | `/logged/breaks/delete/{id}` | ADMIN | Delete a break |

### POST `/logged/breaks/create`
### PUT `/logged/breaks/update/{id}`
**Body:**
```json
{
  "startTime": "12:00:00",
  "endTime": "13:00:00",
  "daysOff": "MONDAY",
  "enterpriseId": 1
}
```
**Rules:**
- `startTime` must be before `endTime`
- Break must be within enterprise opening hours
- Break cannot overlap with existing breaks on the same day
- `daysOff` accepts: `MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY`

---

## Business Rules

### Appointment creation
- Cannot create appointment in the past
- Cannot create appointment on enterprise day off (`daysOff`)
- Cannot create appointment during a holiday period
- Cannot create appointment during a break
- Appointment must start and end within opening hours
- Cannot create duplicate appointment (same client, same date, same time)
- Slot must be available (no other appointment at same date/time)
- Admin's enterprise is automatically used — cannot create appointment for another enterprise

### Appointment cancellation
- Must be cancelled at least **12 hours** before the appointment
- A `CANCELLED` appointment cannot be cancelled again
- A CLIENT can only cancel their own appointments
- An ADMIN can cancel any appointment

### Admin creation
- An admin must be linked to an existing enterprise at creation
- An admin can only manage appointments for their own enterprise

### Error responses
| HTTP Code | Situation |
|-----------|-----------|
| 400 | Invalid date/time, past date, invalid schedule, missing enterpriseId for admin |
| 403 | Unauthorized access to another user's appointment |
| 404 | Resource not found |
| 409 | Conflict (slot taken, already cancelled, overlap) |
