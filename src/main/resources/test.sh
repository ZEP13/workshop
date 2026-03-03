cat > tests.sh << 'EOF'
#!/bin/bash

BASE_URL="http://localhost:8080"
ADMIN_TOKEN=""
CLIENT_TOKEN=""

GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

print_test() { echo -e "${BLUE}>>> $1${NC}"; }
print_ok()   { echo -e "${GREEN}OK: $1${NC}"; }
print_err()  { echo -e "${RED}ERR: $1${NC}"; }

# ─── 1. ENTERPRISE ────────────────────────────────────────────────────────────

print_test "Create enterprise"
curl -s -X POST "$BASE_URL/logged/enterprises/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "name": "Workshop Test",
    "openingTime": "08:00:00",
    "closingTime": "18:00:00",
    "daysOff": "SUNDAY"
  }' | jq .

print_test "Get all enterprises"
curl -s -X GET "$BASE_URL/logged/enterprises/all" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Get enterprise by ID"
curl -s -X GET "$BASE_URL/logged/enterprises/1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Get enterprise with details"
curl -s -X GET "$BASE_URL/logged/enterprises/1/details" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Get enterprise breaks only"
curl -s -X GET "$BASE_URL/logged/enterprises/1/breaks" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Get enterprise holidays only"
curl -s -X GET "$BASE_URL/logged/enterprises/1/holidays" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Update enterprise"
curl -s -X PUT "$BASE_URL/logged/enterprises/update/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "name": "Workshop Updated",
    "openingTime": "09:00:00",
    "closingTime": "17:00:00",
    "daysOff": "SUNDAY"
  }' | jq .

# ─── 2. BREAKS ────────────────────────────────────────────────────────────────

print_test "Create break"
curl -s -X POST "$BASE_URL/logged/breaks/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "startTime": "12:00:00",
    "endTime": "13:00:00",
    "daysOff": "MONDAY",
    "enterpriseId": 1
  }' | jq .

print_test "Create break overlap ERROR (should fail)"
curl -s -X POST "$BASE_URL/logged/breaks/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "startTime": "12:30:00",
    "endTime": "13:30:00",
    "daysOff": "MONDAY",
    "enterpriseId": 1
  }' | jq .

print_test "Create break outside opening hours ERROR (should fail)"
curl -s -X POST "$BASE_URL/logged/breaks/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "startTime": "07:00:00",
    "endTime": "08:00:00",
    "daysOff": "TUESDAY",
    "enterpriseId": 1
  }' | jq .

print_test "Get all breaks"
curl -s -X GET "$BASE_URL/logged/breaks/all" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Get break by ID"
curl -s -X GET "$BASE_URL/logged/breaks/1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Get breaks by enterprise"
curl -s -X GET "$BASE_URL/logged/breaks/by-enterprise/1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Update break"
curl -s -X PUT "$BASE_URL/logged/breaks/update/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "startTime": "12:00:00",
    "endTime": "13:00:00",
    "daysOff": "MONDAY",
    "enterpriseId": 1
  }' | jq .

# ─── 3. HOLIDAYS ──────────────────────────────────────────────────────────────

print_test "Create holiday"
curl -s -X POST "$BASE_URL/logged/holidays/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "name": "Summer closure",
    "startDate": "2026-07-15",
    "endDate": "2026-08-15",
    "description": "Annual summer break"
  }' | jq .

print_test "Create holiday in past ERROR (should fail)"
curl -s -X POST "$BASE_URL/logged/holidays/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "name": "Past holiday",
    "startDate": "2024-01-01",
    "endDate": "2024-01-10",
    "description": "Should fail"
  }' | jq .

print_test "Create holiday startDate after endDate ERROR (should fail)"
curl -s -X POST "$BASE_URL/logged/holidays/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "name": "Bad dates",
    "startDate": "2026-08-15",
    "endDate": "2026-07-15",
    "description": "Should fail"
  }' | jq .

print_test "Get all holidays"
curl -s -X GET "$BASE_URL/logged/holidays/all" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Get holiday by ID"
curl -s -X GET "$BASE_URL/logged/holidays/1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Get holidays by enterprise"
curl -s -X GET "$BASE_URL/logged/holidays/by-enterprise/1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Update holiday"
curl -s -X PUT "$BASE_URL/logged/holidays/update/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "name": "Summer closure updated",
    "startDate": "2026-07-20",
    "endDate": "2026-08-20",
    "description": "Updated"
  }' | jq .

