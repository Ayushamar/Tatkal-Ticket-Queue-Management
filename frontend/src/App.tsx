import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './contexts/AuthContext';
import { UserProvider } from './contexts/UserContext';

// User Pages
import QRScanner from './pages/user/QRScanner';
import Registration from './pages/user/Registration';
import TokenConfirmation from './pages/user/TokenConfirmation';

// Admin Pages
import AdminLogin from './pages/admin/AdminLogin';
import AdminDashboard from './pages/admin/AdminDashboard';
import PassengerList from './pages/admin/PassengerList';
import CounterManagement from './pages/admin/CounterManagement';
import LogsExport from './pages/admin/LogsExport';

// Components
import ProtectedRoute from './components/ProtectedRoute';
import Header from './components/Header';

function App() {
  return (
    <AuthProvider>
      <UserProvider>
        <Router>
          <div className="min-h-screen bg-gray-50">
            <Header />
            <main className="container mx-auto px-4 py-6">
              <Routes>
                {/* User Routes */}
                <Route path="/" element={<QRScanner />} />
                <Route path="/register" element={<Registration />} />
                <Route path="/token/:tokenId" element={<TokenConfirmation />} />
                
                {/* Admin Routes */}
                <Route path="/admin/login" element={<AdminLogin />} />
                <Route path="/admin" element={
                  <ProtectedRoute>
                    <AdminDashboard />
                  </ProtectedRoute>
                } />
                <Route path="/admin/passengers" element={
                  <ProtectedRoute>
                    <PassengerList />
                  </ProtectedRoute>
                } />
                <Route path="/admin/counters" element={
                  <ProtectedRoute>
                    <CounterManagement />
                  </ProtectedRoute>
                } />
                <Route path="/admin/logs" element={
                  <ProtectedRoute>
                    <LogsExport />
                  </ProtectedRoute>
                } />
                
                {/* Redirects */}
                <Route path="/admin/*" element={<Navigate to="/admin" replace />} />
              </Routes>
            </main>
            <Toaster position="top-right" />
          </div>
        </Router>
      </UserProvider>
    </AuthProvider>
  );
}

export default App;