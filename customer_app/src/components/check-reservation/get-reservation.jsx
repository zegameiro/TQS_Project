import { Button } from "@nextui-org/react";

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


const GetReservation = ({ token, allReservations, setCurrentStep }) => {

    const reservation = allReservations.find(reservation => reservation.id === token);

    return (
        <>
            {reservation ? (
                <>
                    <div className="h-[40vh]" style={{ display: 'flex', flexDirection: 'row', overflowY: "auto" }}>

                        <div className="w-[45vw] px-5">
                            <h1 className="text-3xl font-bold mb-5" >Costumer Details</h1>
                            <ReservationField label="Name" value={reservation.costumer.name} />
                            <ReservationField label="Email" value={reservation.costumer.email} />
                            <ReservationField label="Phone" value={reservation.costumer.phone} />
                            <ReservationField label="Address" value={reservation.costumer.address} />
                        </div>

                        <div className="w-[45vw] px-5">
                            <h1 className="text-3xl font-bold mb-5" >Reservation Details</h1>
                            <ReservationField label="Facility" value={reservation.reservationDetails.facility} />
                            <ReservationField label="Section" value={reservation.reservationDetails.section} />
                            <div className="text-xl mt-3">
                                <span className="font-bold">Services: </span>
                                <ul>
                                    {reservation.reservationDetails.services.map((service, index) => (
                                        <li key={index}>{service}</li>
                                    ))}
                                </ul>
                            </div>
                            <ReservationField label="Price" value={reservation.reservationDetails.price} />
                        </div>

                    </div>

                    <div className="mt-5" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'center', alignItems: 'center' }}>
                        <Button color="primary" className="text-white m-1 mt-8" size="lg" onClick={() => setCurrentStep(0)}>Search Another Reservation</Button>
                        <Button color="danger" className="text-white m-1 mt-8" size="lg">Cancel Reservation</Button>
                    </div>
                </>
            ) : (
                <div className="flex justify-center items-center h-[100%]" style={{ flexDirection: 'column' }}>
                    <h1 className="text-5xl text-danger font-bold">Reservation not found</h1>
                    <Button color="primary" className="text-white mt-[20vh]" size="lg" onClick={() => setCurrentStep(0)}>Back</Button>
                </div>
            )
            }
        </>
    );
}
export default GetReservation;
