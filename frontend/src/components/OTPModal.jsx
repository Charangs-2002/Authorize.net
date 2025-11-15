import React, { useState, useRef, useEffect } from 'react';
import { verifyOtp, updateBusinessDetails } from '../api/businessApi';

const OTPModal = ({ context, onClose, businessId }) => {
  const [otp, setOtp] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [timeLeft, setTimeLeft] = useState(300); // 5 minutes
  const [isExpired, setIsExpired] = useState(false);
  const otpInputRef = useRef(null);

  // Timer effect
  useEffect(() => {
    if (timeLeft <= 0) {
      setIsExpired(true);
      return;
    }

    const timer = setInterval(() => {
      setTimeLeft((prev) => prev - 1);
    }, 1000);

    return () => clearInterval(timer);
  }, [timeLeft]);

  // Focus OTP input on mount
  useEffect(() => {
    if (otpInputRef.current) {
      otpInputRef.current.focus();
    }
  }, []);

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  };

  const handleOtpChange = (e) => {
    const value = e.target.value.replace(/\D/g, '').slice(0, 6);
    setOtp(value);
  };

  const handleVerifyOtp = async (e) => {
    e.preventDefault();

    if (otp.length !== 6) {
      setError('OTP must be 6 digits');
      return;
    }

    if (isExpired) {
      setError('OTP has expired. Please request a new one.');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      // Verify OTP
      const verifyResponse = await verifyOtp({
        businessId: businessId,
        otp: otp,
        method: context.method,
        fieldName: context.field,
      });

      if (verifyResponse.data.verified) {
        // OTP verified, now update business details
        const updateResponse = await updateBusinessDetails(businessId, context.formData);
        alert('Business details updated successfully!');
        onClose();
      } else {
        setError('Invalid OTP. Please try again.');
      }
    } catch (err) {
      setError(
        `Verification failed: ${err.response?.data?.message || err.message}`
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-2xl max-w-md w-full mx-4">
        {/* Header */}
        <div className="bg-gradient-to-r from-blue-600 to-blue-800 px-6 py-6">
          <h2 className="text-2xl font-bold text-white">Verify Identity</h2>
          <p className="text-blue-100 mt-1">Enter the OTP sent to your {context.method === 'EMAIL' ? 'email' : 'phone'}</p>
        </div>

        {/* Content */}
        <div className="p-6">
          {/* Recipient Info */}
          <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
            <p className="text-sm text-gray-600 mb-1">Verification sent to:</p>
            <p className="text-lg font-semibold text-blue-900 break-all">{context.recipient}</p>
            <p className="text-xs text-gray-500 mt-2">
              We're updating your {context.field}. Please verify to confirm this change.
            </p>
          </div>

          {/* Error Message */}
          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-6">
              <p className="text-red-600 text-sm">{error}</p>
            </div>
          )}

          {/* OTP Input Form */}
          <form onSubmit={handleVerifyOtp}>
            <div className="mb-6">
              <label className="block text-gray-700 font-medium mb-2">Enter OTP</label>
              <input
                ref={otpInputRef}
                type="text"
                value={otp}
                onChange={handleOtpChange}
                placeholder="000000"
                disabled={isExpired}
                maxLength="6"
                className="w-full px-4 py-3 border-2 border-gray-300 rounded-lg text-center text-3xl tracking-widest focus:outline-none focus:border-blue-500 font-mono disabled:bg-gray-100"
              />
              <p className="text-xs text-gray-500 mt-1">6-digit code</p>
            </div>

            {/* Timer */}
            <div className={`text-center mb-6 ${isExpired ? 'text-red-600' : timeLeft < 60 ? 'text-orange-600' : 'text-gray-600'}`}>
              <p className="font-semibold">
                {isExpired ? '⏱️ OTP Expired' : `⏱️ ${formatTime(timeLeft)}`}
              </p>
            </div>

            {/* Action Buttons */}
            <div className="flex gap-3">
              <button
                type="button"
                onClick={onClose}
                className="flex-1 px-4 py-2 border border-gray-300 rounded-lg text-gray-700 font-medium hover:bg-gray-50 transition duration-200"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={loading || isExpired || otp.length !== 6}
                className="flex-1 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg transition duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? 'Verifying...' : 'Verify'}
              </button>
            </div>
          </form>

          {/* Resend Option */}
          <div className="mt-6 text-center">
            <p className="text-sm text-gray-600">
              Didn't receive the code?{' '}
              <button className="text-blue-600 hover:text-blue-800 font-medium">
                Resend OTP
              </button>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OTPModal;
