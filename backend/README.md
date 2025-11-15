# Backend Setup Instructions

## Quick Start

### 1. Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### 2. Database Setup

Create the database:
```sql
CREATE DATABASE business_details;
```

The application will automatically create tables on first run (Hibernate DDL mode set to `update`).

### 3. Configuration

Edit `src/main/resources/application.properties`:

```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/business_details
spring.datasource.username=root
spring.datasource.password=root

# Email (for OTP via Email)
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password

# CORS
app.cors.allowed-origins=http://localhost:3306
```

### 4. Build & Run

```bash
cd backend

# Build
mvn clean package

# Run
mvn spring-boot:run
```

Server starts at: `http://localhost:8080/api`

## Project Structure

```
backend/
├── src/main/java/com/authorize/net/businessdetails/
│   ├── controller/
│   │   ├── BusinessController.java     # Business CRUD endpoints
│   │   ├── OtpController.java          # OTP send/verify endpoints
│   │   └── ResellerController.java     # Reseller info endpoint
│   ├── service/
│   │   ├── BusinessService.java        # Business logic
│   │   ├── OtpService.java             # OTP generation & verification
│   │   ├── EmailService.java           # Email delivery
│   │   └── SmsService.java             # SMS delivery (mock)
│   ├── entity/
│   │   ├── Business.java               # Business JPA entity
│   │   ├── Reseller.java               # Reseller JPA entity
│   │   └── Otp.java                    # OTP JPA entity
│   ├── repository/
│   │   ├── BusinessRepository.java
│   │   ├── ResellerRepository.java
│   │   └── OtpRepository.java
│   ├── dto/
│   │   ├── BusinessDto.java
│   │   ├── ResellerDto.java
│   │   ├── OtpRequest.java
│   │   ├── OtpVerifyRequest.java
│   │   ├── OtpResponse.java
│   │   └── ApiResponse.java
│   ├── config/
│   │   └── CorsConfig.java             # CORS configuration
│   └── BusinessDetailsApplication.java # Main class
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

## Entities

### Business
- id (PK)
- name (unique)
- email (unique)
- phone (unique)
- website
- address
- city
- state
- zipCode
- resellerId (FK)
- createdAt
- updatedAt

### Reseller
- id (PK)
- name
- commissionRate
- status (ACTIVE, INACTIVE, SUSPENDED)
- accountManager
- createdAt
- updatedAt

### Otp
- id (PK)
- businessId
- otp (6 digits)
- recipient (email/phone)
- deliveryMethod (EMAIL, SMS)
- status (PENDING, VERIFIED, EXPIRED, MAX_ATTEMPTS_EXCEEDED)
- fieldName
- createdAt
- expiresAt
- verifiedAt
- attempts

## API Endpoints

### Business Management

**GET** `/business/{id}` - Get business details
```bash
curl http://localhost:8080/api/business/1
```

**PUT** `/business/{id}` - Update business
```bash
curl -X PUT http://localhost:8080/api/business/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "New Name", "email": "new@example.com", ...}'
```

**GET** `/business` - Get all businesses
```bash
curl http://localhost:8080/api/business
```

**POST** `/business` - Create business
```bash
curl -X POST http://localhost:8080/api/business \
  -H "Content-Type: application/json" \
  -d '{"name": "...", "email": "...", ...}'
```

**DELETE** `/business/{id}` - Delete business
```bash
curl -X DELETE http://localhost:8080/api/business/1
```

### OTP Management

**POST** `/otp/send` - Send OTP
```bash
curl -X POST http://localhost:8080/api/otp/send \
  -H "Content-Type: application/json" \
  -d '{
    "businessId": 1,
    "recipient": "email@example.com",
    "method": "EMAIL",
    "fieldName": "email"
  }'
```

**POST** `/otp/verify` - Verify OTP
```bash
curl -X POST http://localhost:8080/api/otp/verify \
  -H "Content-Type: application/json" \
  -d '{
    "businessId": 1,
    "otp": "123456",
    "method": "EMAIL",
    "fieldName": "email"
  }'
