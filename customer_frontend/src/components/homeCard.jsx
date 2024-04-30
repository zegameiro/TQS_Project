import { Card, CardFooter, CardHeader, Image, Button } from "@nextui-org/react";

export default function HomeCard({ text }) {

    let url = "/images/" + text.replace(" ", "-").toLowerCase() + ".jpg"

    return (
        <div style={{ width: '30vw' }}>
            <Card isFooterBlurred className="w-full h-[300px] col-span-12 sm:col-span-7">
                <Image
                    removeWrapper
                    alt={text}
                    className="z-0 w-full h-full object-cover"
                    src={url}
                />
                <CardFooter className="absolute bottom-0 grid place-items-center w-full">
                    <Button radius="full" size="lg" variant="shadow" className="button-home">{text}</Button>
                </CardFooter>
            </Card>
        </div>
    );
}