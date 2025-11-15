# Business Details Module - Quick Reference Guide

## 📚 Quick Commands

### Start Backend
```bash
cd backend
mvn spring-boot:run
# http://localhost:8080/api
```

### Start Frontend
```bash
cd frontend
npm start
# http://localhost:3000
```

### Database Setup
```bash
mysql -u root -p
CREATE DATABASE business_details;
mysql -u root -p business_details < sample-data.sql
```

---

## 🔌 API Quick Reference

### Business Endpoints
```bash
# Get business details
curl http://localhost:8080/api/business/1

# Update business
curl -X PUT http://localhost:8080/api/business/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"New Name","email":"new@example.com",...}'

# Get all businesses
curl http://localhost:8080/api/business

# Create business
curl -X POST http://localhost:8080/api/business \
  -H "Content-Type: application/json" \
  -d '{...}'

# Delete business
curl -X DELETE http://localhost:8080/api/business/1
```

### OTP Endpoints
```bash
# Send OTP
curl -X POST http://localhost:8080/api/otp/send \
  -H "Content-Type: application/json" \
  -d '{
    "businessId": 1,
    "recipient": "email@example.com",
    "method": "EMAIL",
    "fieldName": "email"
  }'

# Verify OTP
curl -X POST http://localhost:8080/api/otp/verify \
  -H "Content-Type: application/json" \
  -d '{
    "businessId": 1,
    "otp": "123456",
    "method": "EMAIL"
  }'
```

### Reseller Endpoints
```bash
# Get reseller info
curl http://localhost:8080/api/reseller/1
```

---

## 📂 Key File Locations

### Frontend
| File | Purpose |
|------|---------|
| `src/components/BusinessDetailsPage.jsx` | Main page |
| `src/components/EditBusinessForm.jsx` | Edit form |
| `src/components/OTPModal.jsx` | OTP verification |
| `src/api/businessApi.js` | API client |
| `src/hooks/useBusinessDetails.js` | Data fetching |
| `tailwind.config.js` | Tailwind config |
| `package.json` | Dependencies |
| `.env.example` | Environment template |

### Backend
| File | Purpose |
|------|---------|
| `src/controller/BusinessController.java` | Business CRUD |
| `src/controller/OtpController.java` | OTP operations |
| `src/service/BusinessService.java` | Business logic |
| `src/service/OtpService.java` | OTP generation/verify |
| `src/entity/Business.java` | Business entity |
| `src/entity/Otp.java` | OTP entity |
| `pom.xml` | Dependencies |
| `application.properties` | Configuration |

---

## 🧪 Test Data

### Sample Business
```json
{
  "id": 1,
  "name": "Acme Corporation",
  "email": "contact@acme.com",
  "phone": "+1-555-0100",
  "website": "https://www.acme.com",
  "address": "123 Business Street",
  "city": "San Francisco",
  "state": "CA",
  "zipCode": "94105",
  "resellerId": 1
}
```

### Sample Reseller
```json
{
  "id": 1,
  "name": "Tech Solutions LLC",
  "commissionRate": 15.5,
  "status": "ACTIVE",
  "accountManager": "John Smith"
}
```

### Test OTP
- **Code**: `123456`
- **For any business with ID**: 1
- **Methods**: EMAIL or SMS

---

## ⚙️ Configuration Keys

