
import React, { useState } from "react";
import { Button, Progress } from "@nextui-org/react";
import { format } from 'date-fns';

import NavbarFixed from "../components/NavbarFixed"
import ChooseService from "../components/make-reservation/chooseService";
import PickTimeSlot from "../components/make-reservation/pickTimeSlot";
import Payment from "../components/make-reservation/payment";
import Confirmation from "../components/make-reservation/confirmation";

import { useNavigate } from "react-router-dom";

import { useMutation, useQuery } from "@tanstack/react-query"
import axios from "../../api"
import { getRoomById, getSpecialitiesByBeautyServiceID } from "../../actions/getActions";

const Reservation = () => {

  const steps = ["Choose services", "Pick a Time Slot", "Payment", "Here it is your reservation"]
  const components = [<ChooseService />, <PickTimeSlot />, <Payment />, <Confirmation />]
  const [currentStep, setCurrentStep] = useState(0)

  const [selectedServices, setSelectedServices] = useState([]);

  const [selectedDate, setSelectedDate] = useState(format(new Date(), 'yyyy-MM-dd'));
  const [selectedHour, setSelectedHour] = useState("");
  const [selectedMinute, setSelectedMinute] = useState("");

  // client data
  const [clientName, setClientName] = useState("");
  const [clientEmail, setClientEmail] = useState("");
  const [clientPhone, setClientPhone] = useState("");

  // payment
  const [priceToPay, setPriceToPay] = useState(0);

  // get URL
  const url = window.location.href.split('?')[1];
  const roomID = url.split('+')[0];

  const room = useQuery({
    queryKey: ["room", roomID],
    queryFn: () => getRoomById(axios, roomID),
  })

  const specialities = useQuery({
    queryKey: ["specialities"],
    queryFn: () => getSpecialitiesByBeautyServiceID(axios, room?.data?.beautyServiceId),
  })

  console.log(specialities?.data)
  const specialitiesList = specialities?.data?.map(speciality => ({
    name: speciality.name,
    price: speciality.price,
  }))

  const allSpecialities = specialities?.data;

  // navigation
  const navigate = useNavigate();

  const handleClickHome = () => {
    navigate("/")
  }

  const handleClickBack = () => {
    document.getElementById("warning-label").innerHTML = "";
    setCurrentStep(currentStep - 1)
  };

  const handleClickNext = () => {
    if (currentStep === 0 && selectedServices.length === 0) {
      document.getElementById("warning-label").innerHTML = "Please select at least one service";
      return;
    }
    if (currentStep === 1 && (selectedDate === null || selectedHour === "" || selectedMinute === "")) {
      document.getElementById("warning-label").innerHTML = "Please select a date and time";
      return;
    }
    if (currentStep === 2 && (clientName === "" || clientEmail === "" || clientPhone === "")) {
      document.getElementById("warning-label").innerHTML = "Please fill all the fields";
      return;
    }
    document.getElementById("warning-label").innerHTML = "";
    setCurrentStep(currentStep + 1)
  }

  function hasPreviousButton() {
    return currentStep > 0 && currentStep < steps.length - 1;
  }

  function hasNextButton() {
    return currentStep < steps.length - 1;
  }

  function isConcludeButton() {
    return currentStep === steps.length - 2;
  }

  function isLastStep() {
    return currentStep === steps.length - 1;
  }

  return (
    <div className="min-h-screen flex flex-col">
      <NavbarFixed />

      <div className="m-5">
        <h1 style={{ textAlign: 'center', color: '#1F0F53', fontSize: '40px', fontWeight: 'bold' }}>{steps[currentStep]}</h1>
        <div className="w-[70%] ml-[15%] mt-5 mb-10 h-[50vh] p-10 border-primary" style={{ border: '.125rem solid #220f67', borderRadius: '1rem', overflowY: 'auto' }}>
          {specialitiesList &&
            <div>
              {
                currentStep === 0 ? <ChooseService service={room.data.name} services={specialitiesList} selectedServices={selectedServices} setSelectedServices={setSelectedServices} /> :
                  currentStep === 1 ? <PickTimeSlot selectedDate={selectedDate} setSelectedDate={setSelectedDate} selectedHour={selectedHour} setSelectedHour={setSelectedHour} selectedMinute={selectedMinute} setSelectedMinute={setSelectedMinute} /> :
                    currentStep === 2 ? <Payment services={specialitiesList} selectedServices={selectedServices} selectedPaymentData={[clientName, clientEmail, clientPhone]} setSelectedPaymentData={[setClientName, setClientEmail, setClientPhone]} setPriceToPay={setPriceToPay} /> :
                      currentStep === 3 ? <Confirmation reservationDetails={{ roomID, selectedServices, allSpecialities }} userData={[clientName, clientEmail, clientPhone]} /> :
                        components[currentStep]
              }
            </div>
          }
        </div>


        {!isLastStep() ? (
          <div style={{ margin: '0 5vw', display: 'flex', flexDirection: 'row' }}>
            <div style={{ width: '25vw' }}>
              {hasPreviousButton() && <Button size="lg" className="button-blue" onPress={handleClickBack}>Back</Button>}
            </div>
            <div style={{ width: '50vw', textAlign: 'center' }}>
              <span id="warning-label" className="text-danger font-bold text-xl"></span>
            </div>
            <div style={{ width: '25vw', display: 'flex', justifyContent: 'flex-end' }}>
              {hasNextButton() && <Button size="lg" className="button-blue" onPress={handleClickNext}>{isConcludeButton() ? "Conclude" : "Next"}</Button>}
            </div>
          </div>
        ) : (
          <div style={{ display: 'flex', justifyContent: 'center' }}>
            <Button size="lg" className="button-blue" onPress={handleClickHome}>Home</Button>
          </div>
        )}

      </div>

      <div style={{ display: 'flex', justifyContent: 'center', margin: '5vh 15vw' }}>
        <Progress value={(currentStep + 1) * 100 / 4} color="#1F0F53" />
      </div>

    </div>
  )
}

export default Reservation