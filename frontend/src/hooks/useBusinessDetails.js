import { useState, useEffect } from 'react';
import { getBusinessDetails, getResellerInfo } from '../api/businessApi';

export const useBusinessDetails = (businessId) => {
  const [business, setBusiness] = useState(null);
  const [reseller, setReseller] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);
      try {
        const [businessRes, resellerRes] = await Promise.all([
          getBusinessDetails(businessId),
          getResellerInfo(businessId),
        ]);
        setBusiness(businessRes.data);
        setReseller(resellerRes.data);
      } catch (err) {
        setError(err.message || 'Failed to fetch business details');
        console.error('Error fetching data:', err);
      } finally {
        setLoading(false);
      }
    };

    if (businessId) {
      fetchData();
    }
  }, [businessId]);

  return { business, reseller, loading, error };
};

export default useBusinessDetails;
