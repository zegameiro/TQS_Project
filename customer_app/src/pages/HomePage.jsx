import React, { useState } from "react";
import { Select, SelectItem } from "@nextui-org/react";

import NavbarFixed from "../components/NavbarFixed"

import { BasicHairDresser, ComplexHairDresser, Makeup, Depilation, Manicure, Massager } from "../images";
import SwiperServices from "../components/SwiperServices";
import ServiceCard from "../components/ServiceCard";


const HomePage = () => {

  const [selectedLocation, setSelectedLocation] = useState("")


  const categories = [
    {
      title: "Basic Hairdresser",
      services: [
        {
          name: "Hair Cut",
          price: 10,
        },
        {
          name: "Beard Trimming",
          price: 5,
        },
        {
          name: "Washing",
          price: 5,
        },
        {
          name: "Brushing",
          price: 5,
        },
      ],
      image: BasicHairDresser,
    },
    {
      title: "Complex Hairdresser",
      services: [
        {
          name: "Extensions",
          price: 50,
        },
        {
          name: "Coloring",
          price: 30,
        },
        {
          name: "Discoloration",
          price: 30,
        },
        {
          name: "Straightening/Curling",
          price: 30,
        },
        {
          name: "Perm",
          price: 30,
        },
      ],
      image: ComplexHairDresser,
    },
    {
      title: "Makeup",
      services: [
        {
          name: "Eyebrows",
          price: 5,
        },
        {
          name: "Eyelashes",
          price: 5,
        },
        {
          name: "Lips",
          price: 5,
        },
        {
          name: "Full Face",
          price: 20,
        },
        {
          name: "Special Occasions",
          price: 30,
        },
      ],
      image: Makeup,
    },
    {
      title: "Depilation",
      services: [
        {
          name: "Wax hair removal",
          price: 10,
        },
        {
          name: "Laser hair removal",
          price: 50,
        },
        {
          name: "Tweezers",
          price: 5,
        },
        {
          name: "Thread",
          price: 5,
        },
        {
          name: "Epilator",
          price: 5,
        },
        {
          name: "Sugaring",
          price: 10,
        },
      ],
      image: Depilation,
    },
    {
      title: "Manicure/Pedicure",
      services: [
        {
          name: "Manicure",
          price: 30,
        },
        {
          name: "Pedicure",
          price: 30,
        },
      ],
      image: Manicure,
    },
    {
      title: "Spa",
      services: [
        {
          name: "Massages",
          price: 20,
        },
        {
          name: "Facial treatments",
          price: 15,
        },
        {
          name: "Body treatments",
          price: 20,
        },
        {
          name: "Dermatological treatments",
          price: 30,
        },
        {
          name: "Sauna",
          price: 25,
        },
        {
          name: "Jacuzzi",
          price: 25,
        },
        {
          name: "Turkish bath",
          price: 25,
        },
        {
          name: "Pools",
          price: 20,
        },
      ],
      image: Massager,
    },
  ]
  localStorage.setItem("categories", JSON.stringify(categories));

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

export default HomePage