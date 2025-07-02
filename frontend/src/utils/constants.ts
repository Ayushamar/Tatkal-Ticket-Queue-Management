export const ROUTES = {
  HOME: '/',
  REGISTER: '/register',
  TOKEN: '/token/:tokenId',
  ADMIN_LOGIN: '/admin/login',
  ADMIN_DASHBOARD: '/admin',
  ADMIN_PASSENGERS: '/admin/passengers',
  ADMIN_COUNTERS: '/admin/counters',
  ADMIN_LOGS: '/admin/logs'
};

export const STATIONS = [
  'NEW DELHI (NDLS)',
  'MUMBAI CENTRAL (MMCT)',
  'CHENNAI CENTRAL (MAS)',
  'KOLKATA (KOAA)',
  'BANGALORE (SBC)',
  'HYDERABAD (HYB)',
  'PUNE (PUNE)',
  'AHMEDABAD (ADI)',
  'KANPUR (CNB)',
  'NAGPUR (NGP)',
  'JAIPUR (JP)',
  'LUCKNOW (LJN)',
  'PATNA (PNBE)',
  'BHOPAL (BPL)',
  'INDORE (INDB)'
];

export const RELATIONS = [
  'Father',
  'Mother',
  'Spouse',
  'Son',
  'Daughter',
  'Brother',
  'Sister',
  'Friend',
  'Colleague'
];

export const PASSENGER_STATUS = {
  VERIFIED: 'Verified',
  PENDING: 'Pending'
};

export const COUNTER_STATUS = {
  ACTIVE: 'Active',
  INACTIVE: 'Inactive'
};

export const MAX_CO_PASSENGERS = 5;
export const MAX_TOTAL_PASSENGERS = 6;
export const AADHAAR_LENGTH = 12;
export const OTP_LENGTH = 6;