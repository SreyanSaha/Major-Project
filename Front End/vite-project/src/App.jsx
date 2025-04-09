import AdminAuth from './adminSection/adminLoginSignup'
import AdminDashboard from './adminSection/AdminDashboard'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/admin/login" element={<AdminAuth status="false"/>} />
        <Route path="/admin/signup" element={<AdminAuth />} />
        <Route path="/admin/dashboard" element={<AdminDashboard />} />
        {/* <Route path="/admin" element={<AdminPanel />} />
        <Route path="*" element={<NotFound />} /> */}
      </Routes>
    </Router>
  )
}

export default App
