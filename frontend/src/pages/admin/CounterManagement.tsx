import React, { useState, useEffect } from "react";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "../../components/ui/card";
import { Button } from "../../components/ui/button";
import { Input } from "../../components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../../components/ui/select";
import {
  Plus,
  Edit,
  Trash2,
  UserPlus,
  Users,
  Activity,
  AlertCircle,
  CheckCircle,
  Clock,
  Wrench,
} from "lucide-react";
import { apiClient } from "../../utils/api";
import toast from "react-hot-toast";

interface Counter {
  counterId: number;
  counterName: string;
  counterNumber: number;
  status: string;
  assignedStaffId: string | null;
  assignedStaffName: string | null;
  currentQueuePosition: number;
  totalServedToday: number;
  lastActivity: string;
  createdAt: string;
  updatedAt: string;
}

interface Staff {
  staffId: string;
  name: string;
  role: string;
  status: string;
}

export default function CounterManagement() {
  const [counters, setCounters] = useState<Counter[]>([]);
  const [staff, setStaff] = useState<Staff[]>([]);
  const [loading, setLoading] = useState(true);
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showAssignModal, setShowAssignModal] = useState(false);
  const [selectedCounter, setSelectedCounter] = useState<Counter | null>(null);
  const [formData, setFormData] = useState({
    counterName: "",
    counterNumber: "",
    status: "ACTIVE",
  });

  const fetchData = async () => {
    try {
      const [countersData, staffData] = await Promise.all([
        apiClient.getCounters(),
        apiClient.getActiveStaff(),
      ]);
      setCounters(countersData);
      setStaff(staffData);
    } catch (error) {
      console.error("Error fetching data:", error);
      toast.error("Failed to load data");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleAddCounter = async () => {
    try {
      await apiClient.createCounter({
        ...formData,
        counterNumber: parseInt(formData.counterNumber),
      });
      toast.success("Counter created successfully");
      setShowAddModal(false);
      setFormData({ counterName: "", counterNumber: "", status: "ACTIVE" });
      fetchData();
    } catch (error) {
      console.error("Error creating counter:", error);
      toast.error("Failed to create counter");
    }
  };

  const handleEditCounter = async () => {
    if (!selectedCounter) return;

    try {
      await apiClient.updateCounter(selectedCounter.counterId, {
        ...formData,
        counterNumber: parseInt(formData.counterNumber),
      });
      toast.success("Counter updated successfully");
      setShowEditModal(false);
      setSelectedCounter(null);
      setFormData({ counterName: "", counterNumber: "", status: "ACTIVE" });
      fetchData();
    } catch (error) {
      console.error("Error updating counter:", error);
      toast.error("Failed to update counter");
    }
  };

  const handleDeleteCounter = async (counterId: number) => {
    if (!confirm("Are you sure you want to delete this counter?")) return;

    try {
      await apiClient.deleteCounter(counterId);
      toast.success("Counter deleted successfully");
      fetchData();
    } catch (error) {
      console.error("Error deleting counter:", error);
      toast.error("Failed to delete counter");
    }
  };

  const handleAssignStaff = async (staffId: string) => {
    if (!selectedCounter) return;

    try {
      await apiClient.assignStaffToCounter(selectedCounter.counterId, staffId);
      toast.success("Staff assigned successfully");
      setShowAssignModal(false);
      setSelectedCounter(null);
      fetchData();
    } catch (error) {
      console.error("Error assigning staff:", error);
      toast.error("Failed to assign staff");
    }
  };

  const openEditModal = (counter: Counter) => {
    setSelectedCounter(counter);
    setFormData({
      counterName: counter.counterName,
      counterNumber: counter.counterNumber.toString(),
      status: counter.status,
    });
    setShowEditModal(true);
  };

  const openAssignModal = (counter: Counter) => {
    setSelectedCounter(counter);
    setShowAssignModal(true);
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case "ACTIVE":
        return <CheckCircle className="h-4 w-4 text-green-600" />;
      case "BREAK":
        return <Clock className="h-4 w-4 text-yellow-600" />;
      case "MAINTENANCE":
        return <Wrench className="h-4 w-4 text-red-600" />;
      default:
        return <AlertCircle className="h-4 w-4 text-gray-600" />;
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "ACTIVE":
        return "text-green-600 bg-green-100";
      case "BREAK":
        return "text-yellow-600 bg-yellow-100";
      case "MAINTENANCE":
        return "text-red-600 bg-red-100";
      default:
        return "text-gray-600 bg-gray-100";
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">
            Counter Management
          </h1>
          <p className="text-gray-600 mt-1">
            Manage counters and staff assignments
          </p>
        </div>
        <Button onClick={() => setShowAddModal(true)}>
          <Plus className="h-4 w-4 mr-2" />
          Add Counter
        </Button>
      </div>

      {/* Counters Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {counters.map((counter) => (
          <Card key={counter.counterId}>
            <CardHeader>
              <div className="flex items-center justify-between">
                <CardTitle className="flex items-center gap-2">
                  {getStatusIcon(counter.status)}
                  {counter.counterName}
                </CardTitle>
                <span
                  className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(
                    counter.status
                  )}`}
                >
                  {counter.status}
                </span>
              </div>
              <CardDescription>
                Counter #{counter.counterNumber}
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Queue Position:</span>
                  <span className="font-medium">
                    {counter.currentQueuePosition}
                  </span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Served Today:</span>
                  <span className="font-medium">
                    {counter.totalServedToday}
                  </span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Assigned Staff:</span>
                  <span className="font-medium">
                    {counter.assignedStaffName || "Unassigned"}
                  </span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Last Activity:</span>
                  <span className="font-medium">
                    {new Date(counter.lastActivity).toLocaleTimeString()}
                  </span>
                </div>
              </div>

              <div className="flex gap-2">
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => openEditModal(counter)}
                  className="flex-1"
                >
                  <Edit className="h-4 w-4 mr-1" />
                  Edit
                </Button>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => openAssignModal(counter)}
                  className="flex-1"
                >
                  <UserPlus className="h-4 w-4 mr-1" />
                  Assign
                </Button>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => handleDeleteCounter(counter.counterId)}
                  className="text-red-600 hover:text-red-700"
                >
                  <Trash2 className="h-4 w-4" />
                </Button>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Add Counter Modal */}
      {showAddModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <Card className="w-full max-w-md mx-4">
            <CardHeader>
              <CardTitle>Add New Counter</CardTitle>
              <CardDescription>
                Create a new counter for queue management
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div>
                <label className="text-sm font-medium">Counter Name</label>
                <Input
                  value={formData.counterName}
                  onChange={(e) =>
                    setFormData({ ...formData, counterName: e.target.value })
                  }
                  placeholder="e.g., Counter 1"
                />
              </div>
              <div>
                <label className="text-sm font-medium">Counter Number</label>
                <Input
                  type="number"
                  value={formData.counterNumber}
                  onChange={(e) =>
                    setFormData({ ...formData, counterNumber: e.target.value })
                  }
                  placeholder="1"
                />
              </div>
              <div>
                <label className="text-sm font-medium">Status</label>
                <Select
                  value={formData.status}
                  onValueChange={(value) =>
                    setFormData({ ...formData, status: value })
                  }
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="ACTIVE">Active</SelectItem>
                    <SelectItem value="INACTIVE">Inactive</SelectItem>
                    <SelectItem value="MAINTENANCE">Maintenance</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div className="flex gap-2">
                <Button onClick={handleAddCounter} className="flex-1">
                  Add Counter
                </Button>
                <Button
                  variant="outline"
                  onClick={() => setShowAddModal(false)}
                  className="flex-1"
                >
                  Cancel
                </Button>
              </div>
            </CardContent>
          </Card>
        </div>
      )}

      {/* Edit Counter Modal */}
      {showEditModal && selectedCounter && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <Card className="w-full max-w-md mx-4">
            <CardHeader>
              <CardTitle>Edit Counter</CardTitle>
              <CardDescription>Update counter information</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div>
                <label className="text-sm font-medium">Counter Name</label>
                <Input
                  value={formData.counterName}
                  onChange={(e) =>
                    setFormData({ ...formData, counterName: e.target.value })
                  }
                  placeholder="e.g., Counter 1"
                />
              </div>
              <div>
                <label className="text-sm font-medium">Counter Number</label>
                <Input
                  type="number"
                  value={formData.counterNumber}
                  onChange={(e) =>
                    setFormData({ ...formData, counterNumber: e.target.value })
                  }
                  placeholder="1"
                />
              </div>
              <div>
                <label className="text-sm font-medium">Status</label>
                <Select
                  value={formData.status}
                  onValueChange={(value) =>
                    setFormData({ ...formData, status: value })
                  }
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="ACTIVE">Active</SelectItem>
                    <SelectItem value="INACTIVE">Inactive</SelectItem>
                    <SelectItem value="BREAK">Break</SelectItem>
                    <SelectItem value="MAINTENANCE">Maintenance</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div className="flex gap-2">
                <Button onClick={handleEditCounter} className="flex-1">
                  Update Counter
                </Button>
                <Button
                  variant="outline"
                  onClick={() => setShowEditModal(false)}
                  className="flex-1"
                >
                  Cancel
                </Button>
              </div>
            </CardContent>
          </Card>
        </div>
      )}

      {/* Assign Staff Modal */}
      {showAssignModal && selectedCounter && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <Card className="w-full max-w-md mx-4">
            <CardHeader>
              <CardTitle>Assign Staff to Counter</CardTitle>
              <CardDescription>
                Assign staff member to {selectedCounter.counterName}
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                {staff.map((staffMember) => (
                  <div
                    key={staffMember.staffId}
                    className="flex items-center justify-between p-3 border rounded-lg hover:bg-gray-50 cursor-pointer"
                    onClick={() => handleAssignStaff(staffMember.staffId)}
                  >
                    <div>
                      <p className="font-medium">{staffMember.name}</p>
                      <p className="text-sm text-gray-600">
                        {staffMember.role}
                      </p>
                    </div>
                    <span
                      className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(
                        staffMember.status
                      )}`}
                    >
                      {staffMember.status}
                    </span>
                  </div>
                ))}
              </div>
              <Button
                variant="outline"
                onClick={() => setShowAssignModal(false)}
                className="w-full"
              >
                Cancel
              </Button>
            </CardContent>
          </Card>
        </div>
      )}
    </div>
  );
}
