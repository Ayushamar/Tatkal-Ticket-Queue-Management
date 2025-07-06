# 🚀 Railway Deployment Guide

## Prerequisites

- GitHub repository with your code
- Railway account (free tier available)

## Step-by-Step Deployment

### 1. Connect to Railway

1. Go to [railway.app](https://railway.app)
2. Sign in with GitHub
3. Click "New Project"
4. Select "Deploy from GitHub repo"
5. Choose your repository

### 2. Railway Configuration

Railway will automatically detect the `railway.toml` configuration:

- **Builder**: Nixpacks
- **Start Command**: `cd backend && java -jar target/backend-0.0.1-SNAPSHOT.jar`
- **Health Check**: `/api/stations`

### 3. Database Setup

1. In Railway dashboard, click "New Service"
2. Select "Database" → "PostgreSQL"
3. Railway will create a PostgreSQL database automatically

### 4. Environment Variables

Add these variables in Railway dashboard → Variables tab:

```bash
# Database (Railway will provide these automatically)
DATABASE_URL=postgresql://username:password@host:port/database_name
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password

# Application
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=8080

# Security (CHANGE THESE!)
JWT_SECRET=your-super-secure-jwt-secret-key-here
ADMIN_USERNAME=Admin
ADMIN_PASSWORD=your-secure-admin-password

# Storage
PDF_OUTPUT_DIR=./pdf-tokens
```

### 5. Deploy

1. Railway will automatically deploy when you push to main branch
2. Monitor deployment in the dashboard
3. Check logs if there are issues

### 6. Access Your API

- **Domain**: `https://your-app-name.railway.app`
- **API Base**: `https://your-app-name.railway.app/api`
- **Health Check**: `https://your-app-name.railway.app/api/stations`

## API Endpoints

### Public Endpoints

- `GET /api/stations` - List stations
- `POST /api/otp/send` - Send OTP
- `POST /api/otp/verify` - Verify OTP
- `POST /api/journey` - Create journey
- `GET /api/journey/{tokenNo}/pdf` - Download PDF

### Admin Endpoints (JWT Protected)

- `POST /api/admin/login` - Admin login
- `GET /api/admin/passengers` - List passengers
- `GET /api/admin/counters` - Counter management
- `GET /api/admin/staff` - Staff management
- `GET /api/admin/reports` - Reports

## Frontend Deployment

### Option 1: Vercel (Recommended)

1. Go to [vercel.com](https://vercel.com)
2. Import your GitHub repository
3. Set environment variable:
   ```
   VITE_API_BASE_URL=https://your-app-name.railway.app/api
   ```
4. Deploy

### Option 2: Netlify

1. Go to [netlify.com](https://netlify.com)
2. Import your GitHub repository
3. Set environment variable:
   ```
   VITE_API_BASE_URL=https://your-app-name.railway.app/api
   ```
4. Deploy

### Option 3: Railway Static Site

1. In Railway, add new service
2. Select "Static Site"
3. Point to `frontend/dist` directory
4. Set build command: `cd frontend && npm install && npm run build`

## Troubleshooting

### Common Issues

1. **Build Fails**

   - Check Railway logs
   - Ensure all files are committed to GitHub
   - Verify Java 17 compatibility

2. **Database Connection Issues**

   - Verify DATABASE_URL is correct
   - Check if PostgreSQL service is running
   - Ensure database credentials are correct

3. **CORS Issues**

   - Update CORS configuration in backend
   - Add your frontend domain to allowed origins

4. **Port Issues**
   - Railway uses PORT environment variable
   - Ensure SERVER_PORT is set correctly

### Useful Commands

```bash
# Check Railway logs
railway logs

# Connect to Railway CLI
railway login

# Deploy manually
railway up

# Check service status
railway status
```

## Security Checklist

- [ ] Change default JWT secret
- [ ] Change default admin credentials
- [ ] Use HTTPS (Railway provides automatically)
- [ ] Set up proper CORS configuration
- [ ] Use environment variables for sensitive data
- [ ] Enable database SSL (Railway handles this)

## Monitoring

Railway provides:

- Real-time logs
- Performance metrics
- Error tracking
- Automatic restarts
- Health checks

## Cost Optimization

- Railway free tier includes:
  - 500 hours/month
  - 1GB storage
  - Shared CPU
- Upgrade to paid plan for:
  - Dedicated resources
  - Custom domains
  - Team collaboration