# ─── 4. AUTH ──────────────────────────────────────────────────────────────────

print_test "Register client"
curl -s -X POST "$BASE_URL/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "client@test.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "0479000001",
    "tokenRdv": null
  }' | jq .

print_test "Register duplicate ERROR (should fail)"
curl -s -X POST "$BASE_URL/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "client@test.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "0479000001",
    "tokenRdv": null
  }' | jq .

print_test "Login client"
CLIENT_TOKEN=$(curl -s -X POST "$BASE_URL/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "client@test.com",
    "password": "password123"
  }' | jq -r '.token')
echo "CLIENT_TOKEN: $CLIENT_TOKEN"

print_test "Login admin"
ADMIN_TOKEN=$(curl -s -X POST "$BASE_URL/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@test.com",
    "password": "password123"
  }' | jq -r '.token')
echo "ADMIN_TOKEN: $ADMIN_TOKEN"

print_test "Create admin linked to enterprise"
curl -s -X POST "$BASE_URL/admin/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "email": "admin2@test.com",
    "password": "password123",
    "firstName": "Jane",
    "lastName": "Admin",
    "phone": "0479000002",
    "enterpriseId": 1
  }' | jq .

print_test "Create admin without enterprise ERROR (should fail)"
curl -s -X POST "$BASE_URL/admin/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "email": "admin3@test.com",
    "password": "password123",
    "firstName": "Bad",
    "lastName": "Admin",
    "phone": "0479000003"
  }' | jq .

# ─── 5. USERS ─────────────────────────────────────────────────────────────────

print_test "Get current user"
curl -s -X GET "$BASE_URL/logged/users/me" \
  -H "Authorization: Bearer $CLIENT_TOKEN" | jq .

print_test "Get all users"
curl -s -X GET "$BASE_URL/logged/users/all" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Get user by ID"
curl -s -X GET "$BASE_URL/logged/users/1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Get user by email"
curl -s -X GET "$BASE_URL/logged/users/by-email?email=client@test.com" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Get user by name"
curl -s -X GET "$BASE_URL/logged/users/by-name?firstName=John&lastName=Doe" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Search users by role"
curl -s -X GET "$BASE_URL/logged/users/search?page=0&size=10&role=CLIENT" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Update current user"
curl -s -X PUT "$BASE_URL/logged/users/update/me" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $CLIENT_TOKEN" \
  -d '{
    "email": "client@test.com",
    "password": "newpassword123",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "0479000001"
  }' | jq .

print_test "Update user by ID as admin"
curl -s -X PUT "$BASE_URL/logged/users/update/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "email": "client@test.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "0479000001"
  }' | jq .

# ─── 6. PUBLIC APPOINTMENTS ───────────────────────────────────────────────────

print_test "Create public appointment without account"
PUBLIC_RESPONSE=$(curl -s -X POST "$BASE_URL/appointments/public/create" \
  -H "Content-Type: application/json" \
  -d '{
    "date_appointment": "2026-04-10",
    "time_appointment": "10:00:00",
    "duration": 60,
    "enterprise": { "id": 1 },
    "email_client": "public@test.com"
  }')
echo $PUBLIC_RESPONSE | jq .
PUBLIC_TOKEN=$(echo $PUBLIC_RESPONSE | jq -r '.token')
echo "PUBLIC_TOKEN: $PUBLIC_TOKEN"

print_test "Consult public appointment by token"
curl -s -X GET "$BASE_URL/appointments/public/consult?token=$PUBLIC_TOKEN" | jq .

print_test "Consult invalid token ERROR (should fail)"
curl -s -X GET "$BASE_URL/appointments/public/consult?token=invalid-token-123" | jq .

print_test "Cancel public appointment by token"
curl -s -X PUT "$BASE_URL/appointments/public/cancel?token=$PUBLIC_TOKEN" | jq .

print_test "Cancel already cancelled public appointment ERROR (should fail)"
curl -s -X PUT "$BASE_URL/appointments/public/cancel?token=$PUBLIC_TOKEN" | jq .

# ─── 7. APPOINTMENTS ──────────────────────────────────────────────────────────

print_test "Create appointment as client"
curl -s -X POST "$BASE_URL/logged/appointments/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $CLIENT_TOKEN" \
  -d '{
    "date_appointment": "2026-04-07",
    "time_appointment": "10:00:00",
    "duration": 60,
    "enterprise": { "id": 1 }
  }' | jq .

