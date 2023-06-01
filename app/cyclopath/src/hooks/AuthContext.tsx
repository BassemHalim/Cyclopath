import { createContext, useContext, useMemo, useState } from "react";

export type User = {
  token: string | null;
  setToken: (t: string | null) => void;
};

const UserContext = createContext<User>({
  token: null,
  setToken: () => {},
});

export const useAuth = () => {
  return useContext(UserContext);
};

interface AuthProviderProps {
  children: React.ReactNode;
}

export const UserProvider = ({ children }: AuthProviderProps) => {
  const [token, setToken] = useState<string | null>(null);
  return (
    <UserContext.Provider value={{ token, setToken }}>
      {children}
    </UserContext.Provider>
  );
};

//   const logout = () => {
//     ["token", "name"].forEach((obj) => removeCookie(obj)); // remove data save in cookies
//     navigate("/login");
//   };

//   const value = useMemo(
//     () => ({
//       cookies,
//       login,
//       logout,
//     }),
//     [cookies]
//   );

//   return <UserContext.Provider value={value}>{children}</UserContext.Provider>;
// };
