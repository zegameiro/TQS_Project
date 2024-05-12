import { Card, CardHeader, CardBody, Image, Button } from "@nextui-org/react";
import { useNavigate } from "react-router-dom";

const ServiceCard = ({ service }) => {

  const navigate = useNavigate();

  return (
    <Card className="py-4 hover:scale-110 cursor-pointer">

      <CardHeader className="pb-0 pt-2 px-4 flex-col items-start">
        <p className="text-tiny uppercase font-bold">{service?.title}</p>
        <small className="text-default-500">{service?.location}</small>
        <h4 className="font-bold text-large">{service?.description}</h4>
      </CardHeader>

      <CardBody className="overflow-visible py-2 items-center gap-4">
        <Image
          alt="Card background"
          className="object-cover rounded-xl"
          src={service?.image}
          width={270}
        />
        <Button color="primary" className="text-white" onClick={() => navigate('/reservation')}>
          Choose service
        </Button>
      </CardBody>

    </Card>
  );
};

export default ServiceCard;
