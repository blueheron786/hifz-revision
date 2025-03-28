import { Navigate, Outlet } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { useContext } from "react";

// Reroute users to login if not authenticated
const PrivateRoute = () => {
  const auth = useContext(AuthContext);
  return auth?.token ? <Outlet /> : <Navigate to="/login" />;
};

export default PrivateRoute;
