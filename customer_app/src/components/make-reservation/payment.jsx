import { useState } from 'react';
import { Input } from "@nextui-org/react";
import PaymentLabel from "./paymentLabel";
import { Select, SelectItem } from "@nextui-org/react";

const Payment = () => {

    let payment_methods = [
        { label: "Bank Transaction", value: "bank" },
        { label: "Paypal", value: "paypal" },
        { label: "MB WAY", value: "mb_way" },
    ]

    const [paymentMethod, setPaymentMethod] = useState('');

    const handlePaymentMethodChange = (incoming) => {
        let value = incoming.target.value;
        setPaymentMethod(value);
    };



    return (
        <div>
            <div style={{ display: 'flex', flexDirection: 'row' }}>
                <div style={{ width: '45vw' }}>
                    <PaymentLabel label_="Name" type_="name" />
                    <PaymentLabel label_="Email" type_="email" />
                    <PaymentLabel label_="Phone" type_="tel" />
                    <PaymentLabel label_="Address" type_="text" />
                </div>
                <div style={{ width: '10vw' }}>
                </div>
                <div style={{ width: '45vw' }}>
                    <div>
                        <span style={{ fontSize: '4vh', color: '#1F0F53' }}>Total to Pay</span>
                        <span style={{ fontSize: '4vh', color: '#1F0F53', float: 'right' }}>€ 100</span>
                    </div>
                    <div style={{ marginTop: '3vh' }}>
                        <Select
                            items={payment_methods}
                            label="Payment Method"
                            variant="bordered"
                            onChange={handlePaymentMethodChange}
                        >
                            {(method) => <SelectItem key={method.value}>{method.label}</SelectItem>}
                        </Select>
                    </div>
                    <div style={{ marginTop: '2.5vh' }}>
                        {paymentMethod === "mb_way" &&
                            <Input variant="bordered" type={"tel"} label={"Number"} />
                        }
                    </div>
                </div>
            </div>

        </div>
    );
}

export default Payment;