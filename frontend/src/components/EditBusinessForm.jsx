import React, { useState } from 'react';
import { updateBusinessDetails, sendOtp } from '../api/businessApi';

const SENSITIVE_FIELDS = ['email', 'phone'];

const EditBusinessForm = ({ business, onCancel, onOtpRequired, businessId }) => {
  const [formData, setFormData] = useState({
    name: business.name || '',
    email: business.email || '',
    phone: business.phone || '',
    website: business.website || '',
    address: business.address || '',
    city: business.city || '',
    state: business.state || '',
    zipCode: business.zipCode || '',
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [changedFields, setChangedFields] = useState(new Set());

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    setChangedFields((prev) => new Set(prev).add(name));
  };

  const hasSensitiveChanges = () => {
    return Array.from(changedFields).some((field) =>
      SENSITIVE_FIELDS.includes(field) &&
      formData[field] !== business[field]
    );
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      // Check if there are sensitive field changes
      if (hasSensitiveChanges()) {
        // Determine which sensitive field was changed
        const sensitiveField = Array.from(changedFields).find(
          (field) => SENSITIVE_FIELDS.includes(field) && formData[field] !== business[field]
        );

        // Prepare OTP context
        const otpContext = {
          field: sensitiveField,
          value: formData[sensitiveField],
          method: sensitiveField === 'email' ? 'EMAIL' : 'SMS',
          recipient: formData[sensitiveField],
          businessId: businessId,
          formData: formData,
        };

        // Send OTP
        try {
          await sendOtp({
            businessId: businessId,
            recipient: formData[sensitiveField],
            method: otpContext.method,
            fieldName: sensitiveField,
          });
          onOtpRequired(otpContext);
        } catch (err) {
          setError(`Failed to send OTP: ${err.response?.data?.message || err.message}`);
        }
      } else {
        // No sensitive changes, update directly
        const response = await updateBusinessDetails(businessId, formData);
        alert('Business details updated successfully!');
        onCancel();
      }
    } catch (err) {
      setError(
        `Failed to update business details: ${err.response?.data?.message || err.message}`
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      {error && (
        <div className="bg-red-50 border border-red-200 rounded-lg p-4">
          <p className="text-red-600">{error}</p>
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-gray-700 font-medium mb-2">Business Name</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleInputChange}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>

        <div>
          <label className="block text-gray-700 font-medium mb-2">
            Email {SENSITIVE_FIELDS.includes('email') && <span className="text-red-600">*Sensitive</span>}
          </label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleInputChange}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>

        <div>
          <label className="block text-gray-700 font-medium mb-2">
            Phone {SENSITIVE_FIELDS.includes('phone') && <span className="text-red-600">*Sensitive</span>}
          </label>
          <input
            type="tel"
            name="phone"
            value={formData.phone}
            onChange={handleInputChange}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>

        <div>
          <label className="block text-gray-700 font-medium mb-2">Website</label>
          <input
            type="url"
            name="website"
            value={formData.website}
            onChange={handleInputChange}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="https://example.com"
          />
        </div>

        <div className="md:col-span-2">
          <label className="block text-gray-700 font-medium mb-2">Address</label>
          <input
            type="text"
            name="address"
            value={formData.address}
            onChange={handleInputChange}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>

        <div>
          <label className="block text-gray-700 font-medium mb-2">City</label>
          <input
            type="text"
            name="city"
            value={formData.city}
            onChange={handleInputChange}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>

        <div>
          <label className="block text-gray-700 font-medium mb-2">State</label>
          <input
            type="text"
            name="state"
            value={formData.state}
            onChange={handleInputChange}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>

        <div>
          <label className="block text-gray-700 font-medium mb-2">Zip Code</label>
          <input
            type="text"
            name="zipCode"
            value={formData.zipCode}
            onChange={handleInputChange}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>
      </div>

      {hasSensitiveChanges() && (
        <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
          <p className="text-yellow-800">
            <span className="font-medium">⚠️ Note:</span> You have changed sensitive information (email/phone).
            You'll need to verify with OTP before updating.
          </p>
        </div>
      )}

      <div className="flex gap-4 justify-end">
        <button
          type="button"
          onClick={onCancel}
          className="px-6 py-2 border border-gray-300 rounded-lg text-gray-700 font-medium hover:bg-gray-50 transition duration-200"
        >
          Cancel
        </button>
        <button
          type="submit"
          disabled={loading}
          className="px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg transition duration-200 disabled:opacity-50"
        >
          {loading ? 'Processing...' : 'Save Changes'}
        </button>
      </div>
    </form>
  );
};

export default EditBusinessForm;
