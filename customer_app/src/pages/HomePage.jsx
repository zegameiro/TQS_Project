
import NavbarFixed from "../components/NavbarFixed"
import ServiceCard from "../components/ServiceCard";

import { HairDresser, Massager, Manicure } from "../images";

const HomePage = () => {

  const services = [
    {
      title: "Hairdresser",
      location: "Aveiro",
      description: "Hair Cut",
      price: 10,
      image: HairDresser,
    },
    {
      title: "Massage",
      location: "Lisbon",
      description: "Back Massage",
      price: 30,
      image: Massager,
    },
    {
      title: "Manicure",
      location: "Porto",
      description: "Pedicure",
      price: 34,
      image: Manicure,
    },
    {
      title: "hairdresser",
      location: "Aveiro",
      description: "Hair Cut",
      price: 22,
      image: HairDresser,
    },
    {
      title: "Hairdresser",
      location: "Aveiro",
      description: "Hair Cut",
      price: 10,
      image: HairDresser,
    },
    {
      title: "Massage",
      location: "Lisbon",
      description: "Back Massage",
      price: 30,
      image: Massager,
    },
    {
      title: "Manicure",
      location: "Porto",
      description: "Pedicure",
      price: 34,
      image: Manicure,
    },
    {
      title: "hairdresser",
      location: "Aveiro",
      description: "Hair Cut",
      price: 22,
      image: HairDresser,
    },
    {
      title: "Hairdresser",
      location: "Aveiro",
      description: "Hair Cut",
      price: 10,
      image: HairDresser,
    },
    {
      title: "Massage",
      location: "Lisbon",
      description: "Back Massage",
      price: 30,
      image: Massager,
    },
    {
      title: "Manicure",
      location: "Porto",
      description: "Pedicure",
      price: 34,
      image: Manicure,
    },
    {
      title: "hairdresser",
      location: "Aveiro",
      description: "Hair Cut",
      price: 22,
      image: HairDresser,
    },
  ]

  return (
    <div className="min-h-screen flex flex-col">
      <NavbarFixed />
      <div className="w-full flex flex-col p-4">
        <h1 className="py-4 px-[5rem] font-semibold text-2xl">Choose you're service</h1>
        <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-5 items-center justify-center sm:px-10 md:px-20">
          {services.map((service, index) => (
            <div key={index} className="flex flex-col p-4">
              <ServiceCard
                service={service}
              />
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}

export default HomePage