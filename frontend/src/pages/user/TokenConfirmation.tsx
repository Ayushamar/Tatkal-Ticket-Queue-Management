import React from "react";
import { useParams, Link } from "react-router-dom";
import {
  Download,
  Calendar,
  MapPin,
  Train,
  Users,
  Clock,
  CheckCircle,
} from "lucide-react";
import { useUser } from "../../contexts/UserContext";
import { format } from "date-fns";
import toast from "react-hot-toast";
import { apiClient } from "../../utils/api";

export default function TokenConfirmation() {
  const { tokenId } = useParams();
  const { userData } = useUser();

  const downloadPDF = async () => {
    try {
      if (!tokenId) {
        toast.error("Token ID is missing.");
        return;
      }
      const response = await apiClient.downloadTokenPdf(Number(tokenId));
      // Create a blob and trigger download
      const url = window.URL.createObjectURL(
        new Blob([response], { type: "application/pdf" })
      );
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `tatkal-token-${tokenId}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.parentNode?.removeChild(link);
      toast.success("Token PDF downloaded successfully!");
    } catch (err) {
      toast.error("Failed to download PDF. Please try again.");
    }
  };

  if (!userData) {
    return (
      <div className="max-w-md mx-auto bg-white rounded-xl shadow-lg p-6 text-center">
        <p className="text-gray-600 mb-4">No booking data found</p>
        <Link to="/" className="text-blue-600 hover:text-blue-800">
          Go back to home
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto bg-white rounded-xl shadow-lg overflow-hidden">
      <div className="bg-gradient-to-r from-green-500 to-green-600 px-6 py-8 text-white text-center">
        <CheckCircle className="h-16 w-16 mx-auto mb-4" />
        <h1 className="text-3xl font-bold mb-2">Registration Successful!</h1>
        <p className="text-green-100">Your token has been generated</p>
      </div>

      <div className="p-6">
        <div className="bg-red-50 border-2 border-red-200 rounded-lg p-6 mb-6 text-center">
          <h2 className="text-2xl font-bold text-red-600 mb-2">TOKEN NUMBER</h2>
          <div className="text-4xl font-black text-red-800">{tokenId}</div>
          <div className="mt-4 flex flex-col sm:flex-row justify-center gap-4">
            <div className="bg-blue-50 border border-blue-200 rounded-lg px-4 py-2 inline-block">
              <span className="font-semibold text-blue-700">Counter:</span>{" "}
              <span className="font-bold">{userData.counterNo ?? "N/A"}</span>
            </div>
            <div className="bg-green-50 border border-green-200 rounded-lg px-4 py-2 inline-block">
              <span className="font-semibold text-green-700">
                Position at Counter:
              </span>{" "}
              <span className="font-bold">
                {userData.counterPosition ?? "N/A"}
              </span>
            </div>
          </div>
        </div>

        <div className="space-y-4 mb-6">
          <div className="flex items-center space-x-3 p-4 bg-gray-50 rounded-lg">
            <Calendar className="h-5 w-5 text-blue-600" />
            <div>
              <p className="text-sm text-gray-600">Journey Date</p>
              <p className="font-semibold">
                {userData.journeyDate
                  ? format(new Date(userData.journeyDate), "dd MMMM yyyy")
                  : "N/A"}
              </p>
            </div>
          </div>

          <div className="flex items-center space-x-3 p-4 bg-gray-50 rounded-lg">
            <MapPin className="h-5 w-5 text-blue-600" />
            <div>
              <p className="text-sm text-gray-600">Route</p>
              <p className="font-semibold">
                {userData.fromStation} → {userData.toStation}
              </p>
            </div>
          </div>

          <div className="flex items-center space-x-3 p-4 bg-gray-50 rounded-lg">
            <Train className="h-5 w-5 text-blue-600" />
            <div>
              <p className="text-sm text-gray-600">Train</p>
              <p className="font-semibold">{userData.trainNumber}</p>
            </div>
          </div>

          <div className="flex items-center space-x-3 p-4 bg-gray-50 rounded-lg">
            <Users className="h-5 w-5 text-blue-600" />
            <div>
              <p className="text-sm text-gray-600">Total Passengers</p>
              <p className="font-semibold">
                {userData.coPassengers.length + 1}
              </p>
            </div>
          </div>
        </div>

        <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-6">
          <div className="flex items-center space-x-2 mb-2">
            <Clock className="h-5 w-5 text-yellow-600" />
            <h3 className="font-semibold text-yellow-800">
              Important Instructions
            </h3>
          </div>
          <ul className="text-sm text-yellow-700 space-y-1">
            <li>
              • Please arrive at the station 30 minutes before train departure
            </li>
            <li>
              • Carry this token and original Aadhaar cards for all passengers
            </li>
            <li>
              • Report to the designated counter for document verification
            </li>
            <li>• This token is valid only for the specified date and train</li>
          </ul>
        </div>

        <div className="flex flex-col sm:flex-row gap-4">
          <button
            onClick={downloadPDF}
            className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition-colors flex items-center justify-center space-x-2"
          >
            <Download className="h-5 w-5" />
            <span>Download Token PDF</span>
          </button>

          <Link
            to="/"
            className="flex-1 bg-gray-600 hover:bg-gray-700 text-white font-semibold py-3 px-6 rounded-lg transition-colors flex items-center justify-center space-x-2"
          >
            <span>Book Another Token</span>
          </Link>
        </div>
      </div>
    </div>
  );
}
