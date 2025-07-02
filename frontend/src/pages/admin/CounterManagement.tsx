import React, { useState, useEffect } from "react";
import { Users, RotateCcw, Plus, Minus } from "lucide-react";
import toast from "react-hot-toast";
import { apiClient } from "../../utils/api";

interface Counter {
  id: string;
  number: number;
  assignedPassengers: number;
  status: "Active" | "Inactive";
  operator?: string;
}

const mockCounters: Counter[] = [
  {
    id: "1",
    number: 1,
    assignedPassengers: 15,
    status: "Active",
    operator: "Raj Kumar",
  },
  {
    id: "2",
    number: 2,
    assignedPassengers: 12,
    status: "Active",
    operator: "Priya Singh",
  },
  {
    id: "3",
    number: 3,
    assignedPassengers: 18,
    status: "Active",
    operator: "Amit Sharma",
  },
  { id: "4", number: 4, assignedPassengers: 0, status: "Inactive" },
  {
    id: "5",
    number: 5,
    assignedPassengers: 22,
    status: "Active",
    operator: "Sunita Devi",
  },
  {
    id: "6",
    number: 6,
    assignedPassengers: 8,
    status: "Active",
    operator: "Ravi Patel",
  },
  { id: "7", number: 7, assignedPassengers: 0, status: "Inactive" },
  {
    id: "8",
    number: 8,
    assignedPassengers: 14,
    status: "Active",
    operator: "Neeta Gupta",
  },
];

