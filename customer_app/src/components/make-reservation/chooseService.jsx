import { CheckboxGroup, Checkbox } from "@nextui-org/react";

const ChooseService = ({ services = [] }) => {
    const chunkArray = (arr, chunkSize) => {
        const chunkedArr = [];
        for (let i = 0; i < arr.length; i += chunkSize) {
            chunkedArr.push(arr.slice(i, i + chunkSize));
        }
        return chunkedArr;
    };

    const chunkedServices = chunkArray(services, 3);

    return (
        <div style={{ marginBottom: '5vh' }}>
            {chunkedServices.map((chunk, index) => ( 
                <CheckboxGroup key={index} orientation="horizontal" style={{ display: 'flex' }}>
                    {chunk.map((service, serviceIndex) => (
                        <div key={serviceIndex} style={{ width: '20vw' }}>
                            <Checkbox size="lg" className="text-white mb-1" value={service.name}>
                                {service.name}
                                <br />
                                <b>{service.price.toFixed(2)}€</b>
                            </Checkbox>
                        </div>
                    ))}
                </CheckboxGroup>
            ))}
        </div>
    );
};

export default ChooseService;
