import { CheckboxGroup, Checkbox } from "@nextui-org/react";

const ChooseService = ({ service, services, selectedServices, setSelectedServices }) => {
    const chunkArray = (arr, chunkSize) => {
        const chunkedArr = [];
        for (let i = 0; i < arr.length; i += chunkSize) {
            chunkedArr.push(arr.slice(i, i + chunkSize));
        }
        return chunkedArr;
    };

    const chunkedServices = chunkArray(services, 3);

    const updateSelectedServices = () => (service) => {
        setSelectedServices(service);
    }


    return (
        <div>
            <h1 className="text-center text-primary text-2xl mb-5">{service}</h1>
            <CheckboxGroup orientation="vertical" style={{ display: 'flex' }} defaultValue={selectedServices} onChange={updateSelectedServices()}>
                {chunkedServices.map((chunk, index) => (
                    <div key={index} style={{display:'flex', flexDirection:'row'}} >
                        {chunk.map((service, serviceIndex) => (
                            <div key={serviceIndex} style={{ width: '20vw' }}>
                                <Checkbox
                                    key={serviceIndex}
                                    size="lg"
                                    className="text-white mb-1"
                                    value={service.name}
                                >
                                    {service.name}
                                    <br />
                                    <b>{service.price.toFixed(2)}€</b>
                                </Checkbox>
                            </div>
                        ))}
                    </div>
                ))}
            </CheckboxGroup>
        </div>

    );
};

export default ChooseService;
