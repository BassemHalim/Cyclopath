import { createContext, useContext, useEffect, useState } from "react";
import { Alert } from "react-native";
import * as SecureStore from "expo-secure-store";
import axios from "axios";
import jwt_decode from "jwt-decode";

const LOGIN_URL = "http://192.168.1.245:8080/auth/authenticate";
const REGISTER_URL = "http://192.168.1.245:8080/auth/register";

export type User = {
  isAuthenticated: boolean;
  token: string;
  login: (username: string, password: string) => Promise<void>;
  register: (username: string, password: string) => Promise<void>;
  logout: () => void;
};

async function saveToken(value) {
  await SecureStore.setItemAsync("cyclopath_token", value);
}

async function getToken(): Promise<string | null> {
  try {
    let token = await SecureStore.getItemAsync("cyclopath_token");
    return token;
  } catch (error) {
    console.log(error);
    return null;
  }
}

const UserContext = createContext<User>({
  isAuthenticated: false,
  token: undefined,
  login: undefined,
  register: undefined,
  logout: undefined,
});

export const useAuth = () => {
  return useContext(UserContext);
};

interface AuthProviderProps {
  children: React.ReactNode;
}

export const UserProvider = ({ children }: AuthProviderProps) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [token, setToken] = useState<string>("");

  useEffect(() => {
    loadLocalStorage();
  }, []);

  const loadLocalStorage = async (): Promise<void> => {
    getToken()
      .then((token) => {
        if (token) {
          const { exp }: any = jwt_decode(token);
          if (Date.now() / 1000 < exp) {
            setToken(token);
            setIsAuthenticated(true);
          }
        }
      })
      .catch((e) => console.log(e));
  };

  const login = async (email: string, password: string) => {
    try {
      const response = await axios.post(LOGIN_URL, {
        email: email,
        password: password,
      });
      if (response.status === 200) {
        const { token, username } = response.data;
        setToken(token);
        setIsAuthenticated(true);
        await saveToken(token);
      } else {
        alert("invalid credentials");
      }
    } catch (error) {
      console.log(error);
      alert("internal error");
    }
  };

  const register = async (email: string, password: string) => {
    try {
      const response = await axios.post(REGISTER_URL, {
        email: email,
        password: password,
      });
      if (response.status === 200) {
        const { token, username } = response.data;
        setToken(token);
        setIsAuthenticated(true);
        await saveToken(token);
      } else {
        alert("invalid credentials");
      }
    } catch (error) {
      console.log(error);
      alert("internal error");
    }
  };

  const logout = () => {
    setToken("");
    saveToken("");
    setIsAuthenticated(false);
  };

  return (
    <UserContext.Provider
      value={{ isAuthenticated, token, login, register, logout }}
    >
      {children}
    </UserContext.Provider>
  );
};
