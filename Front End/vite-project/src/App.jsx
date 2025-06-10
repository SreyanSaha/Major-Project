import AdminAuth from './adminSection/adminLoginSignup'
import UserAuth from './userSection/UserLoginSignup'
import AdminDashboard from './adminSection/AdminDashboard'
import { Routes, Route } from 'react-router-dom';
import UserDashboard from './userSection/UserDashboard'
import CampaignPostCard from './userSection/components/CampaignFullPostComponent'
import PostView from './userSection/components/PostFullPostComponent'
import AllUserProfile from './userSection/components/AllUserFullProfileComponent';
import EmergencyPostView from './userSection/components/EmergencyPostView';

function App() {
  return (
    <Routes>
      <Route path="/" element={<UserDashboard activeTab="posts"/>} />
      <Route path="/campaigns" element={<UserDashboard activeTab="campaigns"/>} />
      <Route path="/emergency-posts" element={<UserDashboard activeTab="emergency"/>} />
      <Route path="/user/login" element={<UserAuth status="false"/>} />
      <Route path="/user/signup" element={<UserAuth />} />
      
      <Route path="/admin/login" element={<AdminAuth status="false"/>} />
      <Route path="/admin/signup" element={<AdminAuth />} />
      <Route path="/admin/dashboard" element={<AdminDashboard />} />

      <Route path="/campaign/:campaignId" element={<CampaignPostCard/>}/>
      <Route path="/post/:postId" element={<PostView/>}/>
      <Route path="/profile/:userId" element={<AllUserProfile/>}/>
      <Route path="/emergency/:emergencyPostId" element={<EmergencyPostView/>}/>
    </Routes>
  )
}

export default App
