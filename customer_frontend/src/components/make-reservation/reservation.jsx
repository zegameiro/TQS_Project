import React, { useEffect, useState } from 'react';
import QRCode from 'qrcode.react';

export default function Reservation() {
    const [token, setToken] = useState('');

    useEffect(() => {
        let token = '';
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        for (let i = 0; i < 8; i++) {
            token += characters.charAt(Math.floor(Math.random() * characters.length));
        }
        setToken(token);
    }, []);

    const notes = [
        "The reservation will be valid after payment.",
        "A copy of the reservation will be sent to the email provided.",
        "The QR code or reservation token will be used when entering the establishment on the day of the reservation or to check the current status of the reservation on the website."
    ]

    return (
        <div>
            <div className="flex justify-center items-center mt-5">
                <QRCode value="https://www.youtube.com/watch?v=dQw4w9WgXcQ" size={200} />
            </div>
            <div className="flex justify-center items-center mt-1">
                <span className="text-xl">{token}</span>
            </div>
            <div style={{ padding: '4vh 7.5vw', paddingBottom: 0, fontSize: '1.15rem' }}>
                {notes.map((note, index) => (
                    <p key={index}><span style={{ color: '#1F0F53', fontWeight: 'bold' }}>{index + 1}.</span> {note}</p>
                ))}
            </div>
        </div>
    );
}