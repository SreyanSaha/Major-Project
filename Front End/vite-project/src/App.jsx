import AdminAuth from './adminSection/adminLoginSignup'
import UserAuth from './userSection/UserLoginSignup'
import AdminDashboard from './adminSection/AdminDashboard'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import UserDashboard from './userSection/UserDashboard'
import CampaignPostCard from './userSection/components/CampaignFullPostComponent'
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<UserDashboard activeTab="posts"/>} />
        <Route path="/campaigns" element={<UserDashboard activeTab="campaigns"/>} />
        <Route path="/emergency-posts" element={<UserDashboard activeTab="emergency"/>} />

        <Route path="/user/login" element={<UserAuth status="false"/>} />
        <Route path="/user/signup" element={<UserAuth />} />

        <Route path="/admin/login" element={<AdminAuth status="false"/>} />
        <Route path="/admin/signup" element={<AdminAuth />} />

        <Route path="/admin/dashboard" element={<AdminDashboard />} />
        <Route path="/campaign" element={<CampaignPostCard/>}/>
      </Routes>
    </Router>
  )
}

export default App
