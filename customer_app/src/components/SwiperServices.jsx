import React, { useRef, useState } from 'react';
// Import Swiper React components
import { Swiper, SwiperSlide } from 'swiper/react';
import ServiceCard from "../components/ServiceCard";


// Import Swiper styles
import 'swiper/css';
import 'swiper/css/navigation';

// import required modules
import { Navigation } from 'swiper/modules';

export default function SwiperServices({ data, location }) {

    return (
        <div className='p-10'>
            <Swiper
                slidesPerView={4}
                spaceBetween={20}
                navigation={true}
                modules={[Navigation]}
                className="mySwiper"
            >

                {data.map((category, index) => (
                    <SwiperSlide key={index}>
                        <div className="flex flex-col px-10 py-0">
                            <ServiceCard
                                service={category}
                                location={location}
                            />
                        </div>
                    </SwiperSlide>
                ))}

            </Swiper>
        </div>
    );
}
