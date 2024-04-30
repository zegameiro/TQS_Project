import ChooseServiceCheckBox from "./chooseServiceCheckBox";

export default function ChooseService() {

    return (
        <div style={{ padding: '5vh 5vw' }}>
            <div style={{ display: 'flex', flexDirection: 'row' }}>
                <div style={{ width: '50vw' }}>
                    <ChooseServiceCheckBox category="BASIC HAIDRESSER" services={["haircut", "beard trimming"]} />
                    <ChooseServiceCheckBox category="COMPLEX HAIDRESSER" services={["extensions", "coloring", "straightening", "curling"]} />
                    <ChooseServiceCheckBox category="MAKEUP" services={["makeup"]} />
                </div>
                <div>
                    <ChooseServiceCheckBox category="DEPILATION" services={["wax hair removal", "laser hair removal"]} />
                    <ChooseServiceCheckBox category="MANICURE/PEDICURE" services={["manicure", "pedicure"]} />
                    <ChooseServiceCheckBox category="SPA" services={["Massages", "Facial treatments", "Dermatological treatments", "Sauna", "Jacuzzi", "Turkish bath", "Pools"]} />
                </div>
            </div>
        </div>
    );
}