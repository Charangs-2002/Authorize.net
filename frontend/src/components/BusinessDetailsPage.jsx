import React, { useState } from 'react';
import useBusinessDetails from '../hooks/useBusinessDetails';
import EditBusinessForm from './EditBusinessForm';
import ResellerInfo from './ResellerInfo';
import OTPModal from './OTPModal';

const BusinessDetailsPage = () => {
  const DEMO_BUSINESS_ID = 1; // Default for demo
  const { business, reseller, loading, error } = useBusinessDetails(DEMO_BUSINESS_ID);
  const [isEditing, setIsEditing] = useState(false);
  const [showOtpModal, setShowOtpModal] = useState(false);
  const [otpContext, setOtpContext] = useState(null);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-lg text-gray-600">Loading business details...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="bg-red-50 border border-red-200 rounded-lg p-6 max-w-md">
          <h2 className="text-red-800 font-semibold mb-2">Error</h2>
          <p className="text-red-600">{error}</p>
        </div>
      </div>
    );
  }

  if (!business) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-lg text-gray-600">No business details found</div>
      </div>
    );
  }

  const handleEditClick = () => {
    setIsEditing(true);
  };

  const handleEditCancel = () => {
    setIsEditing(false);
  };

  const handleOtpRequired = (context) => {
    setOtpContext(context);
    setShowOtpModal(true);
  };

  const handleOtpModalClose = () => {
    setShowOtpModal(false);
    setOtpContext(null);
    setIsEditing(false);
  };

  return (
    <div className="max-w-4xl mx-auto p-6">
      <div className="bg-white rounded-lg shadow-lg overflow-hidden">
        {/* Header */}
        <div className="bg-gradient-to-r from-blue-600 to-blue-800 px-6 py-8">
          <h1 className="text-3xl font-bold text-white mb-2">Business Details</h1>
          <p className="text-blue-100">Manage your organization information</p>
        </div>

        {/* Content */}
        <div className="p-6">
          {isEditing ? (
            <EditBusinessForm
              business={business}
              onCancel={handleEditCancel}
              onOtpRequired={handleOtpRequired}
              businessId={DEMO_BUSINESS_ID}
            />
          ) : (
            <>
              {/* Business Details Display */}
              <div className="mb-8">
                <div className="flex justify-between items-center mb-6">
                  <h2 className="text-2xl font-semibold text-gray-800">Organization Information</h2>
                  <button
                    onClick={handleEditClick}
                    className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-6 rounded-lg transition duration-200"
                  >
                    Edit Details
                  </button>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div className="bg-gray-50 p-4 rounded-lg">
                    <label className="text-gray-600 text-sm font-medium">Business Name</label>
                    <p className="text-gray-900 text-lg font-medium mt-1">{business.name}</p>
                  </div>

                  <div className="bg-gray-50 p-4 rounded-lg">
                    <label className="text-gray-600 text-sm font-medium">Email</label>
                    <p className="text-gray-900 text-lg font-medium mt-1">{business.email}</p>
                  </div>

                  <div className="bg-gray-50 p-4 rounded-lg">
                    <label className="text-gray-600 text-sm font-medium">Phone</label>
                    <p className="text-gray-900 text-lg font-medium mt-1">{business.phone}</p>
                  </div>

                  <div className="bg-gray-50 p-4 rounded-lg">
                    <label className="text-gray-600 text-sm font-medium">Website</label>
                    <p className="text-gray-900 text-lg font-medium mt-1">{business.website || 'N/A'}</p>
                  </div>

                  <div className="bg-gray-50 p-4 rounded-lg md:col-span-2">
                    <label className="text-gray-600 text-sm font-medium">Address</label>
                    <p className="text-gray-900 text-lg font-medium mt-1">
                      {business.address}, {business.city}, {business.state} {business.zipCode}
                    </p>
                  </div>
                </div>
              </div>

              {/* Reseller Information */}
              {reseller && <ResellerInfo reseller={reseller} />}
            </>
          )}
        </div>
      </div>

      {/* OTP Modal */}
      {showOtpModal && otpContext && (
        <OTPModal
          context={otpContext}
          onClose={handleOtpModalClose}
          businessId={DEMO_BUSINESS_ID}
        />
      )}
    </div>
  );
};

export default BusinessDetailsPage;
