import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { Plus, Trash2, Send, CheckCircle, User, Users } from "lucide-react";
import { useUser } from "../../contexts/UserContext";
import toast from "react-hot-toast";
import { apiClient } from "../../utils/api";

interface FormData {
  aadhaar: string;
  otp: string;
  journeyDate: string;
  fromStation: string;
  toStation: string;
  trainNumber: string;
}

interface CoPassenger {
  id: string;
  name: string;
  relation: string;
  aadhaar: string;
  gender?: string;
  dob?: string;
  age?: string;
  mobile?: string;
  address?: string;
}

const relations = [
  "Father",
  "Mother",
  "Spouse",
  "Son",
  "Daughter",
  "Brother",
  "Sister",
  "Friend",
  "Colleague",
];

const stations = [
  { code: "MFP", name: "Muzaffarpur Junction" },
  { code: "ANVT", name: "Anand Vihar Terminal" },
  { code: "HJP", name: "Hajipur Junction" },
  { code: "MAI", name: "Mehsi" },
  { code: "CAA", name: "Chakia" },
  { code: "PPA", name: "Pipra" },
  { code: "BMKI", name: "Bapudham Motihari" },
  { code: "SGL", name: "Sagauli Junction" },
  { code: "BTH", name: "Bettiah" },
  { code: "NKE", name: "Narkatiaganj Junction" },
  { code: "HIR", name: "Harinagar" },
  { code: "BUG", name: "Bagaha" },
  { code: "SBZ", name: "Siswa Bazar" },
  { code: "CPJ", name: "Kaptanganj Junction" },
  { code: "GKP", name: "Gorakhpur Junction" },
  { code: "GD", name: "Gonda Junction" },
  { code: "LKO", name: "Lucknow NR" },
  { code: "MB", name: "Moradabad" },
  { code: "PUNE", name: "Pune Junction" },
  { code: "SMVB", name: "SMVT Bengaluru" },
  { code: "ST", name: "Surat" },
  { code: "DLI", name: "Delhi Junction" },
  { code: "KOAA", name: "Kolkata" },
  { code: "ADI", name: "Ahmedabad Junction" },
  { code: "DDN", name: "Dehradun" },
  { code: "BXR", name: "Buxar" },
  { code: "DDU", name: "Pt. Deen Dayal Upadhyaya Junction" },
  { code: "PRYJ", name: "Prayagraj Junction" },
  { code: "CNB", name: "Kanpur Central" },
  { code: "JHS", name: "Jhansi Junction" },
  { code: "BPL", name: "Bhopal Junction" },
  { code: "ET", name: "Itarsi Junction" },
  { code: "KNW", name: "Khandwa" },
  { code: "BSL", name: "Bhusaval Junction" },
  { code: "MMR", name: "Manmad Junction" },
  { code: "SPJ", name: "Samastipur Junction" },
  { code: "BJU", name: "Barauni Junction" },
  { code: "BGS", name: "Begusarai" },
  { code: "KGG", name: "Khagaria Junction" },
  { code: "NNA", name: "Naugachia" },
  { code: "KIR", name: "Katihar Junction" },
  { code: "KNE", name: "Kishanganj" },
  { code: "NJP", name: "New Jalpaiguri" },
  { code: "NCB", name: "New Cooch Behar" },
  { code: "NBQ", name: "New Bongaigaon" },
  { code: "GHY", name: "Guwahati" },
  { code: "JBP", name: "Jabalpur" },
  { code: "NGP", name: "Nagpur" },
  { code: "BPQ", name: "Balharshah" },
  { code: "BZA", name: "Vijayawada Junction" },
  { code: "MAS", name: "Chennai Central" },
  { code: "KPD", name: "Katpadi Junction" },
  { code: "RXL", name: "Raxaul Junction" },
  { code: "MTR", name: "Motipur" },
  { code: "DEOS", name: "Deoria Sadar" },
  { code: "KLD", name: "Khalilabad" },
  { code: "BST", name: "Basti" },
  { code: "BXN", name: "Bayana Junction" },
  { code: "GGC", name: "Gangapur City" },
  { code: "SWM", name: "Sawai Madhopur" },
  { code: "KOTA", name: "Kota Junction" },
  { code: "NAD", name: "Nagda Junction" },
  { code: "RTM", name: "Ratlam Junction" },
  { code: "BRC", name: "Vadodara Junction" },
  { code: "HW", name: "Haridwar" },
  { code: "MAU", name: "Mau Junction" },
  { code: "JOP", name: "Jaunpur City" },
  { code: "SLN", name: "Sultanpur" },
  { code: "SPN", name: "Shahjahanpur" },
  { code: "BE", name: "Bareilly" },
  { code: "MLDT", name: "Malda Town" },
  { code: "MJL", name: "Majhowlia" },
  { code: "ARJ", name: "Aunrihar Junction" },
  { code: "YPR", name: "Yesvantpur Junction" },
  { code: "BDTS", name: "Bandra Terminus" },
];

