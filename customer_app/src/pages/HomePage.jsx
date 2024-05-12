import React, { useState } from "react";
import { Select, SelectItem } from "@nextui-org/react";

import NavbarFixed from "../components/NavbarFixed"
import ServiceCard from "../components/ServiceCard";

import { BasicHairDresser, ComplexHairDresser, Makeup, Depilation, Manicure, Massager } from "../images";

const HomePage = () => {

  const [selectedLocation, setSelectedLocation] = useState("")


  const categories = [
    {
      title: "Basic Hairdresser",
      services: ["Hair Cut", "Beard Trimming"],
      image: BasicHairDresser,
    },
    {
      title: "Complex Hairdresser",
      services: ["Extensions", "Coloring", "Straightening/Curling"],
      image: ComplexHairDresser,
    },
    {
      title: "Makeup",
      services: ["Eyebrows", "Eyelashes"],
      image: Makeup,
    },
    {
      title: "Depilation",
      services: ["Wax hair removal", "Laser hair removal"],
      image: Depilation,
    },
    {
      title: "Manicure/Pedicure",
      services: ["Manicure", "Pedicure"],
      image: Manicure,
    },
    {
      title: "Spa",
      services: ["Massages", "Facial treatments", "Body treatments", "Dermatological treatments", "Sauna", "Jacuzzi", "Turkish bath", "Pools"],
      image: Massager,
    },
  ]

  const locations = [
    {
      id: "Aveiro",
      name: "Aveiro",
    },
    {
      id: "Lisbon",
      name: "Lisbon",
    },
    {
      id: "Porto",
      name: "Porto",
    }
  ]

  const services = locations.map(city => ({
    location: city.name,
    categories: categories,
  }));


  return (
    <div className="min-h-screen flex flex-col">
      <NavbarFixed />
      <div className="w-full flex flex-col p-4">
        <div style={{ display: 'flex', flexDirection: 'row' }}>
          <h1 className="py-4 px-[5rem] font-semibold text-2xl">Choose your service</h1>
          <Select
            items={locations}
            label="Filter by location"
            placeholder="All locations"
            className="max-w-xs"
            onChange={(city) => setSelectedLocation(city.target.value)}
          >
            {(city) => <SelectItem key={city.id}>{city.name}</SelectItem>}
          </Select>
        </div>
        {services.map((service, index) => (
          (selectedLocation === "" || selectedLocation === service.location) &&
          <div key={index}>
            <h2 className="font-semibold text-4xl text-center">{service.location}</h2>
            <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-5 mt-5 mb-8 items-center justify-center sm:px-10 md:px-20">
              {
                service.categories.map((service, index) => (
                  <div key={index} className="flex flex-col p-4">
                    <ServiceCard
                      service={service}
                    />
                  </div>
                ))
              }
            </div>
          </div>
        ))
        }
      </div>
    </div >
  )
}

export default HomePage