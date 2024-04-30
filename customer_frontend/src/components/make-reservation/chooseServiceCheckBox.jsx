import React from "react";
import { CheckboxGroup, Checkbox } from "@nextui-org/react";

export default function ChooseServiceCheckBox({ category, services }) {
    return (
        <div style={{marginBottom:'5vh'}}>
            <CheckboxGroup
                label={category}
                orientation="horizontal"
            >
                {services.map((service) => (
                    <Checkbox size="lg" value={service.replace(" ", "-").toLowerCase()}>{service}</Checkbox>
                ))}
            </CheckboxGroup>
        </div>
    );
}
