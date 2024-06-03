import React, { useState } from "react";
import { Select, SelectItem } from "@nextui-org/react";

import NavbarFixed from "../components/NavbarFixed"

import { BasicHairDresser, ComplexHairDresser, Makeup, Depilation, Manicure, Massager } from "../images";
import SwiperServices from "../components/SwiperServices";
import ServiceCard from "../components/ServiceCard";

import { useMutation, useQuery } from "@tanstack/react-query"
import axios from "../../api"
import { getAllFacilities, getAllRooms } from "../../actions/getActions";


export default function HomePage() {

  const images = [BasicHairDresser, ComplexHairDresser, Makeup, Depilation, Manicure, Massager]

  const [selectedLocation, setSelectedLocation] = useState("")

  const allFacilities = useQuery({
    queryKey: ["allFacilities"],
    queryFn: () => getAllFacilities(axios),
  })

  const allRooms = useQuery({
    queryKey: ["allRooms"],
    queryFn: () => getAllRooms(axios),
  })

  console.log(allRooms.data)

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

  const services = allFacilities.data?.map(facility => {
    const facilityRooms = allRooms.data?.filter(room => room.facility.city === facility.city);
    const categories = facilityRooms?.map((room, index) => ({
      title: room.name,
      id: room.id,
      image: images[index],
    }));

    return {
      location: facility.city,
      categories: categories,
    };
  });


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
        {services?.map((service, index) => (
          (selectedLocation === "" || selectedLocation === service.location) &&
          <div key={index} className="mb-10">
            <h2 className="font-semibold text-4xl text-center">{service.location}</h2>
            <SwiperServices data={service.categories} location={service.location} />
          </div>
        ))
        }
      </div>
    </div >
  )
}