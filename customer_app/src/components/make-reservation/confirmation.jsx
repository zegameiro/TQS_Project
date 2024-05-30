import React, { useEffect, useState, useMemo } from 'react';
import QRCode from 'qrcode.react';
import { useMutation } from "@tanstack/react-query";
import axios from "../../../api";
import { addNewReservation } from "../../../actions/postActions";

const Confirmation = ({ reservationDetails, userData }) => {
    const [reservationCreated, setReservationCreated] = useState(false);
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
    ];

    const allSpecialities = reservationDetails.allSpecialities;
    const specialityToPost = reservationDetails.selectedServices[0];
    const specialityID = allSpecialities.find(speciality => speciality.name === specialityToPost).id;

    const reservation = useMemo(() => ({
        timestamp: new Date().getTime().toString(),
        secretCode: token,
        customerName: userData[0],
        customerEmail: userData[1],
        customerPhoneNumber: userData[2],
        specialityID: specialityID.toString(),
        roomID: reservationDetails.roomID,
    }), [token, userData, specialityID, reservationDetails.roomID]);

    const addReservationMutation = useMutation({
        mutationKey: ["addReservation"],
        mutationFn: () => addNewReservation(axios, reservation),
        onSuccess: () => {
        },
        onError: (error) => {
            console.log("ERROR ", error);
        }
    });

    useEffect(() => {
        if (token !== '' && !reservationCreated) {
            addReservationMutation.mutate();
            setReservationCreated(true);
        }
    }, [reservationCreated, token, reservation, addReservationMutation]);


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
};

export default Confirmation;
