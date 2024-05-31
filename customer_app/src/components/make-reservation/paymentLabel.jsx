import { Input } from "@nextui-org/react";

const PaymentLabel = ({ label_, type_, selectedValue, setSelectedValue }) => {


    return (
        <div style={{ marginBottom: '2.5vh' }}>
            <Input variant="bordered" type={type_} label={label_} value={selectedValue} onChange={(event) => setSelectedValue(event.target.value)} />
        </div>
    )
}

export default PaymentLabel;