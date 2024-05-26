import React, { useEffect, useState } from 'react';
import QRCode from 'qrcode.react';

const Confirmation = ({ reservationDetails, userData }) => {

    console.log(reservationDetails);
    console.log(userData);

    const [token, setToken] = useState('');

    useEffect(() => {
        let token = '';
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        for (let i = 0; i < 10; i++) {
            token += characters.charAt(Math.floor(Math.random() * characters.length));
        }
        setToken(token);
    }, []);

    const notes = [
        "The QR code or reservation token will be used when entering the establishment on the day of the reservation or to check the current status of the reservation on the website.",
        "A copy of the reservation will be sent to the email provided.",
    ]

    const reservation = {
        id: token,
        reservationDetails: {
            facility: reservationDetails.location,
            section: reservationDetails.service,
            services: reservationDetails.selectedServices,
            price: reservationDetails.priceToPay
        },
        costumer: {
            name: userData[0],
            email: userData[1],
            phone: userData[2],
            address: userData[3]
        }
    }

    console.log(reservation);

    return (
        <div>
            <div className="flex justify-center items-center mt-5">
                <QRCode value={token} size={200} />
            </div>
            <div className="flex justify-center items-center mt-1">
                <span className="text-xl">{token}</span>
            </div>
            <div style={{ padding: '4vh 7.5vw', paddingBottom: 0, fontSize: '1.15rem' }}>
                {notes.map((note, index) => (
                    <p key={index}><span style={{ color: '#1F0F53', fontWeight: 'bold' }}>{index + 1}.</span> {note}</p>
                ))}
            </div>
            <div className="flex justify-center items-center mt-10">
                <span className='text-xl text-center'>JSON example of the reservation:</span >
            </div>
            <div className="flex justify-center items-center mt-1">
                <pre>{JSON.stringify(reservation, null, 2)}</pre>
            </div>
        </div>
    );
}

export default Confirmation;