### Backend (application.properties)

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/business_details
spring.datasource.username=root
spring.datasource.password=root

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Email (Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=app-password

# Server
server.port=8080
server.servlet.context-path=/api

# OTP
otp.expiry.minutes=5
otp.length=6
otp.max-attempts=3

# CORS
app.cors.allowed-origins=http://localhost:3000
```

### Frontend (.env)
```
REACT_APP_API_BASE_URL=http://localhost:8080/api
```

---

## 🔍 Component Structure

### Frontend Components
```
BusinessDetailsPage (Main)
├── Header (gradient blue)
├── Display Section
│   ├── Business Info (read-only)
│   └── Edit Button
├── Reseller Info (if exists)
└── EditBusinessForm (when editing)
    ├── Form Fields
    ├── Sensitive Field Detection
    └── Save/Cancel Buttons

OTPModal (Modal)
├── Recipient Info
├── OTP Input
├── Timer (5 min)
└── Verify Button

ResellerInfo (Component)
├── Commission Rate
├── Status Badge
└── Account Manager
```

### Backend Services
```
BusinessController
└── BusinessService
    └── BusinessRepository

OtpController
└── OtpService
    ├── EmailService
    └── SmsService
    └── OtpRepository

ResellerController
└── ResellerRepository
```

---

## 🚨 Common Issues & Fixes

| Issue | Fix |
|-------|-----|
| MySQL connection error | Ensure MySQL running: `mysql -u root -p` |
| Port 8080 in use | Change `server.port` in `application.properties` |
| Email not sending | Use Gmail App Password (not account password) |
| CORS error on frontend | Backend must be on `http://localhost:8080` |
| API not found (404) | Verify backend running: `http://localhost:8080/api` |
| Tailwind CSS not loading | Run `npm install -D tailwindcss` |
| .env not working | Restart frontend dev server after creating .env |

---

## 📊 Database Tables

### business
```sql
CREATE TABLE business (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  phone VARCHAR(20) NOT NULL UNIQUE,
  website VARCHAR(255),
  address VARCHAR(255) NOT NULL,
  city VARCHAR(100) NOT NULL,
  state VARCHAR(50) NOT NULL,
  zip_code VARCHAR(10) NOT NULL,
  reseller_id BIGINT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### reseller
```sql
CREATE TABLE reseller (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  commission_rate DECIMAL(10,2) NOT NULL,
  status VARCHAR(50) NOT NULL,
  account_manager VARCHAR(255) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### otp
```sql
CREATE TABLE otp (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  business_id BIGINT NOT NULL,
  otp VARCHAR(10) NOT NULL,
  recipient VARCHAR(255) NOT NULL,
  delivery_method VARCHAR(50) NOT NULL,
  status VARCHAR(50) NOT NULL,
  field_name VARCHAR(100),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  expires_at DATETIME NOT NULL,
  verified_at DATETIME,
  attempts INT DEFAULT 0,
  INDEX idx_business_id (business_id),
  INDEX idx_recipient (recipient)
);
```

---

## 🎯 API Response Format

### Success Response
```json
{
  "success": true,
  "message": "Success",
  "data": { ... },
  "timestamp": 1700000000000
}
```

### Error Response
```json
{
  "success": false,
  "message": "Error description",
  "timestamp": 1700000000000
}
```

### OTP Response
```json
{
  "success": true,
  "message": "OTP sent successfully",
  "verified": false,
  "otpId": "123"
}
```

---

## 🔐 Sensitive Fields

### Require OTP for Changes
- `email` - Email address
- `phone` - Phone number

### Allow Direct Update
- `name` - Business name
- `website` - Website URL
- `address` - Street address
- `city` - City
- `state` - State
- `zipCode` - Postal code

---

## 📋 Development Workflow

### 1. Backend Development
```bash
cd backend
# Edit Java files
# Run: mvn spring-boot:run
# Test: mvn test
# Build: mvn clean package
```

### 2. Frontend Development
```bash
cd frontend
# Edit React components
# Run: npm start
# Build: npm run build
# Test: npm test
```

### 3. API Testing
- Use Postman/Insomnia
- Or use curl commands
- Check backend logs for debugging

### 4. Database Queries
```bash
mysql -u root -p business_details
# Query/modify data
# Check schema
```

---

## 🔧 Customization

### Change OTP Expiry
**File**: `backend/src/main/resources/application.properties`
```properties
otp.expiry.minutes=5  # Change to desired minutes
```

### Change OTP Length
**File**: `backend/src/main/resources/application.properties`
```properties
otp.length=6  # Change to desired length
```

### Change Allowed CORS Origins
**File**: `backend/src/main/resources/application.properties`
```properties
app.cors.allowed-origins=http://localhost:3000,http://another-domain.com
```

### Change Email Provider
**File**: `backend/src/main/resources/application.properties`
```properties
spring.mail.host=smtp.your-provider.com
spring.mail.port=587
spring.mail.username=your-email@provider.com
spring.mail.password=your-password
```

---

## 📖 Documentation Files

| File | Content |
|------|---------|
| `README.md` | Project overview & quick start |
| `frontend/README.md` | Frontend setup, components, styling |
| `backend/README.md` | Backend setup, endpoints, configuration |
| `COMPLETION_SUMMARY.md` | Detailed project summary |
| `QUICK_REFERENCE.md` | This file - quick commands & reference |
| `sample-data.sql` | Sample data for testing |

---

## ✅ Verification Checklist

- [ ] MySQL database created
- [ ] Sample data loaded
- [ ] Backend running on port 8080
- [ ] Frontend running on port 3000
- [ ] Can view business details
- [ ] Can edit business details
- [ ] OTP modal appears for email/phone changes
- [ ] Can verify OTP (use: 123456)
- [ ] Business details updated after OTP
- [ ] Reseller info displays

---

## 🚀 Next Steps

1. **Configure Email**
   - Set up Gmail App Password
   - Update `application.properties`

2. **Load Production Data**
   - Modify `sample-data.sql`
   - Insert real business data

3. **Deploy**
   - Build: `mvn clean package` & `npm run build`
   - Docker: Follow backend/frontend README

4. **Extend Features**
   - Add authentication
   - Add API versioning
   - Integrate real SMS provider

---

**Last Updated**: November 15, 2025
