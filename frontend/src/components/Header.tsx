import React from 'react';
import { useLocation, Link } from 'react-router-dom';
import { Train, LogOut, User, Shield } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';

export default function Header() {
  const location = useLocation();
  const { isAuthenticated, logout, user } = useAuth();
  const isAdminRoute = location.pathname.startsWith('/admin');

  return (
    <header className="bg-gradient-to-r from-blue-600 to-blue-800 text-white shadow-lg">
      <div className="container mx-auto px-4 py-4">
        <div className="flex items-center justify-between">
          <Link to="/" className="flex items-center space-x-3 hover:opacity-80 transition-opacity">
            <Train className="h-8 w-8" />
            <div>
              <h1 className="text-xl font-bold">Tatkal Queue</h1>
              <p className="text-blue-100 text-sm">Management System</p>
            </div>
          </Link>

          <nav className="flex items-center space-x-6">
            {isAdminRoute && isAuthenticated ? (
              <>
                <Link
                  to="/admin"
                  className={`flex items-center space-x-2 px-3 py-2 rounded-lg transition-colors ${
                    location.pathname === '/admin' 
                      ? 'bg-blue-700 text-white' 
                      : 'hover:bg-blue-700'
                  }`}
                >
                  <Shield className="h-4 w-4" />
                  <span>Dashboard</span>
                </Link>
                <Link
                  to="/admin/passengers"
                  className={`flex items-center space-x-2 px-3 py-2 rounded-lg transition-colors ${
                    location.pathname === '/admin/passengers' 
                      ? 'bg-blue-700 text-white' 
                      : 'hover:bg-blue-700'
                  }`}
                >
                  <User className="h-4 w-4" />
                  <span>Passengers</span>
                </Link>
                <div className="flex items-center space-x-3">
                  <span className="text-sm">Welcome, {user?.username}</span>
                  <button
                    onClick={logout}
                    className="flex items-center space-x-2 px-3 py-2 bg-red-600 hover:bg-red-700 rounded-lg transition-colors"
                  >
                    <LogOut className="h-4 w-4" />
                    <span>Logout</span>
                  </button>
                </div>
              </>
            ) : (
              <Link
                to="/admin/login"
                className="flex items-center space-x-2 px-4 py-2 bg-orange-500 hover:bg-orange-600 rounded-lg transition-colors"
              >
                <Shield className="h-4 w-4" />
                <span>Admin Login</span>
              </Link>
            )}
          </nav>
        </div>
      </div>
    </header>
  );
}