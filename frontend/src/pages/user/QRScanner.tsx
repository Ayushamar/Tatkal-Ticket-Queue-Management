import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { QrCode, MapPin, ArrowRight } from 'lucide-react';
import { Html5QrcodeScanner } from 'html5-qrcode';
import { useUser } from '../../contexts/UserContext';
import toast from 'react-hot-toast';

export default function QRScanner() {
  const [scannerActive, setScannerActive] = useState(false);
  const [manualCode, setManualCode] = useState('');
  const scannerRef = useRef<Html5QrcodeScanner | null>(null);
  const navigate = useNavigate();
  const { updateUserData } = useUser();

  useEffect(() => {
    return () => {
      if (scannerRef.current) {
        scannerRef.current.clear();
      }
    };
  }, []);

  const startScanner = () => {
    setScannerActive(true);
    const scanner = new Html5QrcodeScanner(
      'qr-reader',
      {
        fps: 10,
        qrbox: { width: 250, height: 250 },
        aspectRatio: 1.0,
      },
      false
    );

    scanner.render(
      (decodedText) => {
        handleScanResult(decodedText);
        scanner.clear();
        setScannerActive(false);
      },
      (error) => {
        console.log('QR scan error:', error);
      }
    );

    scannerRef.current = scanner;
  };

  const handleScanResult = (result: string) => {
    toast.success('QR Code scanned successfully!');
    updateUserData({ stationCode: result });
    navigate('/register');
  };

  const handleManualSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (manualCode.trim()) {
      updateUserData({ stationCode: manualCode.trim() });
      navigate('/register');
    }
  };

  return (
    <div className="max-w-md mx-auto bg-white rounded-xl shadow-lg overflow-hidden">
      <div className="bg-gradient-to-r from-blue-600 to-blue-800 px-6 py-8 text-white text-center">
        <QrCode className="h-16 w-16 mx-auto mb-4" />
        <h1 className="text-2xl font-bold mb-2">Tatkal Queue</h1>
        <p className="text-blue-100">Scan QR Code or Enter Station Code</p>
      </div>

      <div className="p-6 space-y-6">
        {!scannerActive ? (
          <>
            <button
              onClick={startScanner}
              className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-4 px-6 rounded-lg transition-colors flex items-center justify-center space-x-3 shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 transition-all"
            >
              <QrCode className="h-6 w-6" />
              <span>Scan QR Code</span>
            </button>

            <div className="relative">
              <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-gray-300"></div>
              </div>
              <div className="relative flex justify-center text-sm">
                <span className="px-4 bg-white text-gray-500">Or enter manually</span>
              </div>
            </div>

            <form onSubmit={handleManualSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Station Code
                </label>
                <div className="relative">
                  <MapPin className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
                  <input
                    type="text"
                    value={manualCode}
                    onChange={(e) => setManualCode(e.target.value.toUpperCase())}
                    placeholder="Enter station code (e.g., NDLS)"
                    className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    maxLength={10}
                  />
                </div>
              </div>
              <button
                type="submit"
                disabled={!manualCode.trim()}
                className="w-full bg-orange-500 hover:bg-orange-600 disabled:bg-gray-300 disabled:cursor-not-allowed text-white font-semibold py-3 px-6 rounded-lg transition-colors flex items-center justify-center space-x-2"
              >
                <span>Continue</span>
                <ArrowRight className="h-5 w-5" />
              </button>
            </form>
          </>
        ) : (
          <div className="space-y-4">
            <div id="qr-reader" className="w-full"></div>
            <button
              onClick={() => {
                if (scannerRef.current) {
                  scannerRef.current.clear();
                }
                setScannerActive(false);
              }}
              className="w-full bg-gray-500 hover:bg-gray-600 text-white font-semibold py-3 px-6 rounded-lg transition-colors"
            >
              Cancel Scan
            </button>
          </div>
        )}
      </div>
    </div>
  );
}