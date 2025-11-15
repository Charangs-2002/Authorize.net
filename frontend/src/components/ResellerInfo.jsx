import React from 'react';

const ResellerInfo = ({ reseller }) => {
  if (!reseller) {
    return null;
  }

  return (
    <div className="mt-8 border-t pt-8">
      <h2 className="text-2xl font-semibold text-gray-800 mb-6">Reseller Information</h2>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-gradient-to-br from-purple-50 to-purple-100 p-4 rounded-lg border border-purple-200">
          <label className="text-purple-700 text-sm font-medium">Reseller ID</label>
          <p className="text-purple-900 text-lg font-medium mt-1">{reseller.id}</p>
        </div>

        <div className="bg-gradient-to-br from-purple-50 to-purple-100 p-4 rounded-lg border border-purple-200">
          <label className="text-purple-700 text-sm font-medium">Reseller Name</label>
          <p className="text-purple-900 text-lg font-medium mt-1">{reseller.name}</p>
        </div>

        <div className="bg-gradient-to-br from-purple-50 to-purple-100 p-4 rounded-lg border border-purple-200">
          <label className="text-purple-700 text-sm font-medium">Commission Rate</label>
          <p className="text-purple-900 text-lg font-medium mt-1">{reseller.commissionRate}%</p>
        </div>

        <div className="bg-gradient-to-br from-purple-50 to-purple-100 p-4 rounded-lg border border-purple-200">
          <label className="text-purple-700 text-sm font-medium">Account Status</label>
          <p className="text-purple-900 text-lg font-medium mt-1">
            <span className={`px-3 py-1 rounded-full text-sm font-semibold ${
              reseller.status === 'ACTIVE'
                ? 'bg-green-100 text-green-800'
                : 'bg-gray-100 text-gray-800'
            }`}>
              {reseller.status}
            </span>
          </p>
        </div>

        <div className="bg-gradient-to-br from-purple-50 to-purple-100 p-4 rounded-lg border border-purple-200 md:col-span-2">
          <label className="text-purple-700 text-sm font-medium">Account Manager</label>
          <p className="text-purple-900 text-lg font-medium mt-1">{reseller.accountManager}</p>
        </div>
      </div>
    </div>
  );
};

export default ResellerInfo;
