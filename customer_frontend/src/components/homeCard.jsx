'use client';

import { Card, CardFooter, Image, Button } from "@nextui-org/react";

export default function HomeCard({ text }) {

    let link = text.replace(" ", "-").toLowerCase()

    let imageURL = "/images/" + link + ".jpg"

    const handleClickButton = () => {
        window.location.href = link
    };

    return (
        <div style={{ width: '30vw' }}>
            <Card isFooterBlurred className="w-full h-[300px] col-span-12 sm:col-span-7">
                <Image
                    removeWrapper
                    alt={text}
                    className="z-0 w-full h-full object-cover"
                    src={imageURL}
                />
                <CardFooter className="absolute bottom-0 grid place-items-center w-full">
                    <Button radius="full" size="lg" variant="shadow" className="button-blue" onPress={handleClickButton} >{text}</Button>
                </CardFooter>
            </Card>
        </div>
    );
}