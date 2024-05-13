import { Input } from "@nextui-org/react";

const PaymentLabel = ({ label_, type_ }) => {
    return (
        <div style={{ marginBottom: '2.5vh' }}>
            <Input variant="bordered" type={type_} label={label_} />
        </div>
    )
}   

export default PaymentLabel;