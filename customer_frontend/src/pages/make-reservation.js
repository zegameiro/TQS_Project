// 'use client';

import "./../app/globals.css";
import { useState } from "react";
import { Button, Progress } from "@nextui-org/react";


import ChooseService from "@/components/make-reservation/chooseService";
import PickTimeSlot from "@/components/make-reservation/pickTimeSlot";
import Payment from "@/components/make-reservation/payment";
import Confirmation from "@/components/make-reservation/confirmation";


export default function MakeReservation() {

    const steps = ["Choose service(s)", "Pick a Time Slot", "Payment", "Here it is your confirmation"]
    const components = [<ChooseService />, <PickTimeSlot />, <Payment />, <Confirmation />]
    const [currentStep, setCurrentStep] = useState(0)

    const handleClickBack = () => {
        console.log("Button pressed")
        setCurrentStep(currentStep - 1)
    };

    const handleClickNext = () => {
        console.log("Button pressed")
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


    return (
        <main>
            <div style={{ height: '10vh' }}></div>
            <div className="reservation-section">
                <h1 style={{ textAlign: 'center', color: '#1F0F53', fontSize: '40px', fontWeight: 'bold' }}>{steps[currentStep]}</h1>
                <div className="reservation-form">
                    {components[currentStep]}
                </div>

                <div style={{ margin: '0 5vw', display: 'flex', flexDirection: 'row' }}>
                    <div style={{ width: '50vw' }}>
                        {hasPreviousButton() && <Button size="lg" className="button-blue" onPress={handleClickBack}>Back</Button>}
                    </div>
                    <div style={{ width: '50vw', display: 'flex', justifyContent: 'flex-end' }}>
                        {hasNextButton() && <Button size="lg" className="button-blue" onPress={handleClickNext}>{isConcludeButton() ? "Conclude" : "Next"}</Button>}
                    </div>
                </div>

            </div>

            <div style={{ display: 'flex', justifyContent: 'center', margin: '5vh 15vw' }}>
                <Progress value={(currentStep + 1) * 100 / 4} color="#1F0F53" />
            </div>

        </main>
    );
}
