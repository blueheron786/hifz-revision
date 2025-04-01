import { useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';

export default function Dashboard() {
  const [data, setData] = useState<string | null>(null);
  const { token } = useAuth();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get('/api/dashboard', {
          headers: { Authorization: `Bearer ${token}` }
        });
        setData(response.data);
      } catch (error) {
        console.error('Error fetching dashboard data', error);
      }
    };

    if (token) fetchData();
  }, [token]);

  return (
    <div>
      <h1>Dashboard</h1>
      {data && <p>{data}</p>}
    </div>
  );
}