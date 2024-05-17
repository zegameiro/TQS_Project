import React from 'react';
import { useState } from 'react';
import NavbarFixed from '../components/NavbarFixed';

import SearchReservation from '../components/check-reservation/search-reservation';
import GetReservation from '../components/check-reservation/get-reservation';

const ReservationCheck = () => {

    const [currentStep, setCurrentStep] = useState(0)
    const [reservationToken, setReservationToken] = useState('')

    const reservations = [
        {
            "id": "90xB6rqbcR",
            "reservationDetails": {
                "facility": "Aveiro",
                "section": "Basic Hairdresser",
                "services": [
                    "Hair Cut",
                    "Brushing",
                    "Beard Trimming"
                ],
                "price": 20
            },
            "costumer": {
                "name": "Cristiano Ronaldo",
                "email": "cr7@gmail.com",
                "phone": "897654132",
                "address": "Avenida das Arábias, nº7"
            }
        },
        {
            "id": "zauhZIPEMh",
            "reservationDetails": {
                "facility": "Aveiro",
                "section": "Complex Hairdresser",
                "services": [
                    "Coloring",
                    "Perm",
                    "Straightening/Curling",
                    "Extensions",
                    "Discoloration"
                ],
                "price": 170
            },
            "costumer": {
                "name": "Dua Lipa",
                "email": "dua_lipa@music.us",
                "phone": "45618891896",
                "address": "100th Main Star Street"
            }
        },
        {
            "id": "bX46viUsGU",
            "reservationDetails": {
                "facility": "Lisbon",
                "section": "Spa",
                "services": [
                    "Pools",
                    "Jacuzzi",
                    "Facial treatments",
                    "Massages",
                    "Dermatological treatments",
                    "Turkish bath"
                ],
                "price": 135
            },
            "costumer": {
                "name": "Donald Trump",
                "email": "mr.trump_the_donald@usa.us",
                "phone": "4548596489",
                "address": "Trump Tower, 100th Floor"
            }
        }
    ]


    console.log("IDs examples: ", reservations.map(reservation => reservation.id));

    return (
        <div className="min-h-screen flex flex-col">
            <NavbarFixed />

            <div className="m-5">
                <h1 style={{ textAlign: 'center', color: '#1F0F53', fontSize: '40px', fontWeight: 'bold' }}>Check your Reservation</h1>
                <div className="w-[70%] ml-[15%] mt-5 mb-10 h-[60vh] p-10 border-primary" style={{ border: '.125rem solid #220f67', borderRadius: '1rem'}}>
                    {
                        currentStep === 0 ? <SearchReservation setTokenToSearch={setReservationToken} setCurrentStep={setCurrentStep} /> :
                            currentStep === 1 ? <GetReservation token={reservationToken} allReservations={reservations} setCurrentStep={setCurrentStep} />
                                :
                                null
                    }
                </div>
            </div>

        </div>
    );
}

export default ReservationCheck;