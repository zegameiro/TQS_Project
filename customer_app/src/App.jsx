import { BrowserRouter, Route, Routes } from 'react-router-dom'

import HomePage from './pages/HomePage'
import Reservation from './pages/Reservation'

function App() {
  return ( 
    <>
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<HomePage />} />
          <Route path='/reservation' element={<Reservation />} />
        </Routes>
      </BrowserRouter>
    </>
  )
}

export default App
