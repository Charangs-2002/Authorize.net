-- Business Details Module - Sample Data Script
-- Execute this script to populate sample data for testing

-- Create database (if not exists)
-- CREATE DATABASE IF NOT EXISTS business_details;
-- USE business_details;

-- Insert sample reseller data
INSERT INTO reseller (name, commission_rate, status, account_manager, created_at, updated_at)
VALUES 
  ('Tech Solutions LLC', 15.5, 'ACTIVE', 'John Smith', NOW(), NOW()),
  ('Digital Partners Inc', 12.0, 'ACTIVE', 'Sarah Johnson', NOW(), NOW()),
  ('Enterprise Solutions', 20.0, 'ACTIVE', 'Michael Brown', NOW(), NOW());

-- Insert sample business data
INSERT INTO business (name, email, phone, website, address, city, state, zip_code, reseller_id, created_at, updated_at)
VALUES 
  (
    'Acme Corporation',
    'contact@acme.com',
    '+1-555-0100',
    'https://www.acme.com',
    '123 Business Street',
    'San Francisco',
    'CA',
    '94105',
    1,
    NOW(),
    NOW()
  ),
  (
    'Global Enterprises Ltd',
    'info@globalenterprises.com',
    '+1-555-0101',
    'https://www.globalenterprises.com',
    '456 Corporate Avenue',
    'New York',
    'NY',
    '10001',
    2,
    NOW(),
    NOW()
  ),
  (
    'Innovation Tech Solutions',
    'support@innovationtech.com',
    '+1-555-0102',
    'https://www.innovationtech.com',
    '789 Technology Park',
    'Austin',
    'TX',
    '78701',
    3,
    NOW(),
    NOW()
  ),
  (
    'Creative Services Group',
    'hello@creativeservices.com',
    '+1-555-0103',
    'https://www.creativeservices.com',
    '321 Design Boulevard',
    'Los Angeles',
    'CA',
    '90001',
    1,
    NOW(),
    NOW()
  );

-- Insert sample OTP data (for testing - already expired)
INSERT INTO otp (business_id, otp, recipient, delivery_method, status, field_name, created_at, expires_at, attempts)
VALUES 
  (
    1,
    '123456',
    'contact@acme.com',
    'EMAIL',
    'VERIFIED',
    'email',
    DATE_SUB(NOW(), INTERVAL 10 MINUTE),
    DATE_SUB(NOW(), INTERVAL 5 MINUTE),
    1
  );

-- Verify data insertion
SELECT 'Reseller Records:' as 'Data Check';
SELECT COUNT(*) as total_resellers FROM reseller;

SELECT 'Business Records:' as 'Data Check';
SELECT COUNT(*) as total_businesses FROM business;

SELECT 'OTP Records:' as 'Data Check';
SELECT COUNT(*) as total_otps FROM otp;

-- Display all business details
SELECT 'All Business Details:' as 'Data Check';
SELECT 
  b.id,
  b.name,
  b.email,
  b.phone,
  b.city,
  b.state,
  r.name as reseller_name
FROM business b
LEFT JOIN reseller r ON b.reseller_id = r.id;
