**ROUTE MAPPING**

*** /auth ***

- **POST** /login
- **POST** /register

*** /api/users ***

- **GET** /current
- **GET** /getById/:id
- **GET** /getAll

- **PUT** /update/:id
- **PUT** /updateCurrentUser

- **DELETE** /delete/:id
- **DELETE** /deleteCurrentUser


*** /appointments/public ***

- **POST** /create
- **GET** /consult
- **POST** /cancel

*** /logged/appointments ***

- **POST** /create
- **PUT** /cancel/:id
- **GET** /all
- **GET** /:id
- **GET** /by-date?date=YYYY-MM-DD
- **GET** /my-appointments
- **GET** /by-client/:id
- **GET** /by-creator/:id
- **POST** /admin-create