// Masking helpers
function maskAadhaar(aadhaar: string) {
  return aadhaar && aadhaar.length === 12
    ? `XXXX-XXXX-${aadhaar.slice(-4)}`
    : aadhaar;
}
function maskMobile(mobile: string) {
  return mobile && mobile.length === 10 ? `XXXXXX${mobile.slice(-4)}` : mobile;
}

function extractCode(stationLabel: string) {
  const match = stationLabel.match(/\(([^)]+)\)$/);
  return match ? match[1] : stationLabel;
}

export default function Registration() {
  const [step, setStep] = useState(1);
  const [otpSent, setOtpSent] = useState(false);
  const [otpVerified, setOtpVerified] = useState(false);
  const [coPassengers, setCoPassengers] = useState<CoPassenger[]>([]);
  const [loading, setLoading] = useState(false);
  const [familyMembers, setFamilyMembers] = useState<any[]>([]);
  const [showFamilyList, setShowFamilyList] = useState(false);
  const [viewMember, setViewMember] = useState<any>(null);
  const [mainPersonDetails, setMainPersonDetails] = useState<any>(null);
  const [trainName, setTrainName] = useState("");
  const [trainOptions, setTrainOptions] = useState<any[]>([]);
  const [showTrainDropdown, setShowTrainDropdown] = useState(false);

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
    setValue,
  } = useForm<FormData>();
  const { userData, setUserData } = useUser();
  const navigate = useNavigate();

  const watchedAadhaar = watch("aadhaar");
  const watchedFrom = watch("fromStation");
  const watchedTo = watch("toStation");
  const watchedTrainNumber = watch("trainNumber");

  // Auto-fill Train Number/Name when From and To are selected
  useEffect(() => {
    if (watchedFrom && watchedTo) {
      apiClient.getTrainsByStations(watchedFrom, watchedTo).then((trains) => {
        if (trains.length === 1) {
          setValue("trainNumber", trains[0].trainNumber);
          setTrainName(trains[0].trainName);
          setShowTrainDropdown(false);
        } else if (trains.length > 1) {
          setTrainOptions(trains);
          setShowTrainDropdown(true);
        } else {
          setTrainName("");
          setShowTrainDropdown(false);
        }
      });
    }
  }, [watchedFrom, watchedTo, setValue]);

  // Auto-fill From/To/Train Name when Train Number is selected
  useEffect(() => {
    if (watchedTrainNumber) {
      apiClient.getTrainRoute(watchedTrainNumber).then((data) => {
        if (data && data.route && data.route.length > 1) {
          setValue("fromStation", data.route[0].code);
          setValue("toStation", data.route[data.route.length - 1].code);
          setTrainName(data.trainName);
        }
      });
    }
  }, [watchedTrainNumber, setValue]);

  const sendOTP = async () => {
    if (!watchedAadhaar || watchedAadhaar.length !== 12) {
      toast.error("Please enter a valid 12-digit Aadhaar number");
      return;
    }
    setLoading(true);
    try {
      await apiClient.sendOtp(watchedAadhaar);
      setOtpSent(true);
      setLoading(false);
      setValue("otp", "");
      toast.success("OTP sent to your registered mobile number");
    } catch (err) {
      setLoading(false);
      toast.error("Failed to send OTP. Please try again.");
    }
  };

  const verifyOTP = async (otp: string) => {
    if (otp.length !== 6) return;
    setLoading(true);
    try {
      const response = await apiClient.verifyOtp(watchedAadhaar, otp);
      if (response.aadhaarNo) {
        setOtpVerified(true);
        setLoading(false);
        toast.success("OTP verified successfully!");
        setStep(2);
        setMainPersonDetails(response);
        // Fetch family members
        try {
          const family = await apiClient.getFamilyMembers(watchedAadhaar);
          setFamilyMembers(family);
          setShowFamilyList(true);
        } catch (err) {
          setFamilyMembers([]);
          setShowFamilyList(false);
        }
      } else {
        setLoading(false);
        toast.error("Invalid OTP. Please try again.");
      }
    } catch (err) {
      setLoading(false);
      toast.error("Invalid OTP. Please try again.");
    }
  };

  const onSubmit = async (data: FormData) => {
    if (!otpVerified) {
      toast.error("Please verify your Aadhaar OTP first");
      return;
    }
    setLoading(true);
    const payload = {
      aadhaarNo: data.aadhaar,
      station: data.fromStation, // or use a single station field if your UI allows
      journeyDate: data.journeyDate,
      trainNo: data.trainNumber,
      coPassengers: coPassengers.map((cp) => cp.aadhaar),
    };
    try {
      const result = await apiClient.createJourney(payload);
      setUserData({
        ...data,
        name: mainPersonDetails?.name || "",
        coPassengers,
        otpVerified: true,
        stationCode: userData?.stationCode || "",
        tokenNo: result.tokenNo,
        counterNo: result.counterNo,
        counterPosition: result.counterPosition,
      });
      setLoading(false);
      toast.success("Registration successful!");
      navigate(`/token/${result.tokenNo}`);
    } catch (err) {
      setLoading(false);
      toast.error("Registration failed. Please try again.");
    }
  };

  return (
    <div className="max-w-2xl mx-auto bg-white rounded-xl shadow-lg overflow-hidden">
      <div className="bg-gradient-to-r from-blue-600 to-blue-800 px-6 py-6 text-white">
        <h1 className="text-2xl font-bold mb-2">Passenger Registration</h1>
        <div className="flex items-center space-x-4">
          <div
            className={`flex items-center space-x-2 ${
              step >= 1 ? "opacity-100" : "opacity-50"
            }`}
          >
            {otpVerified ? (
              <CheckCircle className="h-5 w-5" />
            ) : (
              <User className="h-5 w-5" />
            )}
            <span className="text-sm">Personal Details</span>
          </div>
          <div className="h-px bg-blue-300 flex-1"></div>
          <div
            className={`flex items-center space-x-2 ${
              step >= 2 ? "opacity-100" : "opacity-50"
            }`}
          >
            <Users className="h-5 w-5" />
            <span className="text-sm">Journey Details</span>
          </div>
        </div>
      </div>

      <form onSubmit={handleSubmit(onSubmit)} className="p-6 space-y-6">
        {step === 1 && (
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Aadhaar Number *
              </label>
              <input
                type="text"
                {...register("aadhaar", {
                  required: "Aadhaar number is required",
                  pattern: {
                    value: /^\d{12}$/,
                    message: "Please enter a valid 12-digit Aadhaar number",
                  },
                })}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Enter 12-digit Aadhaar number"
                maxLength={12}
              />
              {errors.aadhaar && (
                <p className="text-red-500 text-sm mt-1">
                  {errors.aadhaar.message}
                </p>
              )}
            </div>

            {!otpSent ? (
              <button
                type="button"
                onClick={sendOTP}
                disabled={
                  loading || !watchedAadhaar || watchedAadhaar.length !== 12
                }
                className="w-full bg-orange-500 hover:bg-orange-600 disabled:bg-gray-300 disabled:cursor-not-allowed text-white font-semibold py-3 px-6 rounded-lg transition-colors flex items-center justify-center space-x-2"
              >
                {loading ? (
                  <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                ) : (
                  <>
                    <Send className="h-5 w-5" />
                    <span>Get OTP</span>
                  </>
                )}
              </button>
            ) : (
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Enter OTP *
                </label>
                <input
                  type="text"
                  inputMode="numeric"
                  pattern="[0-9]*"
                  {...register("otp", {
                    required: "OTP is required",
                    pattern: {
                      value: /^[0-9]{6}$/,
                      message: "OTP must be a 6-digit number",
                    },
                    onChange: (e) => {
                      if (e.target.value.length === 6) {
                        verifyOTP(e.target.value);
                      }
                    },
                  })}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-center text-lg tracking-widest"
                  placeholder="Enter 6-digit OTP"
                  maxLength={6}
                />
                {errors.otp && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.otp.message}
                  </p>
                )}
                {otpSent && (
                  <p className="text-xs text-gray-500 mt-1">
                    <b>OTP sent to your registered mobile number.</b>
                  </p>
                )}
              </div>
            )}
          </div>
        )}

        {step === 2 && (
          <div className="space-y-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Full Name
              </label>
              <input
                type="text"
                value={mainPersonDetails?.name || ""}
                readOnly
                className="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-100 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Name will be autofilled after OTP verification"
              />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Journey Date *
                </label>
                <input
                  type="date"
                  {...register("journeyDate", {
                    required: "Journey date is required",
                  })}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  min={new Date().toISOString().split("T")[0]}
                />
                {errors.journeyDate && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.journeyDate.message}
                  </p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Train Number *
                </label>
                <input
                  type="text"
                  {...register("trainNumber", {
                    required: "Train number is required",
                  })}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  placeholder="e.g., 12001"
                />
                {errors.trainNumber && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.trainNumber.message}
                  </p>
                )}
                {showTrainDropdown && trainOptions.length > 1 && (
                  <select
                    className="mt-2 w-full px-4 py-2 border border-gray-300 rounded-lg"
                    onChange={(e) => {
                      const selected = trainOptions.find(
                        (t) => t.trainNumber === e.target.value
                      );
                      setValue("trainNumber", selected.trainNumber);
                      setTrainName(selected.trainName);
                      setShowTrainDropdown(false);
                    }}
                    defaultValue=""
                  >
                    <option value="" disabled>
                      Select a train
                    </option>
                    {trainOptions.map((opt) => (
                      <option key={opt.trainNumber} value={opt.trainNumber}>
                        {opt.trainNumber} - {opt.trainName}
                      </option>
                    ))}
                  </select>
                )}
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Train Name
                </label>
                <input
                  type="text"
                  value={trainName}
                  readOnly
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-100 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  placeholder="Train name will be auto-filled"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  From Station *
                </label>
                <select
                  {...register("fromStation", {
                    required: "From station is required",
                  })}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  <option value="">Select station</option>
                  {stations.map((station) => (
                    <option key={station.code} value={station.code}>
                      {station.name} ({station.code})
                    </option>
                  ))}
                </select>
                {errors.fromStation && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.fromStation.message}
                  </p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  To Station *
                </label>
                <select
                  {...register("toStation", {
                    required: "To station is required",
                  })}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  <option value="">Select station</option>
                  {stations.map((station) => (
                    <option key={station.code} value={station.code}>
                      {station.name} ({station.code})
                    </option>
                  ))}
                </select>
                {errors.toStation && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.toStation.message}
                  </p>
                )}
              </div>
            </div>

            {/* Family Members Suggestion (only way to add co-passenger) */}
            {showFamilyList && familyMembers.length > 0 && (
              <div className="mb-4">
                <h3 className="font-semibold mb-2">
                  Select Family Member(s) to Add as Co-Passenger:
                </h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-2">
                  {familyMembers.map((member, idx) => {
                    const alreadyAdded = coPassengers.some(
                      (cp) => cp.aadhaar === member.aadhaar
                    );
                    return (
                      <div
                        key={idx}
                        className="border rounded p-2 flex flex-col bg-gray-50"
                      >
                        <span>
                          <b>Name:</b> {member.name}
                        </span>
                        <span>
                          <b>Relation:</b> {member.relation || "Relation"}
                        </span>
                        <span>
                          <b>Aadhaar:</b> {maskAadhaar(member.aadhaar)}
                        </span>
                        <span>
                          <b>Mobile:</b> {maskMobile(member.mobile)}
                        </span>
                        <button
                          type="button"
                          className="mt-2 bg-blue-500 hover:bg-blue-600 text-white px-2 py-1 rounded"
                          onClick={() => setViewMember(member)}
                        >
                          View
                        </button>
                        <button
                          type="button"
                          className={`mt-2 ${
                            alreadyAdded
                              ? "bg-gray-400"
                              : "bg-green-500 hover:bg-green-600"
                          } text-white px-2 py-1 rounded`}
                          onClick={() => {
                            if (alreadyAdded) return;
                            if (coPassengers.length >= 5) {
                              toast.error("Maximum 5 co-passengers allowed");
                              return;
                            }
                            setCoPassengers([
                              ...coPassengers,
                              {
                                id: Date.now().toString(),
                                name: member.name,
                                relation: member.relation,
                                aadhaar: member.aadhaar,
                                gender: member.gender,
                                dob: member.dob,
                                age: member.age,
                                mobile: member.mobile,
                                address: member.address,
                              },
                            ]);
                            toast.success("Added as co-passenger");
                          }}
                          disabled={alreadyAdded}
                        >
                          {alreadyAdded ? "Added" : "Add as Co-Passenger"}
                        </button>
                        {alreadyAdded && (
                          <button
                            type="button"
                            className="mt-2 bg-red-500 hover:bg-red-600 text-white px-2 py-1 rounded"
                            onClick={() =>
                              setCoPassengers(
                                coPassengers.filter(
                                  (cp) => cp.aadhaar !== member.aadhaar
                                )
                              )
                            }
                          >
                            Remove
                          </button>
                        )}
                      </div>
                    );
                  })}
                </div>
              </div>
            )}

            {/* View Modal */}
            {viewMember && (
              <div
                style={{
                  position: "fixed",
                  top: 100,
                  left: "50%",
                  transform: "translateX(-50%)",
                  background: "#fff",
                  border: "1px solid #ccc",
                  padding: 20,
                  zIndex: 1000,
                }}
              >
                <h4>Family Member Details</h4>
                <p>
                  <b>Name:</b> {viewMember.name}
                </p>
                <p>
                  <b>Relation:</b> {viewMember.relation || "Relation"}
                </p>
                <p>
                  <b>Gender:</b> {viewMember.gender}
                </p>
                <p>
                  <b>Age:</b> {viewMember.age}
                </p>
                <p>
                  <b>Date of Birth:</b> {viewMember.dob}
                </p>
                <p>
                  <b>Aadhaar:</b> {maskAadhaar(viewMember.aadhaar)}
                </p>
                <p>
                  <b>Mobile:</b> {maskMobile(viewMember.mobile)}
                </p>
                <p>
                  <b>Address:</b> {viewMember.address}
                </p>
                <button
                  onClick={() => setViewMember(null)}
                  className="mt-2 bg-gray-500 hover:bg-gray-600 text-white px-2 py-1 rounded"
                >
                  Close
                </button>
              </div>
            )}

            <button
              type="submit"
              disabled={!otpVerified || loading}
              className="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed text-white font-semibold py-4 px-6 rounded-lg transition-colors flex items-center justify-center space-x-2"
            >
              {loading ? (
                <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
              ) : (
                <>
                  <CheckCircle className="h-5 w-5" />
                  <span>Submit Registration</span>
                </>
              )}
            </button>
          </div>
        )}
      </form>
    </div>
  );
}
