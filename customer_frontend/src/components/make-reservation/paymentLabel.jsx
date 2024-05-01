import { Input } from "@nextui-org/react";

export default function PaymentLabel({ type_, label_ }) {

    return (
        <div style={{ marginBottom: '2.5vh' }}>
            <Input variant="bordered" type={type_} label={label_} />
        </div>
    )
}