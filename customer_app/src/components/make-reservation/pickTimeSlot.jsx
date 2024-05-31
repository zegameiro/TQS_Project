import { Calendar, Select, SelectItem } from "@nextui-org/react";
import { parseDate } from '@internationalized/date';
import { today, getLocalTimeZone } from "@internationalized/date";


const PickTimeSlot = ({ selectedDate, setSelectedDate, selectedHour, setSelectedHour, selectedMinute, setSelectedMinute }) => {

    const hours = Array.from({ length: 12 }, (_, i) => ({ key: String(i + 8).padStart(2, '0'), label: String(i + 8).padStart(2, '0') }));
    const minutes = [0, 30].map(minute => ({ key: String(minute).padStart(2, '0'), label: String(minute).padStart(2, '0') }));

    return (
        <div>
            <div style={{ display: 'flex', flexDirection: 'row', height: '40vh' }}>
                <div style={{ width: '10vw' }}></div>
                <div style={{ width: '40vw', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                    <Calendar aria-label="Reservation Date" minValue={today(getLocalTimeZone())}
                        value={parseDate(selectedDate)}
                        onChange={(e) => { console.log(e); setSelectedDate(e.year+"-"+String(e.month).padStart(2, '0')+"-"+String(e.day).padStart(2, '0')) }}
                    />
                </div>
                <div style={{ width: '40vw', display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '1rem' }}>
                    <Select
                        label="Hour"
                        placeholder="HH"
                        className="max-w-xs"
                        defaultSelectedKeys={[selectedHour]}
                        onChange={(e) => { setSelectedHour(e.target.value); }}
                    >
                        {hours.map((hour) => (
                            <SelectItem key={hour.key}>
                                {hour.label}
                            </SelectItem>
                        ))}
                    </Select>
                    <Select
                        label="Minutes"
                        placeholder="MM"
                        className="max-w-xs"
                        defaultSelectedKeys={[selectedMinute]}
                        onChange={(e) => { setSelectedMinute(e.target.value); }}
                    >
                        {minutes.map((minute) => (
                            <SelectItem key={minute.key}>
                                {minute.label}
                            </SelectItem>
                        ))}
                    </Select>

                </div>
                <div style={{ width: '10vw' }}></div>
            </div>
        </div>
    )
}

export default PickTimeSlot;