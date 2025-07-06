import React, { useState, useEffect } from "react";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "../../components/ui/card";
import { Button } from "../../components/ui/button";
import {
  Users,
  Clock,
  Activity,
  TrendingUp,
  AlertCircle,
  CheckCircle,
  UserCheck,
  BarChart3,
} from "lucide-react";
import { apiClient } from "../../utils/api";
import toast from "react-hot-toast";

interface DashboardStats {
  todaySummary: {
    totalTokensIssued: number;
    totalPassengersServed: number;
    averageWaitTime: number;
    peakHour: string;
    peakHourTokens: number;
  };
  queueStatus: {
    totalInQueue: number;
    estimatedWaitTime: number;
  };
  activeCounters: number;
  activeStaff: number;
  totalServedToday: number;
}

interface QueueStatus {
  totalInQueue: number;
  queueByCounter: Record<string, number>;
  counterStatus: Record<string, string>;
  assignedStaff: Record<string, string>;
  estimatedWaitTime: number;
}

export default function Dashboard() {
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [queueStatus, setQueueStatus] = useState<QueueStatus | null>(null);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const fetchDashboardData = async () => {
    try {
      const [dashboardStats, queueData] = await Promise.all([
        apiClient.getDashboardStats(),
        apiClient.getQueueStatus(),
      ]);

      setStats(dashboardStats);
      setQueueStatus(queueData);
    } catch (error) {
      console.error("Error fetching dashboard data:", error);
      toast.error("Failed to load dashboard data");
    } finally {
      setLoading(false);
    }
  };

  const refreshData = async () => {
    setRefreshing(true);
    await fetchDashboardData();
    setRefreshing(false);
    toast.success("Dashboard refreshed");
  };

  useEffect(() => {
    fetchDashboardData();

    // Auto-refresh every 30 seconds
    const interval = setInterval(fetchDashboardData, 30000);
    return () => clearInterval(interval);
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  if (!stats || !queueStatus) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <AlertCircle className="h-12 w-12 text-red-500 mx-auto mb-4" />
          <h2 className="text-xl font-semibold text-gray-900">
            Failed to load dashboard
          </h2>
          <p className="text-gray-600 mt-2">Please try refreshing the page</p>
        </div>
      </div>
    );
  }

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

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
          <p className="text-gray-600 mt-1">
            Real-time queue management overview
          </p>
        </div>
        <Button onClick={refreshData} disabled={refreshing}>
          <Activity className="h-4 w-4 mr-2" />
          {refreshing ? "Refreshing..." : "Refresh"}
        </Button>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Total Served Today
            </CardTitle>
            <UserCheck className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.totalServedToday}</div>
            <p className="text-xs text-muted-foreground">
              +{stats.todaySummary.totalTokensIssued} tokens issued
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Current Queue</CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{queueStatus.totalInQueue}</div>
            <p className="text-xs text-muted-foreground">
              ~{queueStatus.estimatedWaitTime} min wait time
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Active Counters
            </CardTitle>
            <Activity className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.activeCounters}</div>
            <p className="text-xs text-muted-foreground">
              {stats.activeStaff} staff on duty
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Avg Wait Time</CardTitle>
            <Clock className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {stats.todaySummary.averageWaitTime.toFixed(1)}m
            </div>
            <p className="text-xs text-muted-foreground">
              Peak: {stats.todaySummary.peakHour}
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Queue Status by Counter */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <BarChart3 className="h-5 w-5" />
            Live Queue Status
          </CardTitle>
          <CardDescription>
            Real-time queue status for each counter
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-4">
            {Object.entries(queueStatus.queueByCounter).map(
              ([counterNo, queueCount]) => {
                const status =
                  queueStatus.counterStatus[counterNo] || "UNKNOWN";
                const staffName =
                  queueStatus.assignedStaff[counterNo] || "Unassigned";

                return (
                  <div key={counterNo} className="border rounded-lg p-4">
                    <div className="flex items-center justify-between mb-2">
                      <h3 className="font-semibold">Counter {counterNo}</h3>
                      <span
                        className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(
                          status
                        )}`}
                      >
                        {status}
                      </span>
                    </div>
                    <div className="space-y-1">
                      <p className="text-sm text-gray-600">
                        <span className="font-medium">{queueCount}</span> in
                        queue
                      </p>
                      <p className="text-xs text-gray-500">
                        Staff: {staffName}
                      </p>
                      {queueCount > 0 && (
                        <p className="text-xs text-blue-600">
                          ~{Math.ceil(queueCount * 5)} min wait
                        </p>
                      )}
                    </div>
                  </div>
                );
              }
            )}
          </div>
        </CardContent>
      </Card>

      {/* Today's Summary */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <TrendingUp className="h-5 w-5" />
              Today's Performance
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="flex justify-between items-center">
              <span className="text-sm font-medium">Total Tokens Issued</span>
              <span className="text-lg font-bold">
                {stats.todaySummary.totalTokensIssued}
              </span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-sm font-medium">
                Total Passengers Served
              </span>
              <span className="text-lg font-bold">
                {stats.todaySummary.totalPassengersServed}
              </span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-sm font-medium">Peak Hour</span>
              <span className="text-lg font-bold">
                {stats.todaySummary.peakHour}
              </span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-sm font-medium">Peak Hour Tokens</span>
              <span className="text-lg font-bold">
                {stats.todaySummary.peakHourTokens}
              </span>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <CheckCircle className="h-5 w-5" />
              System Status
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="flex justify-between items-center">
              <span className="text-sm font-medium">Active Counters</span>
              <span className="text-lg font-bold text-green-600">
                {stats.activeCounters}
              </span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-sm font-medium">Staff on Duty</span>
              <span className="text-lg font-bold text-blue-600">
                {stats.activeStaff}
              </span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-sm font-medium">Current Queue</span>
              <span className="text-lg font-bold text-orange-600">
                {queueStatus.totalInQueue}
              </span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-sm font-medium">Est. Wait Time</span>
              <span className="text-lg font-bold text-purple-600">
                {queueStatus.estimatedWaitTime}m
              </span>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
