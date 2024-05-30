import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, Button, useDisclosure } from "@nextui-org/react";
import { useQuery } from "@tanstack/react-query";

import React, { useState } from "react";
import axios from "../../../api";
import { getReservationBySecretCode } from "../../../actions/getActions";

const ReservationField = ({ label, value }) => {
    let isPrice = false;
    if (label === 'Price') {
        isPrice = true;
    }

    return (
        <div className="text-xl mt-3">
            <span className="font-bold">{label}: </span>
            <span>{value}{isPrice ? "€" : ""}</span>
        </div>
    );
};


const GetReservation = ({ token, setCurrentStep }) => {

    const reservationGet = useQuery({
        queryKey: ["reservation", token],
        queryFn: () => getReservationBySecretCode(axios, token),
    });
    const reservation = {}

    if (reservationGet?.data !== '') {
        reservation.costumer =
        {
            "name": reservationGet.data?.customerName,
            "email": reservationGet.data?.customerEmail,
            "phone": reservationGet.data?.customerPhoneNumber
        }

        reservation.reservationDetails =
        {
            "employee": reservationGet.data?.employee.fullName,
            "date": new Date(reservationGet.data?.timestamp).toLocaleString()
        }
    }

    // Modal
    const { isOpen, onOpen, onOpenChange } = useDisclosure();

    const handleCancel = () => {
        alert("Reservation canceled");
    }

    return (
        <>
            {reservationGet &&
                <>
                    {reservationGet.data !== '' ? (
                        <>
                            <div className="h-[40vh]" style={{ display: 'flex', flexDirection: 'row', overflowY: "auto" }}>

                                <div className="w-[45vw] px-5">
                                    <h1 className="text-3xl font-bold mb-5" >Costumer Details</h1>
                                    <ReservationField label="Name" value={reservation.costumer.name} />
                                    <ReservationField label="Email" value={reservation.costumer.email} />
                                    <ReservationField label="Phone" value={reservation.costumer.phone} />
                                </div>

                                <div className="w-[45vw] px-5">
                                    <h1 className="text-3xl font-bold mb-5" >Reservation Details</h1>
                                    {/* <ReservationField label="Facility" value={reservation.reservationDetails.facility} /> */}
                                    {/* <ReservationField label="Section" value={reservation.reservationDetails.section} /> */}
                                    {/* <ReservationField label="Price" value={reservation.reservationDetails.price} /> */}
                                    <ReservationField label="Employee" value={reservation.reservationDetails.employee} />
                                    <ReservationField label="Date" value={reservation.reservationDetails.date} />
                                </div>

                            </div>

                            <div className="mt-5" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'center', alignItems: 'center' }}>
                                <Button color="primary" className="text-white m-1 mt-8" size="lg" onClick={() => setCurrentStep(0)}>Search Another Reservation</Button>
                                <Button color="danger" className="text-white m-1 mt-8" size="lg" onPress={onOpen}>Cancel Reservation</Button>
                            </div>
                        </>
                    ) : (
                        <div className="flex justify-center items-center h-[100%]" style={{ flexDirection: 'column' }}>
                            <h1 className="text-5xl text-danger font-bold">Reservation not found</h1>
                            <Button color="primary" className="text-white mt-[20vh]" size="lg" onClick={() => setCurrentStep(0)}>Back</Button>
                        </div>
                    )}
                </>
            }

            <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
                <ModalContent>
                    {(onClose) => (
                        <>
                            <ModalHeader className="flex flex-col gap-1">Confirm Cancelation</ModalHeader>
                            <ModalBody>
                                <p>Are you sure you want to cancel this reservation?</p>
                            </ModalBody>
                            <ModalFooter>
                                <Button color="danger" variant="light" onClick={handleCancel} onPress={onClose}>
                                    Yes, cancel
                                </Button>
                            </ModalFooter>
                        </>
                    )}
                </ModalContent>
            </Modal >
        </>
    );
}
export default GetReservation;
