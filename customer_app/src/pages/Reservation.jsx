
import React, { useState } from "react";
import { Button, Progress } from "@nextui-org/react";

import NavbarFixed from "../components/NavbarFixed"
import ChooseService from "../components/make-reservation/chooseService";
import PickTimeSlot from "../components/make-reservation/pickTimeSlot";
import Payment from "../components/make-reservation/payment";
import Confirmation from "../components/make-reservation/confirmation";

import { useNavigate } from "react-router-dom";

const Reservation = () => {

  const steps = ["Choose services", "Pick a Time Slot", "Payment", "Here it is your reservation"]
  const components = [<ChooseService />, <PickTimeSlot />, <Payment />, <Confirmation />]
  const [currentStep, setCurrentStep] = useState(0)

  // get URL
  const url = window.location.href.split('?')[1];
  const location = url.split('+')[0];
  const service = decodeURIComponent(url.split('+')[1]);

  const categories = JSON.parse(localStorage.getItem("categories"));
  // simulate API call
  const selectedCategory = categories.find(category => category.title === service);



  // navigation
  const navigate = useNavigate();

  const handleClickHome = () => {
    navigate("/")
  }

  const handleClickBack = () => {
    setCurrentStep(currentStep - 1)
  };

  const handleClickNext = () => {
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
        <div className="w-[70%] ml-[15%] mt-5 mb-10 h-[50vh] p-10 border-primary" style={{ border: '.125rem solid #220f67', borderRadius: '1rem' }}>
          {
            currentStep === 0 ? <ChooseService services={selectedCategory.services}/> : components[currentStep]
          }
        </div>

        {!isLastStep() ? (
          <div style={{ margin: '0 5vw', display: 'flex', flexDirection: 'row' }}>
            <div style={{ width: '50vw' }}>
              {hasPreviousButton() && <Button size="lg" className="button-blue" onPress={handleClickBack}>Back</Button>}
            </div>
            <div style={{ width: '50vw', display: 'flex', justifyContent: 'flex-end' }}>
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