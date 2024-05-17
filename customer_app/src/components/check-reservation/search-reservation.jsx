import { Button, Input } from "@nextui-org/react";


const SearchReservation = ({ setTokenToSearch, setCurrentStep }) => {

    const handleSearch = () => {
        const token = document.getElementsByTagName('input')[0].value;
        setTokenToSearch(token);
        setCurrentStep(1);
    }

    return (
        <div className="w-[60%] h-[100%] ml-[20%] check-reservation" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
            <Input variant="underlined" type="text" size="lg" label="Insert your reservation token" />
            <Button color="primary" className="text-white ml-[25px] button" size="lg" onClick={handleSearch}>Search</Button>
        </div>
    );

}

export default SearchReservation;