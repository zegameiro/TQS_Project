import { useState } from 'react';
import { Input } from "@nextui-org/react";
import PaymentLabel from "./paymentLabel";
import { Select, SelectItem } from "@nextui-org/react";

const Payment = ({ services, selectedServices, selectedPaymentData, setSelectedPaymentData, setPriceToPay}) => {

    const paymentLabels = [
        { label: "Name", type: "name" },
        { label: "Email", type: "email" },
        { label: "Phone", type: "tel" },
        { label: "Address", type: "text" },
    ]

    let priceToPay = 0;
    let v;
    selectedServices.forEach(service => {
        v = services.find(s => s.name === service);
        priceToPay += v.price;
    });
    setPriceToPay(priceToPay);



    return (
        <div>
            <div style={{ display: 'flex', flexDirection: 'row' }}>
                <div style={{ width: '45vw' }}>
                    {paymentLabels.map((label, index) => {
                        return (
                            <PaymentLabel key={index} label_={label.label} type_={label.type} selectedValue={selectedPaymentData[index]} setSelectedValue={(value) => setSelectedPaymentData[index](value)} />
                        )
                    })}
                </div>
                <div style={{ width: '10vw' }}>
                </div>
                <div style={{ width: '45vw' }}>
                    <div>
                        <span style={{ fontSize: '4vh', color: '#1F0F53' }}>Total to Pay</span>
                        <span style={{ fontSize: '4vh', color: '#1F0F53', float: 'right' }}>€ {priceToPay}</span>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Payment;