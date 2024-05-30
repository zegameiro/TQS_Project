import React from 'react';
import { useState } from 'react';
import NavbarFixed from '../components/NavbarFixed';

import SearchReservation from '../components/check-reservation/search-reservation';
import GetReservation from '../components/check-reservation/get-reservation';

const ReservationCheck = () => {

    const [currentStep, setCurrentStep] = useState(0)
    const [reservationToken, setReservationToken] = useState('')

    return (
        <div className="min-h-screen flex flex-col">
            <NavbarFixed />

            <div className="m-5">
                <h1 style={{ textAlign: 'center', color: '#1F0F53', fontSize: '40px', fontWeight: 'bold' }}>Check your Reservation</h1>
                <div className="w-[70%] ml-[15%] mt-5 mb-10 h-[60vh] p-10 border-primary" style={{ border: '.125rem solid #220f67', borderRadius: '1rem'}}>
                    {
                        currentStep === 0 ? <SearchReservation setTokenToSearch={setReservationToken} setCurrentStep={setCurrentStep} /> :
                            currentStep === 1 ? <GetReservation token={reservationToken} setCurrentStep={setCurrentStep} />
                                :
                                null
                    }
                </div>
            </div>

        </div>
    );
}

export default ReservationCheck;