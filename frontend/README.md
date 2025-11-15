# Frontend Setup Instructions

## Quick Start

### 1. Install Dependencies
```bash
cd frontend
npm install
```

### 2. Environment Configuration
```bash
cp .env.example .env
```

### 3. Start Development Server
```bash
npm start
```

The application will open automatically at `http://localhost:3000`

## Project Structure

```
frontend/
├── src/
│   ├── components/
│   │   ├── BusinessDetailsPage.jsx    # Main page component
│   │   ├── EditBusinessForm.jsx       # Edit form with OTP handling
│   │   ├── ResellerInfo.jsx           # Reseller information display
│   │   └── OTPModal.jsx               # OTP verification modal
│   ├── api/
│   │   └── businessApi.js             # API service layer with axios
│   ├── hooks/
│   │   └── useBusinessDetails.js      # Custom hook for fetching data
│   ├── App.js
│   ├── index.js
│   └── index.css
├── public/
│   └── index.html
├── package.json
├── tailwind.config.js
├── postcss.config.js
└── .env.example
```

## Available Scripts

- `npm start` - Run development server
- `npm build` - Build for production
- `npm test` - Run tests
- `npm eject` - Eject from Create React App (irreversible)

## Features

### BusinessDetailsPage
- Displays complete business information
- Shows associated reseller information (if available)
- Loading and error states
- Edit button to switch to edit mode

### EditBusinessForm
- Form validation
- Detects sensitive field changes (email, phone)
- Shows warning when sensitive fields are modified
- Triggers OTP modal for sensitive changes
- Direct update for non-sensitive field changes

### OTPModal
- Supports both Email and SMS OTP delivery
- 5-minute countdown timer
- OTP expiration handling
- Maximum 6-digit OTP input
- Real-time verification feedback

### Responsive Design
- Mobile-first Tailwind CSS
- Gradient backgrounds
- Smooth transitions and hover effects
- Accessible form inputs

## API Integration

All API calls use axios with centralized error handling. Examples:

### Get Business Details
```javascript
const { data } = await getBusinessDetails(1);
console.log(data); // { name, email, phone, ... }
```

### Send OTP
```javascript
const { data } = await sendOtp({
  businessId: 1,
  recipient: 'email@example.com',
  method: 'EMAIL',
  fieldName: 'email'
});
```

### Verify OTP
```javascript
const { data } = await verifyOtp({
  businessId: 1,
  otp: '123456',
  method: 'EMAIL'
});
```

## Tailwind CSS

Custom styling with Tailwind utility classes:
- Color scheme: Blue primary, Purple secondary
- Responsive breakpoints: sm, md, lg, xl
- Custom fonts and spacing
- Gradient overlays on headers

## Hooks

### useBusinessDetails
- Fetches business and reseller information
- Manages loading and error states
- Auto-refetch when businessId changes

```javascript
const { business, reseller, loading, error } = useBusinessDetails(1);
```

## Dependencies

- **react**: UI framework
- **react-dom**: React rendering
- **axios**: HTTP client
- **tailwindcss**: Utility-first CSS framework
- **react-scripts**: Build tools

## Environment Variables

```env
REACT_APP_API_BASE_URL=http://localhost:8080/api
```

## Common Issues

1. **CORS Errors**: Ensure backend CORS is configured for `http://localhost:3000`
2. **API Connection Failed**: Check if backend is running on port 8080
3. **Tailwind Styles Not Loading**: Run `npm install -D tailwindcss autoprefixer postcss`

## Production Build

```bash
npm run build
```

Output: `/build` directory ready for deployment

## Testing the OTP Flow

1. Click "Edit Details"
2. Change email or phone number
3. Click "Save Changes"
4. OTP modal appears
5. Check console or mock email service for OTP code
6. Enter OTP (default: 123456 for testing)
7. Click "Verify"
8. Changes are saved