export default function CounterManagement() {
  const [counters, setCounters] = useState<Counter[]>([]);
  const [selectedCounters, setSelectedCounters] = useState<string[]>([]);
  const [showReassignModal, setShowReassignModal] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchCounters = async () => {
      setLoading(true);
      try {
        const data = await apiClient.getCounters();
        setCounters(data);
      } catch (err) {
        toast.error("Failed to fetch counters");
      }
      setLoading(false);
    };
    fetchCounters();
  }, []);

  const toggleCounterStatus = (counterId: string) => {
    setCounters(
      counters.map((counter) =>
        counter.id === counterId
          ? {
              ...counter,
              status: counter.status === "Active" ? "Inactive" : "Active",
              assignedPassengers:
                counter.status === "Active" ? 0 : counter.assignedPassengers,
            }
          : counter
      )
    );

    const counter = counters.find((c) => c.id === counterId);
    toast.success(
      `Counter ${counter?.number} ${
        counter?.status === "Active" ? "deactivated" : "activated"
      }`
    );
  };

  const redistributePassengers = () => {
    // Simple redistribution logic
    const activeCounters = counters.filter((c) => c.status === "Active");
    const totalPassengers = counters.reduce(
      (sum, c) => sum + c.assignedPassengers,
      0
    );
    const passengersPerCounter = Math.floor(
      totalPassengers / activeCounters.length
    );

    setCounters(
      counters.map((counter) =>
        counter.status === "Active"
          ? { ...counter, assignedPassengers: passengersPerCounter }
          : counter
      )
    );

    toast.success("Passengers redistributed evenly across active counters");
  };

  const getCounterStatusColor = (status: string) => {
    return status === "Active"
      ? "bg-green-100 text-green-800"
      : "bg-gray-100 text-gray-800";
  };

  const getLoadColor = (passengers: number) => {
    if (passengers === 0) return "bg-gray-100 text-gray-800";
    if (passengers <= 10) return "bg-green-100 text-green-800";
    if (passengers <= 15) return "bg-yellow-100 text-yellow-800";
    return "bg-red-100 text-red-800";
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold text-gray-900">Counter Management</h1>
        <div className="flex space-x-3">
          <button
            onClick={redistributePassengers}
            className="flex items-center space-x-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-colors"
          >
            <RotateCcw className="h-4 w-4" />
            <span>Redistribute</span>
          </button>
          <button
            onClick={() => setShowReassignModal(true)}
            className="flex items-center space-x-2 px-4 py-2 bg-green-600 hover:bg-green-700 text-white rounded-lg transition-colors"
          >
            <Users className="h-4 w-4" />
            <span>Auto Assign</span>
          </button>
        </div>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="bg-white rounded-xl shadow-lg p-6">
          <div className="flex items-center space-x-3">
            <div className="p-3 bg-blue-100 rounded-lg">
              <Users className="h-6 w-6 text-blue-600" />
            </div>
            <div>
              <p className="text-sm text-gray-600">Total Counters</p>
              <p className="text-2xl font-bold text-gray-900">
                {counters.length}
              </p>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-xl shadow-lg p-6">
          <div className="flex items-center space-x-3">
            <div className="p-3 bg-green-100 rounded-lg">
              <Plus className="h-6 w-6 text-green-600" />
            </div>
            <div>
              <p className="text-sm text-gray-600">Active Counters</p>
              <p className="text-2xl font-bold text-gray-900">
                {counters.filter((c) => c.status === "Active").length}
              </p>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-xl shadow-lg p-6">
          <div className="flex items-center space-x-3">
            <div className="p-3 bg-yellow-100 rounded-lg">
              <Minus className="h-6 w-6 text-yellow-600" />
            </div>
            <div>
              <p className="text-sm text-gray-600">Total Passengers</p>
              <p className="text-2xl font-bold text-gray-900">
                {counters.reduce((sum, c) => sum + c.assignedPassengers, 0)}
              </p>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-xl shadow-lg p-6">
          <div className="flex items-center space-x-3">
            <div className="p-3 bg-purple-100 rounded-lg">
              <RotateCcw className="h-6 w-6 text-purple-600" />
            </div>
            <div>
              <p className="text-sm text-gray-600">Avg. Load</p>
              <p className="text-2xl font-bold text-gray-900">
                {Math.round(
                  counters.reduce((sum, c) => sum + c.assignedPassengers, 0) /
                    counters.filter((c) => c.status === "Active").length
                ) || 0}
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Counter Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        {counters.map((counter) => (
          <div
            key={counter.id}
            className="bg-white rounded-xl shadow-lg p-6 hover:shadow-xl transition-shadow"
          >
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-xl font-bold text-gray-900">
                Counter {counter.number}
              </h3>
              <span
                className={`px-2 py-1 text-xs font-semibold rounded-full ${getCounterStatusColor(
                  counter.status
                )}`}
              >
                {counter.status}
              </span>
            </div>

            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600">
                  Assigned Passengers
                </span>
                <span
                  className={`px-2 py-1 text-xs font-semibold rounded-full ${getLoadColor(
                    counter.assignedPassengers
                  )}`}
                >
                  {counter.assignedPassengers}
                </span>
              </div>

              {counter.operator && (
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-600">Operator</span>
                  <span className="text-sm font-medium text-gray-900">
                    {counter.operator}
                  </span>
                </div>
              )}

              <div className="pt-2">
                <button
                  onClick={() => toggleCounterStatus(counter.id)}
                  className={`w-full py-2 px-4 rounded-lg font-medium transition-colors ${
                    counter.status === "Active"
                      ? "bg-red-100 text-red-700 hover:bg-red-200"
                      : "bg-green-100 text-green-700 hover:bg-green-200"
                  }`}
                >
                  {counter.status === "Active" ? "Deactivate" : "Activate"}
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Load Distribution Chart */}
      <div className="bg-white rounded-xl shadow-lg p-6">
        <h2 className="text-xl font-semibold text-gray-900 mb-4">
          Load Distribution
        </h2>
        <div className="space-y-3">
          {counters
            .filter((c) => c.status === "Active")
            .map((counter) => (
              <div key={counter.id} className="flex items-center space-x-4">
                <div className="w-20 text-sm font-medium text-gray-700">
                  Counter {counter.number}
                </div>
                <div className="flex-1 bg-gray-200 rounded-full h-4 relative">
                  <div
                    className={`h-4 rounded-full ${
                      counter.assignedPassengers <= 10
                        ? "bg-green-500"
                        : counter.assignedPassengers <= 15
                        ? "bg-yellow-500"
                        : "bg-red-500"
                    }`}
                    style={{
                      width: `${Math.min(
                        (counter.assignedPassengers / 25) * 100,
                        100
                      )}%`,
                    }}
                  ></div>
                </div>
                <div className="w-12 text-sm font-medium text-gray-700 text-right">
                  {counter.assignedPassengers}
                </div>
              </div>
            ))}
        </div>
      </div>

      {/* Auto Assign Modal */}
      {showReassignModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl p-6 max-w-md w-full mx-4">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">
              Auto Assign Passengers
            </h3>
            <p className="text-gray-600 mb-6">
              This will automatically assign new passengers to counters based on
              current load and availability.
            </p>
            <div className="flex space-x-3">
              <button
                onClick={() => {
                  toast.success("Auto assignment enabled for new passengers");
                  setShowReassignModal(false);
                }}
                className="flex-1 bg-green-600 hover:bg-green-700 text-white py-2 px-4 rounded-lg transition-colors"
              >
                Enable Auto Assign
              </button>
              <button
                onClick={() => setShowReassignModal(false)}
                className="flex-1 bg-gray-600 hover:bg-gray-700 text-white py-2 px-4 rounded-lg transition-colors"
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