print_test "Create appointment in past ERROR (should fail)"
curl -s -X POST "$BASE_URL/logged/appointments/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $CLIENT_TOKEN" \
  -d '{
    "date_appointment": "2024-01-01",
    "time_appointment": "10:00:00",
    "duration": 60,
    "enterprise": { "id": 1 }
  }' | jq .

print_test "Create appointment on daysOff SUNDAY ERROR (should fail)"
curl -s -X POST "$BASE_URL/logged/appointments/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $CLIENT_TOKEN" \
  -d '{
    "date_appointment": "2026-04-05",
    "time_appointment": "10:00:00",
    "duration": 60,
    "enterprise": { "id": 1 }
  }' | jq .

print_test "Create appointment during break MONDAY 12h ERROR (should fail)"
curl -s -X POST "$BASE_URL/logged/appointments/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $CLIENT_TOKEN" \
  -d '{
    "date_appointment": "2026-04-06",
    "time_appointment": "12:00:00",
    "duration": 60,
    "enterprise": { "id": 1 }
  }' | jq .

print_test "Create appointment during holiday ERROR (should fail)"
curl -s -X POST "$BASE_URL/logged/appointments/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $CLIENT_TOKEN" \
  -d '{
    "date_appointment": "2026-07-25",
    "time_appointment": "10:00:00",
    "duration": 60,
    "enterprise": { "id": 1 }
  }' | jq .

print_test "Create appointment outside opening hours ERROR (should fail)"
curl -s -X POST "$BASE_URL/logged/appointments/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $CLIENT_TOKEN" \
  -d '{
    "date_appointment": "2026-04-07",
    "time_appointment": "07:00:00",
    "duration": 60,
    "enterprise": { "id": 1 }
  }' | jq .

print_test "Create appointment as admin for client"
curl -s -X POST "$BASE_URL/logged/appointments/admin-create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "date_appointment": "2026-04-08",
    "time_appointment": "14:00:00",
    "duration": 30,
    "id_client": { "id": 1 }
  }' | jq .

print_test "Get all appointments"
curl -s -X GET "$BASE_URL/logged/appointments/all" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Get appointment by ID as client (own)"
curl -s -X GET "$BASE_URL/logged/appointments/1" \
  -H "Authorization: Bearer $CLIENT_TOKEN" | jq .

print_test "Get appointment by ID as client (other) ERROR (should fail)"
curl -s -X GET "$BASE_URL/logged/appointments/99" \
  -H "Authorization: Bearer $CLIENT_TOKEN" | jq .

print_test "Get appointments by date"
curl -s -X GET "$BASE_URL/logged/appointments/by-date?date=2026-04-07" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Get my appointments"
curl -s -X GET "$BASE_URL/logged/appointments/my-appointments" \
  -H "Authorization: Bearer $CLIENT_TOKEN" | jq .

print_test "Get my appointments by date"
curl -s -X GET "$BASE_URL/logged/appointments/my-appointments/by-date?date=2026-04-07" \
  -H "Authorization: Bearer $CLIENT_TOKEN" | jq .

print_test "Get appointments by client"
curl -s -X GET "$BASE_URL/logged/appointments/by-client/1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Get appointments by creator"
curl -s -X GET "$BASE_URL/logged/appointments/by-creator/1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Search appointments by status"
curl -s -X GET "$BASE_URL/logged/appointments/search?page=0&size=10&status=PLANIFIED" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Cancel appointment as admin"
curl -s -X PUT "$BASE_URL/logged/appointments/cancel/1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Cancel already cancelled ERROR (should fail)"
curl -s -X PUT "$BASE_URL/logged/appointments/cancel/1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Cancel other client appointment as client ERROR (should fail)"
curl -s -X PUT "$BASE_URL/logged/appointments/cancel/2" \
  -H "Authorization: Bearer $CLIENT_TOKEN" | jq .

# ─── 8. CLEANUP ───────────────────────────────────────────────────────────────

print_test "Delete break"
curl -s -X DELETE "$BASE_URL/logged/breaks/delete/1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Delete holiday"
curl -s -X DELETE "$BASE_URL/logged/holidays/delete/1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

print_test "Delete current user"
curl -s -X DELETE "$BASE_URL/logged/users/delete/me" \
  -H "Authorization: Bearer $CLIENT_TOKEN" | jq .

print_test "Delete enterprise"
curl -s -X DELETE "$BASE_URL/logged/enterprises/delete/1" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

echo -e "${GREEN}=== Tests done ===${NC}"
EOF
chmod +x tests.sh
