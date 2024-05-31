import { BrowserRouter, Route, Routes } from 'react-router-dom'

import HomePage from './pages/HomePage'
import Reservation from './pages/Reservation'
import ReservationCheck from './pages/ReservationCheck'

function App() {
  return ( 
    <>
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<HomePage />} />
          <Route path='/reservation' element={<Reservation />} />
          <Route path='/reservation-check' element={<ReservationCheck />} />
        </Routes>
      </BrowserRouter>
    </>
  )
}

export default App
