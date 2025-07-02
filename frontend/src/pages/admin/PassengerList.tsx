import React, { useState, useEffect } from "react";
import toast from "react-hot-toast";
import { apiClient } from "../../utils/api";

interface Passenger {
  tokenNo: number;
  name: string;
  maskedAadhaar: string;
  journeyDate: string;
  trainNo: string;
  station: string;
}

export default function PassengerList() {
  const [groupedPassengers, setGroupedPassengers] = useState<
    Record<string, Passenger[]>
  >({});
  const [selectedPassenger, setSelectedPassenger] = useState<Passenger | null>(
    null
  );
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchPassengers = async () => {
      setLoading(true);
      try {
        const data = await apiClient.getPassengersByCounter();
        setGroupedPassengers(data);
      } catch (err) {
        toast.error("Failed to fetch passengers");
      }
      setLoading(false);
    };
    fetchPassengers();
  }, []);

  const counterNumbers = Object.keys(groupedPassengers).sort(
    (a, b) => Number(a) - Number(b)
  );

  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold text-gray-900">Passenger Management</h1>
      <div className="overflow-x-auto">
        <table className="w-full border">
          <thead>
            <tr>
              {counterNumbers.map((counter) => (
                <th
                  key={counter}
                  className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider border"
                >
                  Counter {counter}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {/* Find the max number of passengers in any counter for row count */}
            {Array.from({
              length: Math.max(
                ...counterNumbers.map((c) => groupedPassengers[c].length),
                0
              ),
            }).map((_, rowIdx) => (
              <tr key={rowIdx}>
                {counterNumbers.map((counter) => {
                  const passenger = groupedPassengers[counter][rowIdx];
                  return (
                    <td
                      key={counter}
                      className="px-6 py-4 text-center border align-top"
                    >
                      {passenger ? (
                        <div>
                          <div className="font-medium">{passenger.name}</div>
                          <button
                            className="mt-2 px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700 text-xs"
                            onClick={() => {
                              setSelectedPassenger(passenger);
                              setShowModal(true);
                            }}
                          >
                            View
                          </button>
                        </div>
                      ) : null}
                    </td>
                  );
                })}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {/* Modal for passenger details */}
      {showModal && selectedPassenger && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl p-6 max-w-md w-full mx-4">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">
              Passenger Details
            </h3>
            <div className="space-y-2 mb-6">
              <p>
                <span className="font-medium">Name:</span>{" "}
                {selectedPassenger.name}
              </p>
              <p>
                <span className="font-medium">Aadhaar:</span>{" "}
                {selectedPassenger.maskedAadhaar}
              </p>
              <p>
                <span className="font-medium">Token No:</span>{" "}
                {selectedPassenger.tokenNo}
              </p>
              <p>
                <span className="font-medium">Journey Date:</span>{" "}
                {selectedPassenger.journeyDate}
              </p>
              <p>
                <span className="font-medium">Train No:</span>{" "}
                {selectedPassenger.trainNo}
              </p>
              <p>
                <span className="font-medium">Station:</span>{" "}
                {selectedPassenger.station}
              </p>
            </div>
            <button
              onClick={() => setShowModal(false)}
              className="w-full bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded-lg transition-colors"
            >
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
