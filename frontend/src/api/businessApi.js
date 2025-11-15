import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';

// Create axios instance with default config
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor for error handling
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Handle unauthorized
      console.error('Unauthorized access');
    }
    return Promise.reject(error);
  }
);

/**
 * Get business details by ID
 * @param {number} businessId - The business ID
 * @returns {Promise} Business details
 */
export const getBusinessDetails = (businessId) => {
  return apiClient.get(`/business/${businessId}`);
};

/**
 * Update business details
 * @param {number} businessId - The business ID
 * @param {Object} businessData - Updated business data
 * @returns {Promise} Updated business details
 */
export const updateBusinessDetails = (businessId, businessData) => {
  return apiClient.put(`/business/${businessId}`, businessData);
};

/**
 * Get reseller information
 * @param {number} businessId - The business ID
 * @returns {Promise} Reseller information
 */
export const getResellerInfo = (businessId) => {
  return apiClient.get(`/reseller/${businessId}`);
};

/**
 * Send OTP for verification
 * @param {Object} otpRequest - Contains businessId, email/phone, and delivery method
 * @returns {Promise} OTP send response
 */
export const sendOtp = (otpRequest) => {
  return apiClient.post('/otp/send', otpRequest);
};

/**
 * Verify OTP
 * @param {Object} otpVerifyRequest - Contains businessId, otp, and delivery method
 * @returns {Promise} OTP verification response
 */
export const verifyOtp = (otpVerifyRequest) => {
  return apiClient.post('/otp/verify', otpVerifyRequest);
};

/**
 * Update business details with OTP verification
 * @param {number} businessId - The business ID
 * @param {Object} updateRequest - Contains businessData and otpToken
 * @returns {Promise} Updated business details
 */
export const updateBusinessWithOtp = (businessId, updateRequest) => {
  return apiClient.put(`/business/${businessId}/with-otp`, updateRequest);
};

export default apiClient;
