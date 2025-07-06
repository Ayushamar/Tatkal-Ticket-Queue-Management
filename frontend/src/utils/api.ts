// Mock API utilities for demo purposes
// In production, replace with actual API calls

import axios from "axios";

export const API_BASE_URL = "http://localhost:8080/api";

// Add axios interceptor for admin token
axios.interceptors.request.use((config) => {
  const token = localStorage.getItem("admin_token");
  if (token && config.url?.includes("/api/admin")) {
    config.headers["Authorization"] = `Bearer ${token}`;
  }
  return config;
});

// Mock OTP storage (DO NOT USE IN PRODUCTION)
const mockOtpStore: { [key: string]: string } = {};

export const apiClient = {
  // OTP endpoints
  async sendOtp(aadhaarNo: string) {
    const response = await axios.post(`${API_BASE_URL}/otp/send`, {
      aadhaarNo,
    });
    return response.data;
  },

  async verifyOtp(aadhaarNo: string, otp: string) {
    const response = await axios.post(`${API_BASE_URL}/otp/verify`, {
      aadhaarNo,
      otp,
    });
    return response.data;
  },

  // Family members (co-passenger options)
  async getFamilyMembers(aadhaarNo: string) {
    const response = await axios.get(`${API_BASE_URL}/family/${aadhaarNo}`);
    return response.data;
  },

  // Station list
  async getStations() {
    const response = await axios.get(`${API_BASE_URL}/stations`);
    return response.data;
  },

  // Journey creation
  async createJourney(journeyData: {
    aadhaarNo: string;
    station: string;
    journeyDate: string;
    trainNo?: string;
    coPassengers: string[];
  }) {
    const response = await axios.post(`${API_BASE_URL}/journey`, journeyData);
    return response.data;
  },

  // Download PDF token
  async downloadTokenPdf(tokenNo: number) {
    const response = await axios.get(`${API_BASE_URL}/journey/${tokenNo}/pdf`, {
      responseType: "blob",
    });
    return response.data;
  },

  // User endpoints
  async register(userData: any) {
    const response = await axios.post(`${API_BASE_URL}/register`, userData);
    return response.data;
  },

  async getOTP(identifier: string) {
    const response = await axios.post(`${API_BASE_URL}/get-otp`, {
      identifier,
    });
    return response.data;
  },

  async verifyOTP(identifier: string, otp: string) {
    const response = await axios.post(`${API_BASE_URL}/verify-otp`, {
      identifier,
      otp,
    });
    return response.data;
  },

  // Admin endpoints
  async adminLogin(credentials: { username: string; password: string }) {
    const response = await axios.post(
      `${API_BASE_URL}/admin/login`,
      credentials
    );
    return response.data;
  },

  async getPassengers(params?: any) {
    const response = await axios.get(`${API_BASE_URL}/admin/passengers`, {
      params,
    });
    return response.data;
  },

  async verifyPassenger(passengerId: string) {
    const response = await axios.post(`${API_BASE_URL}/admin/verify`, {
      passengerId,
    });
    return response.data;
  },

  async assignCounter(passengerId: string, counterId: string) {
    const response = await axios.post(`${API_BASE_URL}/admin/assign-counter`, {
      passengerId,
      counterId,
    });
    return response.data;
  },

  async exportLogs() {
    const response = await axios.get(`${API_BASE_URL}/admin/logs/export`, {
      responseType: "blob",
    });
    return response.data;
  },

  async getCounters() {
    const response = await axios.get(`${API_BASE_URL}/admin/counters`);
    return response.data;
  },

  async getPassengerDetails(aadhaar: string) {
    const response = await axios.post(`${API_BASE_URL}/passenger/details`, {
      aadhaar,
    });
    return response.data;
  },

  async getPassengersByCounter() {
    const response = await axios.get(
      `${API_BASE_URL}/admin/passengers-by-counter`
    );
    return response.data;
  },

  async getActiveCounters() {
    const response = await axios.get(`${API_BASE_URL}/admin/counters/active`);
    return response.data;
  },

  async createCounter(counterData: any) {
    const response = await axios.post(
      `${API_BASE_URL}/admin/counters`,
      counterData
    );
    return response.data;
  },

  async updateCounter(counterId: number, counterData: any) {
    const response = await axios.put(
      `${API_BASE_URL}/admin/counters/${counterId}`,
      counterData
    );
    return response.data;
  },

  async deleteCounter(counterId: number) {
    const response = await axios.delete(
      `${API_BASE_URL}/admin/counters/${counterId}`
    );
    return response.data;
  },

  async assignStaffToCounter(counterId: number, staffId: string) {
    const response = await axios.put(
      `${API_BASE_URL}/admin/counters/${counterId}/assign-staff?staffId=${staffId}`
    );
    return response.data;
  },

  // Staff Management
  async getStaff() {
    const response = await axios.get(`${API_BASE_URL}/admin/staff`);
    return response.data;
  },

  async getActiveStaff() {
    const response = await axios.get(`${API_BASE_URL}/admin/staff/active`);
    return response.data;
  },

  async createStaff(staffData: any) {
    const response = await axios.post(`${API_BASE_URL}/admin/staff`, staffData);
    return response.data;
  },

  async updateStaff(staffId: string, staffData: any) {
    const response = await axios.put(
      `${API_BASE_URL}/admin/staff/${staffId}`,
      staffData
    );
    return response.data;
  },

  async deleteStaff(staffId: string) {
    const response = await axios.delete(
      `${API_BASE_URL}/admin/staff/${staffId}`
    );
    return response.data;
  },

  // Token Rules
  async getTokenRules() {
    const response = await axios.get(`${API_BASE_URL}/admin/rules`);
    return response.data;
  },

  async getActiveTokenRules() {
    const response = await axios.get(`${API_BASE_URL}/admin/rules/active`);
    return response.data;
  },

  async createTokenRule(ruleData: any) {
    const response = await axios.post(`${API_BASE_URL}/admin/rules`, ruleData);
    return response.data;
  },

  async updateTokenRule(ruleId: number, ruleData: any) {
    const response = await axios.put(
      `${API_BASE_URL}/admin/rules/${ruleId}`,
      ruleData
    );
    return response.data;
  },

  async deleteTokenRule(ruleId: number) {
    const response = await axios.delete(
      `${API_BASE_URL}/admin/rules/${ruleId}`
    );
    return response.data;
  },

  // Reports
  async getDashboardStats() {
    const response = await axios.get(`${API_BASE_URL}/admin/reports/dashboard`);
    return response.data;
  },

  async getDailySummary(date?: string) {
    const params = date ? { date } : {};
    const response = await axios.get(
      `${API_BASE_URL}/admin/reports/daily-summary`,
      { params }
    );
    return response.data;
  },

  async getCounterPerformance(date?: string) {
    const params = date ? { date } : {};
    const response = await axios.get(
      `${API_BASE_URL}/admin/reports/counter-performance`,
      { params }
    );
    return response.data;
  },

  async getQueueStatus() {
    const response = await axios.get(
      `${API_BASE_URL}/admin/reports/queue-status`
    );
    return response.data;
  },

  async getTrainsByStations(from: string, to: string) {
    const response = await axios.get(`${API_BASE_URL}/trains`, {
      params: { from, to },
    });
    return response.data;
  },

  async getTrainRoute(trainNumber: string) {
    const response = await axios.get(`${API_BASE_URL}/train/${trainNumber}`);
    return response.data;
  },
};
