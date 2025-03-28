import { createContext, useState, ReactNode } from "react";
import { login as loginService, logout as logoutService, getToken } from "../services/authService";

interface AuthContextType {
  token: string | null;
  login: (username: string, password: string) => Promise<void>;
  logout: () => void;
}

// Authentication context: a page requires authz
export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [token, setToken] = useState<string | null>(getToken());

  const login = async (username: string, password: string) => {
    const userToken = await loginService(username, password);
    setToken(userToken);
  };

  const logout = () => {
    logoutService();
    setToken(null);
  };

  return (
    <AuthContext.Provider value={{ token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