```

### Reseller Information

**GET** `/reseller/{id}` - Get reseller
```bash
curl http://localhost:8080/api/reseller/1
```

## Email Configuration

### Gmail (Recommended for Testing)

1. Enable 2-Step Verification: https://myaccount.google.com/security
2. Generate App Password: https://myaccount.google.com/apppasswords
3. Select "Mail" and "Windows Computer"
4. Use generated password in `application.properties`

```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=generated-app-password
```

### Other Providers

```properties
# Outlook
spring.mail.host=smtp.outlook.com
spring.mail.port=587

# SendGrid
spring.mail.host=smtp.sendgrid.net
spring.mail.port=587
spring.mail.username=apikey
spring.mail.password=SG.xxxxx
```

## SMS Configuration

The `SmsService` includes a mock implementation. To integrate real SMS:

### Twilio Integration

1. Add dependency to `pom.xml`:
```xml
<dependency>
    <groupId>com.twilio.sdk</groupId>
    <artifactId>twilio</artifactId>
    <version>8.2.0</version>
</dependency>
```

2. Update `SmsService.java`:
```java
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public void sendOtp(String phoneNumber, String otp) {
    Message message = Twilio.getRestClient().messages.create(
        new PhoneNumber("+1" + phoneNumber),
        new PhoneNumber("+1234567890"),
        "Your OTP is: " + otp + ". Valid for 5 minutes."
    );
}
```

## OTP Configuration

Customize OTP behavior in `application.properties`:

```properties
# OTP expiry in minutes
otp.expiry.minutes=5

# OTP length in digits
otp.length=6

# Maximum verification attempts
otp.max-attempts=3
```

## Database Population

Load sample data:
```bash
mysql -u root -p business_details < sample-data.sql
```

Or manually via MySQL Workbench:
```sql
-- See sample-data.sql for complete script
INSERT INTO reseller (name, commission_rate, status, account_manager) 
VALUES ('Tech Solutions LLC', 15.5, 'ACTIVE', 'John Smith');

INSERT INTO business (name, email, phone, address, city, state, zip_code, reseller_id) 
VALUES ('Acme Corp', 'contact@acme.com', '+1-555-0100', '123 St', 'SF', 'CA', '94105', 1);
```

## Logging

Configure logging in `application.properties`:

```properties
logging.level.root=INFO
logging.level.com.authorize.net=DEBUG
logging.level.org.springframework.web=DEBUG
```

## Testing

### Manual Testing

1. Create business: POST `/business`
2. Fetch business: GET `/business/1`
3. Update business (non-sensitive): PUT `/business/1`
4. Update email/phone: Triggers OTP flow
5. Verify OTP: POST `/otp/verify`

### JUnit Testing

```bash
mvn test
```

Create test file: `src/test/java/com/authorize/net/businessdetails/BusinessControllerTest.java`

## Deployment

### Docker

Create `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/business-details-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

Build & run:
```bash
mvn clean package
docker build -t business-details-api .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-host:3306/business_details \
  -e SPRING_MAIL_USERNAME=... \
  business-details-api
```

### Production Settings

1. Set `hibernate.ddl-auto` to `validate` (don't auto-create tables)
2. Enable HTTPS
3. Configure proper authentication/authorization
4. Add API rate limiting
5. Enable request/response logging
6. Configure alerting and monitoring

## Troubleshooting

### MySQL Connection Error
- Verify MySQL is running: `mysql -u root -p`
- Check `application.properties` credentials
- Ensure database exists: `CREATE DATABASE business_details;`

### Email Service Error
- Verify credentials in `application.properties`
- For Gmail: Use App Password, not account password
- Check firewall allows outbound SMTP (port 587)

### OTP Not Sending
- Check email configuration
- Verify recipient email format
- Enable DEBUG logging: `logging.level.com.authorize.net=DEBUG`
- Check logs for detailed error messages

### CORS Error on Frontend
- Verify frontend URL in `app.cors.allowed-origins`
- Frontend should be on `http://localhost:3000`
- Restart backend after changing CORS config

## Support & Documentation

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [MySQL JDBC Documentation](https://dev.mysql.com/doc/connector-j/8.0/en/)
- [Lombok Documentation](https://projectlombok.org/)
