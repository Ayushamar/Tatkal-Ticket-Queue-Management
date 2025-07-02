import React, { createContext, useContext, useState, ReactNode } from "react";

interface CoPassenger {
  id: string;
  name: string;
  relation: string;
  aadhaar: string;
}

interface UserData {
  name: string;
  aadhaar: string;
  journeyDate: string;
  fromStation: string;
  toStation: string;
  trainNumber: string;
  coPassengers: CoPassenger[];
  stationCode?: string;
  otpVerified: boolean;
  tokenNo?: number;
  counterNo?: number;
  counterPosition?: number;
}

interface UserContextType {
  userData: UserData | null;
  setUserData: (data: UserData) => void;
  updateUserData: (data: Partial<UserData>) => void;
  clearUserData: () => void;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

export function UserProvider({ children }: { children: ReactNode }) {
  const [userData, setUserDataState] = useState<UserData | null>(null);

  const setUserData = (data: UserData) => {
    setUserDataState(data);
  };

  const updateUserData = (data: Partial<UserData>) => {
    setUserDataState((prev) => (prev ? { ...prev, ...data } : null));
  };

  const clearUserData = () => {
    setUserDataState(null);
  };

  return (
    <UserContext.Provider
      value={{ userData, setUserData, updateUserData, clearUserData }}
    >
      {children}
    </UserContext.Provider>
  );
}

export function useUser() {
  const context = useContext(UserContext);
  if (context === undefined) {
    throw new Error("useUser must be used within a UserProvider");
  }
  return context;
}
